package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.ExceptionWithErrorCode;
import io.inugami.api.functionnals.VoidFunctionWithException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertModel {

    public static final AssertModel INSTANCE = new AssertModel();


    public List<ErrorCode> checkModel(final VoidFunctionWithException... assertions) {
        return checkModel(Arrays.asList(assertions));
    }

    public List<ErrorCode> checkModel(final List<VoidFunctionWithException> assertions) {
        final List<ErrorCode> result = new ArrayList<>();
        if (assertions != null) {
            for (final VoidFunctionWithException function : assertions) {
                try {
                    function.process();
                } catch (final Exception e) {

                    if (e instanceof ExceptionWithErrorCode) {
                        result.add(((ExceptionWithErrorCode) e).getErrorCode());
                    } else {
                        result.add(DefaultErrorCode.fromErrorCode(DefaultErrorCode.buildUndefineError())
                                                   .message(e.getMessage())
                                                   .build());
                    }
                }
            }
        }
        return result;
    }


    public void assertModel(final VoidFunctionWithException... assertions) {
        assertModel(Arrays.asList(assertions));
    }

    public void assertModel(final List<VoidFunctionWithException> assertions) {
        final List<ErrorCode> errors = checkModel(assertions);
        if (!errors.isEmpty()) {
            AssertCommons.INSTANCE.throwException(errors);
        }
    }

}
