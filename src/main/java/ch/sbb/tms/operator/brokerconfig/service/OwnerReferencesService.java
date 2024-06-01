package ch.sbb.tms.operator.brokerconfig.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ch.sbb.tms.operator.brokerconfig.model.broker.Broker;
import ch.sbb.tms.operator.brokerconfig.model.broker.BrokerStatus;
import ch.sbb.tms.operator.brokerconfig.model.broker.BrokerStatusEnum;
import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigAclProfile;
import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigAuthenticationOauthProfile;
import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigAuthorizationGroup;
import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigClientProfile;
import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigClientUsername;
import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigJndiConnectionFactory;
import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigQueue;
import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigQueueTemplate;
import ch.sbb.tms.operator.brokerconfig.utils.Debounce;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.OwnerReference;
import io.fabric8.kubernetes.api.model.OwnerReferenceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerReferencesService {

    private final KubernetesClient kubernetesClient;
    private final Map<String, Debounce> tasks = new ConcurrentHashMap<>();

    public void updateOwnerReferences(String namespace) {
        tasks
                .computeIfAbsent(
                        namespace,
                        n -> new Debounce(
                                Duration.ofSeconds(1),
                                Duration.ofSeconds(10),
                                () -> {
                                    Broker broker = getFirstBroker(namespace);
                                    if (broker == null) {
                                        return;
                                    }

                                    updateOwnerReferences(namespace, broker, BrokerConfigAclProfile.class);
                                    updateOwnerReferences(namespace, broker, BrokerConfigAuthenticationOauthProfile.class);
                                    updateOwnerReferences(namespace, broker, BrokerConfigAuthorizationGroup.class);
                                    updateOwnerReferences(namespace, broker, BrokerConfigClientProfile.class);
                                    updateOwnerReferences(namespace, broker, BrokerConfigClientUsername.class);
                                    updateOwnerReferences(namespace, broker, BrokerConfigJndiConnectionFactory.class);
                                    updateOwnerReferences(namespace, broker, BrokerConfigQueue.class);
                                    updateOwnerReferences(namespace, broker, BrokerConfigQueueTemplate.class);
                                }
                        ))
                .tick();
    }

    private Broker getFirstBroker(String namespace) {
        List<Resource<Broker>> brokers = kubernetesClient.resources(Broker.class)
                                                         .inNamespace(namespace)
                                                         .resources()
                                                         .toList();
        if (CollectionUtils.isEmpty(brokers)) {
            return null;
        }

        if (brokers.size() == 1) {
            return brokers.getFirst()
                          .item();
        }


        String concatBrokerNames = brokers.stream()
                                          .map(resource -> resource.item()
                                                                   .getMetadata()
                                                                   .getName())
                                          .collect(Collectors.joining(", "));

        Resource<Broker> first = brokers.getFirst();

        for (Resource<Broker> broker : brokers) {
            if (Objects.equals(broker, first)) {
                continue;
            }

            try {
                BrokerStatus status = broker.item()
                                            .getStatus();
                status.setStatus(BrokerStatusEnum.FAILED);
                status.setStatusMsg("Only one broker per namespace is supported. Conflicting items are: " + concatBrokerNames + ". Selected broker: " + first.item()
                                                                                                                                                             .getMetadata()
                                                                                                                                                             .getName());
                broker.updateStatus();
            }

            catch (Exception e) {
                log.trace("cannot update error on head entry: ", e);
            }
        }

        return first.item();
    }

    private <T extends HasMetadata> void updateOwnerReferences(String namespace, Broker broker, Class<T> clazz) {
        List<Resource<T>> resources = kubernetesClient.resources(clazz)
                                                      .inNamespace(namespace)
                                                      .resources()
                                                      .toList();

        for (Resource<T> resource : resources) {
            T item = resource.item();
            if (!isMatching(item.getMetadata()
                                .getOwnerReferences(), broker)) {
                item.getMetadata()
                    .setOwnerReferences(List.of(
                            new OwnerReferenceBuilder()
                                    .withApiVersion(broker.getApiVersion())
                                    .withKind(broker.getKind())
                                    .withName(broker.getMetadata()
                                                    .getName())
                                    .withUid(broker.getMetadata()
                                                   .getUid())
                                    .withBlockOwnerDeletion()
                                    .build()
                    ));

                log.info(
                        "UpdateOwnerReference {}: {} namespace: {} --> to broker: {}",
                        item.getKind(),
                        item.getMetadata()
                            .getName(),
                        item.getMetadata()
                            .getNamespace(),
                        broker.getMetadata()
                              .getName()
                );

                resource.update();
            }
        }
    }

    private boolean isMatching(List<OwnerReference> ownerReferences, Broker broker) {
        if (CollectionUtils.isEmpty(ownerReferences)) {
            return false;
        }

        for (OwnerReference ownerReference : ownerReferences) {
            if (!Objects.equals(ownerReference.getApiVersion(), broker.getApiVersion())) {
                continue;
            }

            if (!Objects.equals(ownerReference.getKind(), broker.getKind())) {
                continue;
            }

            if (!Objects.equals(ownerReference.getName(), broker.getMetadata()
                                                                .getName())) {
                continue;
            }

            if (!Objects.equals(ownerReference.getUid(), broker.getMetadata()
                                                               .getUid())) {
                continue;
            }

            return true;
        }

        return false;
    }

}
