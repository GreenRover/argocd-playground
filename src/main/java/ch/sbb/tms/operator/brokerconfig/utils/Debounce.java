package ch.sbb.tms.operator.brokerconfig.utils;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Run a task after a particular time span has passed without another task been queued
 * or maxDelay is reached.
 * <a href="https://rxjs.dev/assets/images/marble-diagrams/debounceTime.png">DebounceTime marble diagram</a>
 */
@Slf4j
@RequiredArgsConstructor
public class Debounce {
    private final Duration delay;
    private final Duration maxDelay;
    private final Runnable task;
    private final AtomicLong lastTick = new AtomicLong(0);
    private final AtomicLong lastExecute = new AtomicLong(0);
    private final Object sync = new Object();

    private ExecutorService executorService;

    private volatile boolean running = false;

    private void init() {
        if (executorService != null) {
            return;
        }
        lastExecute.set(System.currentTimeMillis());

        running = true;
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            while (running) {
                try {
                    synchronized (sync) {
                        sync.wait(delay.toMillis());
                        run();
                    }
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void cleanup() {
        running = false;
        executorService.shutdownNow();
        executorService = null;
    }

    private void run() {
        boolean maxDelayReached = (System.currentTimeMillis() - lastExecute.get()) > maxDelay.toMillis();
        boolean delaySinceLastTickReached = (System.currentTimeMillis() - lastTick.get()) > delay.toMillis();

        if (maxDelayReached || delaySinceLastTickReached) {
            try {
                task.run();
            }
            catch (Throwable t) {
                log.error("Unable to execute task", t);
            }
            finally {
                lastExecute.set(System.currentTimeMillis());
            }
        }

        boolean isZombie = (System.currentTimeMillis() - lastExecute.get()) > (maxDelay.toMillis() * 10);
        if (isZombie) {
            cleanup();
        }
    }

    public void tick() {
        lastTick.set(System.currentTimeMillis());
        init();
        synchronized (sync) {
            sync.notifyAll();
        }
    }
}
