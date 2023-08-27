package io.inugami.api.exceptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiUncheckedException extends UncheckedException {
    private final transient List<ErrorCode> errorCodes;

    public MultiUncheckedException(final Collection<ErrorCode> errorCodes) {
        this.errorCodes = new ArrayList<>();
        if (errorCodes != null) {
            this.errorCodes.addAll(errorCodes);
        }
    }


    @Override
    public ErrorCode getErrorCode() {
        return errorCodes.isEmpty() ? null : errorCodes.get(0);
    }

    public List<ErrorCode> getErrorCodes() {
        return errorCodes;
    }
}
