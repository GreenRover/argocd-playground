package ch.sbb.tms.operator.brokerconfig.service.reconciler.typed;

import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigAuthorizationGroup;
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
public class BrokerConfigAuthorizationGroupReconciler implements Reconciler<BrokerConfigAuthorizationGroup>, Cleaner<BrokerConfigAuthorizationGroup> {
    private final GenericReconcilerService genericReconcilerService;

    @Override
    public UpdateControl<BrokerConfigAuthorizationGroup> reconcile(BrokerConfigAuthorizationGroup authorizationGroup, Context<BrokerConfigAuthorizationGroup> context) {
        return genericReconcilerService.reconcileDefault(authorizationGroup, context);
    }

    @Override
    public DeleteControl cleanup(BrokerConfigAuthorizationGroup authorizationGroup, Context<BrokerConfigAuthorizationGroup> context) {
        return genericReconcilerService.deleteDefault(authorizationGroup, context);
    }
}
