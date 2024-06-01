package ch.sbb.tms.operator.brokerconfig.config;

import java.io.IOException;
import java.util.List;

import ch.sbb.tms.operator.brokerconfig.service.OwnerReferencesService;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.GenericReconcilerService;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.typed.BrokerConfigAclProfileReconciler;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.typed.BrokerConfigAuthenticationOauthProfileReconciler;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.typed.BrokerConfigAuthorizationGroupReconciler;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.typed.BrokerConfigClientProfileReconciler;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.typed.BrokerConfigClientUsernameReconciler;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.typed.BrokerConfigJndiConnectionFactoryReconciler;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.typed.BrokerConfigQueueReconciler;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.typed.BrokerConfigQueueTemplateReconciler;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.typed.BrokerReconciler;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReconcilerAutoRegister {

    @Bean
    public BrokerReconciler customServiceControllerBroker(GenericReconcilerService genericReconcilerService, OwnerReferencesService ownerReferencesService) {
        return new BrokerReconciler(genericReconcilerService, ownerReferencesService);
    }

    @Bean
    public BrokerConfigAclProfileReconciler customServiceControllerBrokerConfigAclProfile(GenericReconcilerService genericReconcilerService) {
        return new BrokerConfigAclProfileReconciler(genericReconcilerService);
    }

    @Bean
    public BrokerConfigAuthenticationOauthProfileReconciler customServiceControllerAuthenticationOauthProfileConfigQueue(GenericReconcilerService genericReconcilerService) {
        return new BrokerConfigAuthenticationOauthProfileReconciler(genericReconcilerService);
    }

    @Bean
    public BrokerConfigAuthorizationGroupReconciler customServiceControllerBrokerConfigAuthorizationGroup(GenericReconcilerService genericReconcilerService) {
        return new BrokerConfigAuthorizationGroupReconciler(genericReconcilerService);
    }

    @Bean
    public BrokerConfigClientProfileReconciler customServiceControllerBrokerConfigClientProfile(GenericReconcilerService genericReconcilerService) {
        return new BrokerConfigClientProfileReconciler(genericReconcilerService);
    }

    @Bean
    public BrokerConfigClientUsernameReconciler customServiceControllerBrokerConfigClientUsername(GenericReconcilerService genericReconcilerService) {
        return new BrokerConfigClientUsernameReconciler(genericReconcilerService);
    }

    @Bean
    public BrokerConfigJndiConnectionFactoryReconciler customServiceControllerBrokerConfigJndiConnectionFactory(GenericReconcilerService genericReconcilerService) {
        return new BrokerConfigJndiConnectionFactoryReconciler(genericReconcilerService);
    }

    @Bean
    public BrokerConfigQueueReconciler customServiceControllerBrokerConfigQueue(GenericReconcilerService genericReconcilerService) {
        return new BrokerConfigQueueReconciler(genericReconcilerService);
    }

    @Bean
    public BrokerConfigQueueTemplateReconciler customServiceControllerBrokerConfigQueueTemplate(GenericReconcilerService genericReconcilerService) {
        return new BrokerConfigQueueTemplateReconciler(genericReconcilerService);
    }

    @Bean
    public KubernetesClient kubernetesClient(KubernetesClientConfig kubernetesClientConfig) throws IOException {
        final Config config = new ConfigBuilder()
                .withOauthToken(kubernetesClientConfig.getAuthToken())
                .withHttpProxy(null)
                .withHttpsProxy(null)
                .withMasterUrl(kubernetesClientConfig.getUrl())
                .withNamespace(kubernetesClientConfig.getNamespace())
                .withTrustCerts(true)
                .build();
        return new KubernetesClientBuilder()
                .withConfig(config)
                .build();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @SuppressWarnings("rawtypes")
    public Operator operator(List<Reconciler> controllers, KubernetesClient kubernetesClient) throws IOException {

        Operator operator = new Operator(operatorCustomizer -> operatorCustomizer
                .withKubernetesClient(kubernetesClient)
                .build());
        controllers.forEach(operator::register);
        return operator;
    }
}
