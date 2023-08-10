package io.inugami.api.loggers.mdc.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings({"java:S1181", "java:S108"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MdcMapperUtils {

    public static int convertToInt(final String value) {
        int result = 0;
        if (value != null) {
            try {
                result = Integer.parseInt(value);
            } catch (Throwable e) {
            }
        }
        return result;
    }

    public static long convertToLong(final String value) {
        long result = 0;
        if (value != null) {
            try {
                result = Long.parseLong(value);
            } catch (Throwable e) {
            }
        }
        return result;
    }

    public static double convertToDouble(final String value) {
        double result = 0.0;
        if (value != null) {
            try {
                result = Double.parseDouble(value);
            } catch (Throwable e) {
            }
        }
        return result;
    }

    public static boolean convertToBoolean(final String value) {
        return Boolean.parseBoolean(value);
    }

}
