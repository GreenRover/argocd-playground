package ch.sbb.tms.operator.brokerconfig.model.broker;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

@Data
@JsonClassDescription("You have to provide one of both possible options to auth against tms-ssp to provide you have admin access on the target application context")
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class TmsSspAuth {
    @JsonPropertyDescription("Use a k8n secret containing client credentials to get an access token for tms-ssp")
    private String secretName;
    @JsonPropertyDescription("Use secret of k8n serviceAccount to authenticate against tms-ssp")
    private String serviceAccount;
}
