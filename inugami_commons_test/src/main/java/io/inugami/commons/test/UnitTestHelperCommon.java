package io.inugami.commons.test;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings({"java:S5361"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperCommon {
    public static final  String UTF_8        = "UTF-8";
    public static final  String NULL         = "null";
    public static final  String EMPTY        = "";
    private static final String WINDOWS_LINE = "\r";

    static String cleanWindowsChar(final String value) {
        return value == null ? value : value.replaceAll(WINDOWS_LINE, EMPTY);
    }
}
