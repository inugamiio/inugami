package io.inugami.framework.interfaces.exceptions.asserts;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ExceptionWithErrorCode;
import io.inugami.framework.interfaces.functionnals.BiConsumerWithException;
import io.inugami.framework.interfaces.functionnals.VoidFunctionWithException;
import io.inugami.framework.interfaces.tools.ListUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
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

                    if (e instanceof ExceptionWithErrorCode exceptionWithErrorCode) {
                        result.add(exceptionWithErrorCode.getErrorCode());
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

    public <T> void assertModel(final Collection<T> models,
                                final BiConsumerWithException<T, Integer>... validations) {
        final Map<Integer, List<ErrorCode>> errors = Optional.ofNullable(checkModel(models, validations))
                                                             .orElse(Map.of());

        final List<ErrorCode>          errorCodes = new ArrayList<>();
        final List<Integer> keys       = new ArrayList<>(errors.keySet());
        Collections.sort(keys);

        for(Integer key : keys){
            errorCodes.addAll(errors.get(key));
        }
        if(!errorCodes.isEmpty()){
            AssertCommons.INSTANCE.throwException(errorCodes);
        }
    }
    public <T> Map<Integer,List<ErrorCode>> checkModel(final Collection<T> models,
                                                       final BiConsumerWithException<T, Integer>... validations) {
        if (ListUtils.isEmpty(models)) {
            return new LinkedHashMap<>();
        }
        final List<T>         values = new ArrayList<>(models);

        final Map<Integer,List<ErrorCode>> result = new LinkedHashMap<>();

        for (int index = 0; index < models.size(); index++) {
            final var model = values.get(index);
            for (BiConsumerWithException<T, Integer> validation : validations) {
                try {
                    validation.process(model, index);
                } catch (Throwable e) {
                    if (log.isDebugEnabled()) {
                        log.error(e.getMessage(), e);
                    }
                    List<ErrorCode> bucket = result.get(index);
                    if(bucket==null){
                        bucket =  new ArrayList<>();
                        result.put(index, bucket);
                    }

                    if (e instanceof ExceptionWithErrorCode exceptionWithErrorCode) {
                        bucket.add(exceptionWithErrorCode.getErrorCode());
                    } else {
                        bucket.add(DefaultErrorCode.buildUndefineError());
                    }
                }
            }
        }
        return result;
    }

}
