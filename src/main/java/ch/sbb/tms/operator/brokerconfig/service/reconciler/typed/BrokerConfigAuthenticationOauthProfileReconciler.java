package ch.sbb.tms.operator.brokerconfig.service.reconciler.typed;

import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigAuthenticationOauthProfile;
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
public class BrokerConfigAuthenticationOauthProfileReconciler implements Reconciler<BrokerConfigAuthenticationOauthProfile>, Cleaner<BrokerConfigAuthenticationOauthProfile> {
    private final GenericReconcilerService genericReconcilerService;

    @Override
    public UpdateControl<BrokerConfigAuthenticationOauthProfile> reconcile(BrokerConfigAuthenticationOauthProfile authenticationOauthProfile, Context<BrokerConfigAuthenticationOauthProfile> context) {
        return genericReconcilerService.reconcileDefault(authenticationOauthProfile, context);
    }

    @Override
    public DeleteControl cleanup(BrokerConfigAuthenticationOauthProfile authenticationOauthProfile, Context<BrokerConfigAuthenticationOauthProfile> context) {
        return genericReconcilerService.deleteDefault(authenticationOauthProfile, context);
    }
}
