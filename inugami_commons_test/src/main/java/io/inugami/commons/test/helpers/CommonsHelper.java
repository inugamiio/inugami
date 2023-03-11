package io.inugami.commons.test.helpers;

import io.inugami.commons.test.DefaultLineAssertion;
import io.inugami.commons.test.LineAssertion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonsHelper {

    public final static Pattern UID_PATTERN = Pattern.compile("(?:[^_-]+[_-]){0,1}(?<uid>[0-9]+)");
    public static final Charset UTF_8       = StandardCharsets.UTF_8;
    public static final String  NULL        = "null";


    public static void throwException(final Exception error) {
        throw new RuntimeException(error.getMessage(), error);
    }


}
