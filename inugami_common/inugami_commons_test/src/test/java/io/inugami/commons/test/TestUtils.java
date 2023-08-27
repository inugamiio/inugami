package io.inugami.commons.test;

import com.fasterxml.jackson.core.type.TypeReference;
import io.inugami.commons.test.dto.UserDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

    public static TypeReference<UserDto> USER_DTO_TYPE = new TypeReference<UserDto>() {
    };

    public static String buildRelativePath(final File file) {
        if (file == null) {
            return null;
        }

        final String[] parts = file.getAbsolutePath().split("inugami_commons_test/");
        return parts[1];
    }
}
