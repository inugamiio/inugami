package io.inugami.commons.test;


import io.inugami.framework.interfaces.exceptions.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings({"java:S2166", "java:S112"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperException {

    public static final String METHOD_MUST_THROW_EXCEPTION = "method must throw exception";
    public static final String ERROR_DIFF                  = "current : \"%s\"  ref: \"%s\"";

    static void throwException(final Exception error) {
        throw new RuntimeException(error.getMessage(), error);
    }


    static void assertThrows(final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new AssertThrowsException(
                    METHOD_MUST_THROW_EXCEPTION, DefaultErrorCode.buildUndefineError());
        } catch (final AssertThrowsException error) {
            throw error;
        } catch (final Throwable error) {
        }
    }

    static void assertThrows(final String errorMessage,
                             final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new AssertThrowsException(METHOD_MUST_THROW_EXCEPTION, DefaultErrorCode.buildUndefineError());
        } catch (final Throwable error) {
            if (error instanceof AssertThrowsException) {
                throw (AssertThrowsException) error;
            }

            if (errorMessage == null) {
                Asserts.assertNull("the exception shouldn't have any message", error.getMessage());
            } else {
                Asserts.assertTrue(String.format(ERROR_DIFF, error.getMessage(), errorMessage),
                                   errorMessage.equals(error.getMessage()));
            }

        }
    }


    static void assertThrows(final Class<? extends Exception> errorClass,
                             final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new AssertThrowsException(METHOD_MUST_THROW_EXCEPTION, DefaultErrorCode.buildUndefineError());
        } catch (final Throwable error) {
            if (error instanceof AssertThrowsException) {
                throw (AssertThrowsException) error;
            }
            Asserts.assertNotNull(errorClass, "error class is mandatory");
            Asserts.assertTrue(
                    "error class isn't a " + errorClass.getName(), error.getClass().isAssignableFrom(errorClass));
        }
    }

    static void assertThrows(final Class<? extends Exception> errorClass, final String errorMessage,
                             final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new AssertThrowsException(METHOD_MUST_THROW_EXCEPTION, DefaultErrorCode.buildUndefineError());
        } catch (final Throwable error) {
            if (error instanceof AssertThrowsException) {
                throw (AssertThrowsException) error;
            }
            Asserts.assertNotNull(errorClass, "error class is mandatory");
            Asserts.assertTrue(
                    "error class isn't a " + errorClass.getName(), error.getClass().isAssignableFrom(errorClass));
            Asserts.assertTrue(String.format(ERROR_DIFF, error.getMessage(), errorMessage),
                               error.getMessage()
                                    .equals(errorMessage));
        }
    }

    static void assertThrows(final ErrorCode errorCode, final ExecutableFunction handler) {
        assertThrows(errorCode, handler, null);
    }

    static void assertThrows(final ErrorCode errorCode, final ExecutableFunction handler, final String path) {
        try {
            handler.execute();
            throw new AssertThrowsException(DefaultErrorCode.buildUndefineError(), METHOD_MUST_THROW_EXCEPTION);
        } catch (final Throwable error) {
            if (error instanceof AssertThrowsException) {
                throw (AssertThrowsException) error;
            }
            Asserts.assertTrue("error class isn't a  ExceptionWithErrorCode", error instanceof ExceptionWithErrorCode);

            final String currentErrorCode = ((ExceptionWithErrorCode) error).getErrorCode()
                                                                            .getErrorCode();

            Asserts.assertTrue(String.format(ERROR_DIFF, currentErrorCode, errorCode.getErrorCode()),
                               currentErrorCode.equals(errorCode.getErrorCode()));
            if (path != null) {
                UnitTestHelperText.assertTextRelative(((ExceptionWithErrorCode) error).getErrorCode().toMap(), path);
            }
        }
    }


    static class AssertThrowsException extends UncheckedException {
        public AssertThrowsException(final String message, final Object... values) {
            super(message, values);
        }

        public AssertThrowsException(final ErrorCode errorCode, final String message) {
            super(errorCode, message);
        }
    }
}
