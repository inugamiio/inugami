package io.inugami.api.exceptions.asserts;

import io.inugami.interfaces.exceptions.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertCommons {
    public static final AssertCommons INSTANCE = new AssertCommons();

    public void throwException(final String message) {
        throw new UncheckedException(DefaultErrorCode.buildUndefineError(), message);
    }

    public void throwException(final ErrorCode errorCode) {
        throw new UncheckedException(errorCode,
                                     errorCode == null ? null : errorCode.getMessage());
    }

    public void throwException(final ErrorCode errorCode,
                               final String message,
                               final Serializable... args) {
        throw new UncheckedException(errorCode,
                                     MessagesFormatter.format(message, args));
    }

    public void throwException(final List<ErrorCode> errors) {
        throw new MultiUncheckedException(errors);
    }
}
