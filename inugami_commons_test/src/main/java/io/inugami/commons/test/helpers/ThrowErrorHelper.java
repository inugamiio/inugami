package io.inugami.commons.test.helpers;

import io.inugami.api.exceptions.*;
import io.inugami.commons.test.ExecutableFunction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThrowErrorHelper {
    public static void assertThrowsError(final Class<? extends Exception> errorClass, final String errorMessage,
                                         final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new UncheckedException("method must throw exception", DefaultErrorCode.buildUndefineError());
        } catch (final Throwable error) {
            Asserts.assertNotNull(errorClass, "error class is mandatory");
            Asserts.assertTrue("error class isn't a " + errorClass.getName(), error.getClass()
                                                                                   .isInstance(errorClass));
            Asserts.assertTrue(String.format("current : \"%s\"  ref: \"%s\"", error.getMessage(), errorMessage),
                               error.getMessage()
                                    .equals(errorMessage));
        }
    }

    public static void assertThrowsError(final ErrorCode errorCode, final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new UncheckedException(DefaultErrorCode.buildUndefineError(), "method must throw exception");
        } catch (final Throwable error) {
            Asserts.assertTrue("error class isn't a  ExceptionWithErrorCode", error instanceof ExceptionWithErrorCode);

            final String currentErrorCode = ((ExceptionWithErrorCode) error).getErrorCode()
                                                                            .getErrorCode();

            Asserts.assertTrue(String.format("current : \"%s\"  ref: \"%s\"", currentErrorCode, errorCode.getErrorCode()),
                               currentErrorCode.equals(errorCode.getErrorCode()));
        }
    }
}
