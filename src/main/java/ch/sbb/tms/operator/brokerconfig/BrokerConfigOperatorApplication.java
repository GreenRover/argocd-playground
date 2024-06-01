package ch.sbb.tms.operator.brokerconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan(basePackages = "ch.sbb")
@EnableScheduling
@SpringBootApplication
public class BrokerConfigOperatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrokerConfigOperatorApplication.class, args);
	}

}
