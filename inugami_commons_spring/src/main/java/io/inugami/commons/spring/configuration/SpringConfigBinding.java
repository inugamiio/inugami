package io.inugami.commons.spring.configuration;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
@Getter
public class SpringConfigBinding {
    private final String springKey;
    private final String inugamiKey;
    private final String defaultValue;

    static SpringConfigBinding fromKey(final String key) {
        return new SpringConfigBinding(key, key, null);
    }

    static SpringConfigBinding fromKey(final String key, final String defaultValue) {
        return new SpringConfigBinding(key, key, defaultValue);
    }
}
