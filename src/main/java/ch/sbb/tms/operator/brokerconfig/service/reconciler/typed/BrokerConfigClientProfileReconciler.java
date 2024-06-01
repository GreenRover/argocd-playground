package ch.sbb.tms.operator.brokerconfig.service.reconciler.typed;

import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigClientProfile;
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
public class BrokerConfigClientProfileReconciler implements Reconciler<BrokerConfigClientProfile>, Cleaner<BrokerConfigClientProfile> {
    private final GenericReconcilerService genericReconcilerService;

    @Override
    public UpdateControl<BrokerConfigClientProfile> reconcile(BrokerConfigClientProfile clientProfile, Context<BrokerConfigClientProfile> context) {
        return genericReconcilerService.reconcileDefault(clientProfile, context);
    }

    @Override
    public DeleteControl cleanup(BrokerConfigClientProfile clientProfile, Context<BrokerConfigClientProfile> context) {
        return genericReconcilerService.deleteDefault(clientProfile, context);
    }
}
