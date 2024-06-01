package ch.sbb.tms.operator.brokerconfig.model.broker;

import ch.sbb.tms.operator.brokerconfig.model.brokerconfig.BrokerConfigType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerDifference {
    private BrokerDifferenceType differenceType;
    private BrokerConfigType configType;
    private String name;

}
