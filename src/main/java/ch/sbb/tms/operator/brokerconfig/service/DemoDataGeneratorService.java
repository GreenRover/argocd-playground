package ch.sbb.tms.operator.brokerconfig.service;

import java.util.HashSet;
import java.util.Set;

import ch.sbb.tms.operator.brokerconfig.model.broker.Broker;
import ch.sbb.tms.operator.brokerconfig.model.broker.BrokerStatus;
import ch.sbb.tms.operator.brokerconfig.model.broker.BrokerStatusEnum;
import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigQueue;
import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigStatus;
import community.solace.broker.deployment.engine.brokerfacade.Queue;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NamespaceableResource;
import io.fabric8.kubernetes.client.dsl.base.PatchContext;
import io.fabric8.kubernetes.client.dsl.base.PatchType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemoDataGeneratorService {

    private final KubernetesClient kubernetesClient;
    private final Set<String> initiated = new HashSet<>();

    public void playDemoDataInThread(CustomResource<?, ?> resource) {
        String key = resource.getMetadata()
                             .getNamespace() + "__" + resource.getMetadata()
                                                              .getName() + "__" + resource.getMetadata()
                                                                                          .getUid();
        if (initiated.contains(key)) {
            return;
        }
        initiated.add(key);

        Thread t = new Thread(() -> this.playDemoData(resource));
        t.start();
    }

    @SneakyThrows
    public void playDemoData(CustomResource<?, ?> resource) {
        Thread.sleep(10_000);

        if (resource instanceof Broker broker) {
            playDemoDataBroker(broker);
        } else if (resource instanceof BrokerConfigQueue queue) {
            switch (queue.getMetadata()
                         .getName()) {
                case "demo-a":
                    playDemoDataQueueSuccess(queue);
                    break;
                case "manual-created-a":
                    playDemoDataQueueGhost(queue);
                    break;
                case "manual-created-b":
                    playDemoDataQueueGhost(queue);
                    break;
                case "demo-b":
                    playDemoDataQueueSuccess(queue);
                    break;
                case "demo-c":
                    playDemoDataQueueFailed(queue);
                    break;
            }
        }

    }

    @SneakyThrows
    public void playDemoDataBroker(Broker broker) {
        setStatus(broker, BrokerStatusEnum.PROGRESSING, "DemoDataGenerator started");
        log.info("DemoData: broker: set started");

        Thread.sleep(10_000);

        setStatus(broker, BrokerStatusEnum.FAILED, "DemoDataGenerator failed");
        log.info("DemoData: broker: set failed");
    }

    @SneakyThrows
    public void playDemoDataQueueSuccess(BrokerConfigQueue queue) {
        setStarted(queue);

        Thread.sleep(5_000);

        setStatus(queue, BrokerStatusEnum.SYNCED, "DemoDataGenerator success");
        log.info("DemoData: {}: set success", queue.getMetadata()
                                                   .getName());
    }

    @SneakyThrows
    public void playDemoDataQueueFailed(BrokerConfigQueue queue) {
        setStarted(queue);

        Thread.sleep(10_000);

        Queue q = new Queue();
        q.setMaxTtl(43656876L);
        q.setPartitionCount(9);

        BrokerConfigQueue toPatch = new BrokerConfigQueue();
        toPatch.setSpec(q);

        applyWithRetry(() -> kubernetesClient
                .resource(queue)
                .patch(PatchContext.of(PatchType.JSON_MERGE), toPatch));

        setStatus(queue, BrokerStatusEnum.FAILED, "DemoDataGenerator failed");
        log.info("DemoData: {}: set failed", queue.getMetadata()
                                                  .getName());
    }

    @SneakyThrows
    public void playDemoDataQueueGhost(BrokerConfigQueue queue) {
        setStarted(queue);

        Thread.sleep(10_000);

        // https://github.com/argoproj/argo-cd/blob/85ed1b96eaded150318348d2e6f468f11d2f32b3/ui/src/app/applications/components/utils.tsx#L742
        setStatus(queue, BrokerStatusEnum.MISSING, "DemoDataGenerator missing");
        log.info("DemoData: {}: set missing", queue.getMetadata()
                                                   .getName());
    }

    private void setStarted(BrokerConfigQueue queue) {
        setStatus(queue, BrokerStatusEnum.PROGRESSING, "DemoDataGenerator started");
        log.info("DemoData: {}: set started", queue.getMetadata()
                                                   .getName());
    }

    private void setStatus(Broker broker, BrokerStatusEnum s, String msg) {
            applyWithRetry(() -> {
                Broker fromServer = kubernetesClient
                        .resource(broker)
                        .require();

                BrokerStatus status = new BrokerStatus();
                status.setStatus(s);
                status.setStatusMsg(msg);
                fromServer.setStatus(status);

                kubernetesClient
                        .resource(fromServer)
                        .updateStatus();
            });
    }

    private void setStatus(BrokerConfigQueue queue, BrokerStatusEnum s, String msg) {
        applyWithRetry(() -> {
            BrokerConfigQueue fromServer = kubernetesClient
                    .resource(queue)
                    .require();

            BrokerConfigStatus status = new BrokerConfigStatus();
            status.setStatus(s);
            status.setStatusMsg(msg);
            fromServer.setStatus(status);

            kubernetesClient
                    .resource(fromServer)
                    .updateStatus();
        });
    }

    private void applyWithRetry(Runnable task) {
        int maxAttempts = 25;
        for (int attempt = 0; attempt <= maxAttempts; attempt++) {
            try {
                task.run();
                return;
            }
            catch (Exception e) {
                if (attempt == maxAttempts) {
                    throw e;
                }

                log.warn("Error while run k8n change: {}", e.getMessage());
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
            }
        }
    }
}
