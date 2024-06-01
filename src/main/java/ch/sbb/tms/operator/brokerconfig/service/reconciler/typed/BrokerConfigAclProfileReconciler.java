package ch.sbb.tms.operator.brokerconfig.service.reconciler.typed;

import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigAclProfile;
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
public class BrokerConfigAclProfileReconciler implements Reconciler<BrokerConfigAclProfile>, Cleaner<BrokerConfigAclProfile> {
    private final GenericReconcilerService genericReconcilerService;

    @Override
    public UpdateControl<BrokerConfigAclProfile> reconcile(BrokerConfigAclProfile aclProfile, Context<BrokerConfigAclProfile> context) {
        return genericReconcilerService.reconcileDefault(aclProfile, context);
    }

    @Override
    public DeleteControl cleanup(BrokerConfigAclProfile aclProfile, Context<BrokerConfigAclProfile> context) {
        return genericReconcilerService.deleteDefault(aclProfile, context);
    }
}
