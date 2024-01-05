package io.inugami.framework.interfaces.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings({"java:S2589"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringComparator {

    public static int compareTo(final String value, final String ref) {
        if (value == null && ref != null) {
            return 1;
        } else if (value != null && ref == null) {
            return -1;
        } else if (value == null && ref == null) {
            return 0;
        } else {
            return convertCompareToResult(value.compareTo(ref));
        }

    }

    private static int convertCompareToResult(final int result) {
        if (result == 0) {
            return 0;
        } else if (result < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
