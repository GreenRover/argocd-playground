package ch.sbb.tms.operator.brokerconfig.service.reconciler;

import ch.sbb.tms.operator.brokerconfig.service.DemoDataGeneratorService;
import ch.sbb.tms.operator.brokerconfig.service.OwnerReferencesService;
import io.fabric8.kubernetes.client.CustomResource;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.DeleteControl;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class GenericReconcilerService {

    private final OwnerReferencesService ownerReferencesService;
    private final DemoDataGeneratorService demoDataGeneratorService;

    public <SPEC, STATUS, T extends CustomResource<SPEC, STATUS>> UpdateControl<T> reconcileDefault(T resource, Context<T> context) {
        log.info("reconcile {}: {} namespace: {}", resource.getCRDName(), resource.getMetadata()
                                                                                  .getName(), resource.getMetadata()
                                                                                                      .getNamespace());

        demoDataGeneratorService.playDemoDataInThread(resource);

        ownerReferencesService.updateOwnerReferences(resource.getMetadata()
                                                              .getNamespace());

        return UpdateControl.noUpdate();
    }

    public <SPEC, STATUS, T extends CustomResource<SPEC, STATUS>> DeleteControl deleteDefault(T resource, Context<T> context) {
        log.info("deleted resource {}: {} namespace: {}", resource.getCRDName(), resource.getMetadata()
                                                                                         .getName(), resource.getMetadata()
                                                                                                             .getNamespace());

        return DeleteControl.defaultDelete();
    }
}
