package io.inugami.api.tools.unit.test;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.UncheckedException;
import io.inugami.api.exceptions.Warning;
import io.inugami.api.monitoring.warn.WarnCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.inugami.api.exceptions.Asserts.assertNotNull;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperErrorCode {

    static void assertErrorCode(final String path, final ErrorCode... errorCodes) {
        assertNotNull(errorCodes);
        final List<Map<String, Serializable>> errors = new ArrayList<>();
        for (final ErrorCode code : errorCodes) {
            errors.add(code.toMap());
        }
        UnitTestHelperText.assertTextRelative(errors, path);
    }

    static void assertErrorCodeUnique(final ErrorCode... errorCodes) {
        assertNotNull(errorCodes);
        final Map<String, Boolean> buffer = new LinkedHashMap<>();
        for (final ErrorCode errorCode : errorCodes) {
            final Boolean value = buffer.get(errorCode.getErrorCode());
            if (value == null) {
                buffer.put(errorCode.getErrorCode(), true);
            } else {
                throw new UncheckedException("non unique errror code detected : " + errorCode.getErrorCode());
            }
        }
    }


    static void assertWarningCode(final String path, final Map<String, List<WarnCode>> warnings) {
        assertNotNull(warnings);
        final Map<String, List<Map<String, Serializable>>> result = new LinkedHashMap();

        for (final Map.Entry<String, List<WarnCode>> entry : warnings.entrySet()) {

            List<Map<String, Serializable>> values = result.get(entry.getKey());
            if (values == null) {
                values = new ArrayList<>();
                result.put(entry.getKey(), values);
            }
            for (final WarnCode warnCode : entry.getValue()) {
                values.add(warnCode.toMap());
            }

        }
        UnitTestHelperText.assertTextRelative(result, path);
    }

    static void assertWarningCode(final String path, final Warning... warnings) {
        assertNotNull(warnings);
        final List<Map<String, Serializable>> values = new ArrayList<>();
        for (final Warning code : warnings) {
            values.add(code.toMap());
        }
        UnitTestHelperText.assertTextRelative(values, path);
    }


    static void assertWaringCodeUnique(final Warning... warnings) {
        assertNotNull(warnings);
        final Map<String, Boolean> buffer = new LinkedHashMap<>();
        for (final Warning code : warnings) {
            final Boolean value = buffer.get(code.getWarningCode());
            if (value == null) {
                buffer.put(code.getWarningCode(), true);
            } else {
                throw new UncheckedException("non unique warning code detected : " + code.getWarningCode());
            }
        }
    }
}
