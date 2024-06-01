package ch.sbb.tms.operator.brokerconfig.service.reconciler.typed;

import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigJndiConnectionFactory;
import ch.sbb.tms.operator.brokerconfig.service.reconciler.GenericReconcilerService;
import io.javaoperatorsdk.operator.api.reconciler.Cleaner;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.DeleteControl;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerConfiguration
@RequiredArgsConstructor
public class BrokerConfigJndiConnectionFactoryReconciler implements Reconciler<BrokerConfigJndiConnectionFactory>, Cleaner<BrokerConfigJndiConnectionFactory> {
    private final GenericReconcilerService genericReconcilerService;

    @Override
    public UpdateControl<BrokerConfigJndiConnectionFactory> reconcile(BrokerConfigJndiConnectionFactory jndiConnectionFactory, Context<BrokerConfigJndiConnectionFactory> context) {
        return genericReconcilerService.reconcileDefault(jndiConnectionFactory, context);
    }

    @Override
    public DeleteControl cleanup(BrokerConfigJndiConnectionFactory jndiConnectionFactory, Context<BrokerConfigJndiConnectionFactory> context) {
        return genericReconcilerService.deleteDefault(jndiConnectionFactory, context);
    }
}
