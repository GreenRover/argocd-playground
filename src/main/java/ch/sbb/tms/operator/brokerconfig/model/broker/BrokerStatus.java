package ch.sbb.tms.operator.brokerconfig.model.broker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BrokerStatus {
    private BrokerStatusEnum status;
    private String statusMsg;
}
