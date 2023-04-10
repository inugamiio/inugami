package io.inugami.monitoring.springboot.activemq.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InugamiActiveMqConstants {
    public static final String DEVICE_IDENTIFIER = "deviceIdentifier";
    public static final String CORRELATION_ID    = "correlationId";
    public static final String CONVERSATION_ID   = "conversationId";
    public static final String TRACE_ID          = "traceId";
    public static final String APPLICATION       = "application";
}
