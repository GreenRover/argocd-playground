package ch.sbb.tms.operator.brokerconfig.service.reconciler.typed;

import ch.sbb.tms.operator.brokerconfig.model.broker.Broker;
import ch.sbb.tms.operator.brokerconfig.service.DemoDataGeneratorService;
import ch.sbb.tms.operator.brokerconfig.service.OwnerReferencesService;
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
public class BrokerReconciler implements Reconciler<Broker>, Cleaner<Broker> {
    private final GenericReconcilerService genericReconcilerService;
    private final OwnerReferencesService ownerReferencesService;

    @Override
    public UpdateControl<Broker> reconcile(Broker broker, Context<Broker> context) {
        return genericReconcilerService.reconcileDefault(broker, context);
    }

    @Override
    public DeleteControl cleanup(Broker broker, Context<Broker> context) {

        // Maybe there was more than one broker per namespace, and we have to renew ownership of all configs.
        ownerReferencesService.updateOwnerReferences(broker.getMetadata()
                                                           .getNamespace());

        return genericReconcilerService.deleteDefault(broker, context);
    }
}
