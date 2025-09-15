package io.inugami.commons.test;

import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.exceptions.Warning;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotNull;


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
