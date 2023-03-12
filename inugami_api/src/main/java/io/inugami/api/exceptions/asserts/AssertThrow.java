package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.ExceptionWithErrorCode;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.MessagesFormatter;
import io.inugami.api.functionnals.VoidFunctionWithException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertThrow {
    private static final String THE_CURRENT_METHOD_SHOULD_THROWS_AN_EXCEPTION  = "the current method should throws an exception";
    private static final String METHOD_SHOULD_THROWS_EXCEPTION_WITH_MESSAGE    = "the current method should throws an exception with message \"{0}\"";
    private static final String METHOD_SHOULD_THROWS_EXCEPTION_WITH_ERROR_CODE = "the current method should throws an exception with error code ";
    private static final String METHOD_SHOULD_THROWS_EXCEPTION_WITH_TYPE       = "the method should throws an exception with type";

    public static void assertThrow(final VoidFunctionWithException function) {
        try {
            function.process();
            throw new MethodShouldThrowException(THE_CURRENT_METHOD_SHOULD_THROWS_AN_EXCEPTION);
        } catch (MethodShouldThrowException error) {
            throw error;
        } catch (Exception error) {
        }
    }


    public static void assertThrow(final String errorMessage,
                                   final String message,
                                   final VoidFunctionWithException function) {

        Pattern pattern = Pattern.compile(errorMessage, Pattern.CASE_INSENSITIVE);
        assertThrow(pattern, message, function);
    }

    public static void assertThrow(final Pattern errorMessage, final String message, final VoidFunctionWithException function) {
        try {
            function.process();
            throw new MethodShouldThrowException(message == null ? MessagesFormatter.format(METHOD_SHOULD_THROWS_EXCEPTION_WITH_MESSAGE, errorMessage) : message);
        } catch (MethodShouldThrowException error) {
            throw error;
        } catch (Throwable error) {
            if (notMatch(errorMessage, error.getMessage() == null ? "null" : error.getMessage())) {
                AssertCommons.throwException(message == null ?
                                                     MessagesFormatter.format(METHOD_SHOULD_THROWS_EXCEPTION_WITH_MESSAGE, errorMessage) + " and not :" + error.getMessage()
                                                     : message);
            }
        }
    }

    private static boolean notMatch(final Pattern regex, final String message) {
        return message == null ? true : !regex.matcher(message).matches();
    }

    public static void assertThrow(final ErrorCode errorCode,
                                   final String message,
                                   final VoidFunctionWithException function) {
        try {
            function.process();
            throw new MethodShouldThrowException(message == null ? METHOD_SHOULD_THROWS_EXCEPTION_WITH_ERROR_CODE + errorCode : message);
        } catch (MethodShouldThrowException error) {
            throw error;
        } catch (Throwable error) {
            ErrorCode currentErrorCode = null;
            if (error instanceof ExceptionWithErrorCode) {
                currentErrorCode = ((ExceptionWithErrorCode) error).getErrorCode();
            }

            if (currentErrorCode == null || !errorCode.getErrorCode().equals(currentErrorCode.getErrorCode())) {
                AssertCommons.throwException(message == null ? METHOD_SHOULD_THROWS_EXCEPTION_WITH_ERROR_CODE + errorCode : message);
            }
        }
    }


    public static void assertThrow(final Class<? extends Throwable> errorClass,
                                   final String message,
                                   final VoidFunctionWithException function) {
        try {
            function.process();
            throw new MethodShouldThrowException(message == null ? METHOD_SHOULD_THROWS_EXCEPTION_WITH_TYPE + errorClass.getName() : message);
        } catch (MethodShouldThrowException error) {
            throw error;
        } catch (Throwable error) {

            if (error.getClass().isAssignableFrom(errorClass)) {
                AssertCommons.throwException(message == null ? METHOD_SHOULD_THROWS_EXCEPTION_WITH_TYPE + errorClass.getName() : message);
            }
        }
    }


    private static final class MethodShouldThrowException extends FatalException {
        public MethodShouldThrowException() {
        }

        public MethodShouldThrowException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public MethodShouldThrowException(final String message) {
            super(message);
        }

        public MethodShouldThrowException(final Throwable cause) {
            super(cause);
        }

        public MethodShouldThrowException(final String message, final Object... values) {
            super(message, values);
        }

        public MethodShouldThrowException(final Throwable cause, final String message, final Object... values) {
            super(cause, message, values);
        }

        public MethodShouldThrowException(final ErrorCode errorCode, final Throwable cause, final String message, final Object... values) {
            super(errorCode, cause, message, values);
        }

        public MethodShouldThrowException(final ErrorCode errorCode) {
            super(errorCode);
        }
    }
}
