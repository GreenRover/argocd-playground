package ch.sbb.tms.operator.brokerconfig.model.brokerconfig;


import ch.sbb.tms.operator.brokerconfig.model.broker.BrokerStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.fabric8.generator.annotation.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrokerConfigStatus {
    private BrokerStatusEnum status;
    private String statusMsg;
}
