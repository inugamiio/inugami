package io.inugami.interfaces.monitoring.logger;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
public final class BasicLogEvent {
    private final String                    loggerName;
    private final String                    message;
    private final String                    level;
    private final Map<String, Serializable> mdc;
}
