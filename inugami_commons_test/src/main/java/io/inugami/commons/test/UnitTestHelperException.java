package io.inugami.commons.test;

import io.inugami.api.exceptions.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperException {

    static void throwException(final Exception error) {
        throw new RuntimeException(error.getMessage(), error);
    }


    static void assertThrows(final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new AssertThrowsException("method must throw exception", DefaultErrorCode.buildUndefineError());
        } catch (final Throwable error) {
            if (error instanceof AssertThrowsException) {
                throw (AssertThrowsException) error;
            }
        }
    }

    static void assertThrows(final String errorMessage,
                             final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new AssertThrowsException("method must throw exception", DefaultErrorCode.buildUndefineError());
        } catch (final Throwable error) {
            if (error instanceof AssertThrowsException) {
                throw (AssertThrowsException) error;
            }

            if (errorMessage == null) {
                Asserts.assertNull("the exception shouldn't have any message", error.getMessage());
            } else {
                Asserts.assertTrue(String.format("current : \"%s\"  ref: \"%s\"", error.getMessage(), errorMessage),
                                   errorMessage.equals(error.getMessage()));
            }

        }
    }


    static void assertThrows(final Class<? extends Exception> errorClass,
                             final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new AssertThrowsException("method must throw exception", DefaultErrorCode.buildUndefineError());
        } catch (final Throwable error) {
            if (error instanceof AssertThrowsException) {
                throw (AssertThrowsException) error;
            }
            Asserts.assertNotNull(errorClass, "error class is mandatory");
            Asserts.assertTrue("error class isn't a " + errorClass.getName(), error.getClass().isAssignableFrom(errorClass));
        }
    }

    static void assertThrows(final Class<? extends Exception> errorClass, final String errorMessage,
                             final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new AssertThrowsException("method must throw exception", DefaultErrorCode.buildUndefineError());
        } catch (final Throwable error) {
            if (error instanceof AssertThrowsException) {
                throw (AssertThrowsException) error;
            }
            Asserts.assertNotNull(errorClass, "error class is mandatory");
            Asserts.assertTrue("error class isn't a " + errorClass.getName(), error.getClass().isAssignableFrom(errorClass));
            Asserts.assertTrue(String.format("current : \"%s\"  ref: \"%s\"", error.getMessage(), errorMessage),
                               error.getMessage()
                                    .equals(errorMessage));
        }
    }

    static void assertThrows(final ErrorCode errorCode, final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new AssertThrowsException(DefaultErrorCode.buildUndefineError(), "method must throw exception");
        } catch (final Throwable error) {
            if (error instanceof AssertThrowsException) {
                throw (AssertThrowsException) error;
            }
            Asserts.assertTrue("error class isn't a  ExceptionWithErrorCode", error instanceof ExceptionWithErrorCode);

            final String currentErrorCode = ((ExceptionWithErrorCode) error).getErrorCode()
                                                                            .getErrorCode();

            Asserts.assertTrue(String.format("current : \"%s\"  ref: \"%s\"", currentErrorCode, errorCode.getErrorCode()),
                               currentErrorCode.equals(errorCode.getErrorCode()));
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
