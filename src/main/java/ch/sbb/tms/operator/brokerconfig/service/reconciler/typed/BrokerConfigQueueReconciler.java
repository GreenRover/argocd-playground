package ch.sbb.tms.operator.brokerconfig.service.reconciler.typed;

import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigQueue;
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
public class BrokerConfigQueueReconciler implements Reconciler<BrokerConfigQueue>, Cleaner<BrokerConfigQueue> {
    private final GenericReconcilerService genericReconcilerService;

    @Override
    public UpdateControl<BrokerConfigQueue> reconcile(BrokerConfigQueue queue, Context<BrokerConfigQueue> context) {
        return genericReconcilerService.reconcileDefault(queue, context);
    }

    @Override
    public DeleteControl cleanup(BrokerConfigQueue queue, Context<BrokerConfigQueue> context) {
        return genericReconcilerService.deleteDefault(queue, context);
    }
}
