package ch.sbb.tms.operator.brokerconfig.model.broker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import community.solace.broker.deployment.engine.brokerfacade.BrokerConfig;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BrokerSpec {
    @Pattern(regexp = "\\w{2,20}/\\w{2,20}/\\w{2,20}/[dtip](-\\w{3,12})?")
    private String name;

    private BrokerConfig config;
}
