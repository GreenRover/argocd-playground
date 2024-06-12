package ch.sbb.tms.operator.brokerconfig.model.broker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import community.solace.broker.deployment.engine.brokerfacade.BrokerConfig;
import io.fabric8.generator.annotation.Required;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BrokerSpec {
    @Required
    @Pattern(regexp = "\\w{2,20}/\\w{2,20}/\\w{2,20}/[dtip](-\\w{3,12})?")
    private String name;

    @Required
    private TmsSspAuth auth;

    private BrokerConfig config;

    @JsonPropertyDescription("Specify this option to create a solace cloud broker if not exist. Already existing broker will not be modified.")
    private SolaceCloudBrokerSpec brokerTemplate;
}
