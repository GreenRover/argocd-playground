package ch.sbb.tms.operator.brokerconfig.model.brokerconfig;

import community.solace.broker.deployment.engine.brokerfacade.JndiConnectionFactory;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("solace.broker.sbb.ch")
@Version("v1alpha1")
@Kind("Jndi")
public class BrokerConfigJndiConnectionFactory extends CustomResource<JndiConnectionFactory, BrokerConfigStatus> implements Namespaced {
}
