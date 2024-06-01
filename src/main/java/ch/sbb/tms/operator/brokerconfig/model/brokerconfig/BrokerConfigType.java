package ch.sbb.tms.operator.brokerconfig.model.brokerconfig;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import org.springframework.util.StringUtils;

public enum BrokerConfigType {
    ACL_PROFILE,
    AUTHENTICATION_OAUTH_PROFILE,
    AUTHORIZATION_GROUP,
    CLIENT_PROFILE,
    CLIENT_USERNAME,
    JNDI,
    QUEUE,
    QUEUE_TEMPLATE,
    REPLAY_LOG;
}
