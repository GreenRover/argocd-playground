package ch.sbb.tms.operator.brokerconfig.model.broker;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("solace.broker.sbb.ch")
@Version("v1alpha1")
public class Broker extends CustomResource<BrokerSpec, BrokerStatus> implements Namespaced {
}
