package io.inugami.commons.test;

import io.inugami.commons.marshaling.YamlMarshaller;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperYaml {

    static <T> T convertFromYaml(final String content, final Class<? extends T> userDtoClass) {
        return YamlMarshaller.getInstance().convertFromYaml(content, userDtoClass);
    }
}
