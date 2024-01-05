package io.inugami.framework.api.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtils {

    public static Throwable searchCause(int deep, final Throwable error) {
        if (error == null || deep <= 0) {
            return null;
        }

        Throwable cursor = null;
        for (int i = deep; i >= 0; i--) {
            if (cursor == null) {
                cursor = error.getCause();
            } else if (cursor.getCause() != null) {
                cursor = cursor.getCause();
            }
        }
        
        return cursor;
    }
}
