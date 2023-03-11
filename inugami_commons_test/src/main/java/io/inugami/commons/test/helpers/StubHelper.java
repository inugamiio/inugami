package io.inugami.commons.test.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import io.inugami.commons.test.UnitTestHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mockito.invocation.InvocationOnMock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StubHelper {
    public static <T, E, U extends Object> List<T> loadListObjectStrubByUid(final InvocationOnMock invocationOnMock,
                                                                            final String basePath,
                                                                            final String objectType,
                                                                            final TypeReference<T> refObjectType) {
        return loadListObjectStrubByUid(invocationOnMock, (value) -> value, basePath, objectType, refObjectType);
    }

    public static <T, E, U extends Object> List<T> loadListObjectStrubByUid(final InvocationOnMock invocationOnMock,
                                                                            final Function<E, U> uidExtractor,
                                                                            final String basePath,
                                                                            final String objectType,
                                                                            final TypeReference<T> refObjectType) {
        final List<T> result        = new ArrayList<>();
        final Object  rawListValues = invocationOnMock.getArguments()[0];
        if (rawListValues != null && rawListValues instanceof Iterable) {
            final Iterable<E> values = (Iterable<E>) rawListValues;
            for (final E element : values) {
                T elementResult = null;
                if (element != null) {
                    final U uidObject = uidExtractor.apply(element);
                    elementResult = loadStub(basePath, objectType, refObjectType, cleanArgument(uidObject));
                }

                if (elementResult != null) {
                    result.add(elementResult);
                }
            }
        }

        return result.isEmpty() ? null : result;
    }


    public static <T> T loadObjectStrubByUid(final InvocationOnMock invocationOnMock,
                                             final String basePath,
                                             final String objectType,
                                             final TypeReference<T> refObjectType) {
        return loadObjectStrubByUid(invocationOnMock,
                                    (c) -> cleanArgument(c.getArguments()[0]),
                                    basePath,
                                    objectType,
                                    refObjectType);
    }

    public static <T> T loadObjectStrubByUid(final InvocationOnMock invocationOnMock,
                                             final Function<InvocationOnMock, Object> uidExtractor,
                                             final String basePath,
                                             final String objectType,
                                             final TypeReference<T> refObjectType) {
        T            result    = null;
        final Object uidObject = uidExtractor.apply(invocationOnMock);
        result = loadStub(basePath, objectType, refObjectType, uidObject);

        return result;
    }

    private static <T> T loadStub(final String basePath, final String objectType, final TypeReference<T> refObjectType,
                                  final Object uidObject) {
        T result = null;
        if (uidObject != null) {
            final StringBuilder path = new StringBuilder(basePath).append(File.separator)
                                                                  .append(objectType)
                                                                  .append(".")
                                                                  .append(uidObject)
                                                                  .append(".json");
            result = UnitTestHelper.loadJson(path.toString(), refObjectType);
        }
        return result;
    }
    private static Object cleanArgument(final Object argument) {
        Object result = null;
        if (argument != null && argument instanceof String) {
            final Matcher matcher = CommonsHelper.UID_PATTERN.matcher((String) argument);
            if (matcher.matches()) {
                result = matcher.group("uid");
            }
        } else {
            result = argument;
        }
        return result;
    }
}
