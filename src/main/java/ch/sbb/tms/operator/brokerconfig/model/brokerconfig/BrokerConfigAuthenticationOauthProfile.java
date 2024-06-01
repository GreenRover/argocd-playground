package ch.sbb.tms.operator.brokerconfig.model.brokerconfig;

import community.solace.broker.deployment.engine.brokerfacade.AuthenticationOauthProfile;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("solace.broker.sbb.ch")
@Version("v1alpha1")
@Kind("AuthenticationOauthProfile")
public class BrokerConfigAuthenticationOauthProfile extends CustomResource<AuthenticationOauthProfile, BrokerConfigStatus> implements Namespaced {
}
