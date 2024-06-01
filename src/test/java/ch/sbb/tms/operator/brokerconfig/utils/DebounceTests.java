package ch.sbb.tms.operator.brokerconfig.utils;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class DebounceTests {

    @Test
    void whenFirstTick_runShouldNotBeTriggered() {
        AtomicLong runs = new AtomicLong(0);

        Debounce d = new Debounce(
                Duration.ofMillis(100),
                Duration.ofMillis(500),
                runs::incrementAndGet
        );

        d.tick();

        assertEquals(0, runs.get(), "First tick should not trigger a run");
    }

    @Test
    void whenTicksAreInFastSequence_runShouldNotBeTriggered() throws InterruptedException {
        AtomicLong runs = new AtomicLong(0);

        Debounce d = new Debounce(
                Duration.ofMillis(100),
                Duration.ofMillis(500),
                runs::incrementAndGet
        );

        for (int i = 0; i < 15; i++) {
            d.tick();
            Thread.sleep(10);
        }

        assertEquals(0, runs.get(), "Tick with was interval should not trigger a run");
    }

    @Test
    void whenTicksNoTickForATimeout_runShouldBeTriggeredOnce() throws InterruptedException {
        AtomicLong runs = new AtomicLong(0);

        Debounce d = new Debounce(
                Duration.ofMillis(100),
                Duration.ofMillis(500),
                runs::incrementAndGet
        );

        for (int i = 0; i < 15; i++) {
            d.tick();
            Thread.sleep(10);
        }

        Thread.sleep(101);
        d.tick();

        assertEquals(1, runs.get(), "Tick with was interval should not trigger a run");
    }

    @Test
    void whenTicksAreInFastSequenceForLongerThanMaxDelay_runShouldBeTriggeredOnce() throws InterruptedException {
        AtomicLong runs = new AtomicLong(0);

        Debounce d = new Debounce(
                Duration.ofMillis(100),
                Duration.ofMillis(500),
                runs::incrementAndGet
        );

        for (int i = 0; i < 60; i++) {
            d.tick();
            Thread.sleep(10);
        }

        assertEquals(1, runs.get(), "Tick should be triggered once because maxDelay reached");
    }

    @Test
    void whenTicksInSlowInterval_runShouldBeTriggeredAlways() throws InterruptedException {
        AtomicLong runs = new AtomicLong(0);

        Debounce d = new Debounce(
                Duration.ofMillis(100),
                Duration.ofMillis(500),
                runs::incrementAndGet
        );

        for (int i = 0; i < 3; i++) {
            d.tick();
            Thread.sleep(110);
        }

        assertEquals(3, runs.get(), "Tick in slow interval should trigger always");
    }
}