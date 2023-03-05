package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.MessagesFormatter;
import io.inugami.api.exceptions.UncheckedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class  AssertCommons {
    public static void throwException(final String message) {
        throwException(null,
                       message);
    }

    public static void throwException(final ErrorCode errorCode) {
        throw new UncheckedException(errorCode,
                                     errorCode == null ? null : errorCode.getMessage());
    }

    public static void throwException(final ErrorCode errorCode,
                                      final String message,
                                      final Serializable... args) {
        throw new UncheckedException(errorCode,
                                     MessagesFormatter.format(message, args));
    }

}
