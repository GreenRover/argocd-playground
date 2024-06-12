package ch.sbb.tms.operator.brokerconfig.model.broker;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.fabric8.generator.annotation.Default;
import io.fabric8.generator.annotation.Required;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@JsonClassDescription("Please consult https://developer.sbb.ch/apis/tms_ssp_solace/documentation for full parameter documentation.")
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class SolaceCloudBrokerSpec {
    @Required
    private String cluster;
    @Required
    private String serviceTemplateId;

    @JsonPropertyDescription("Ldap group, containing user that should readonly access granted to the broker.")
    private String readonlyLdapGroup;
    @JsonPropertyDescription("Ldap group, containing user that should admin access granted to the broker.")
    private String adminLdapGroup;
    @JsonPropertyDescription("Messaging storage in GB / the expected amount used for all messages queued at this broker. If a message is in 100 queues it just count once. But a to small storage will affect the performance of the broker negative.")

    @Required
    @Min(3)
    @Max(800)
    private long messagingStorage;
    @JsonPropertyDescription("Messaging storage in GB / the expected amount used for all messages queued at this broker. If a message is in 100 queues it just count once. But a to small storage will affect the performance of the broker negative.")

    @Required
    private String version;

    @Default("false")
    @JsonPropertyDescription("All brokers are accessible via sbb network. Optional you can expose a broker additionally via internet (only SMF via wss, MQTT via ssl and MQTT via wss).")
    private boolean viaInternet;
}
