package ch.sbb.tms.operator.brokerconfig.service.reconciler.typed;

import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigClientUsername;
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
public class BrokerConfigClientUsernameReconciler implements Reconciler<BrokerConfigClientUsername>, Cleaner<BrokerConfigClientUsername> {
    private final GenericReconcilerService genericReconcilerService;

    @Override
    public UpdateControl<BrokerConfigClientUsername> reconcile(BrokerConfigClientUsername clientUsername, Context<BrokerConfigClientUsername> context) {

        return genericReconcilerService.reconcileDefault(clientUsername, context);
    }

    @Override
    public DeleteControl cleanup(BrokerConfigClientUsername clientUsername, Context<BrokerConfigClientUsername> context) {
        return genericReconcilerService.deleteDefault(clientUsername, context);
    }
}
