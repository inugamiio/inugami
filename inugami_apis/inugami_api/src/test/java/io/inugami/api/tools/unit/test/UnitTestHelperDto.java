package io.inugami.api.tools.unit.test;

import io.inugami.api.tools.ReflectionUtils;
import io.inugami.api.tools.unit.test.dto.AssertDtoContext;
import io.inugami.interfaces.tools.reflection.FieldGetterSetter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.inugami.api.exceptions.Asserts.assertNotNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings({"java:S3878", "java:S1764"})
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperDto {

    // =========================================================================
    // assertDto
    // =========================================================================
    static <T> void assertDto(final AssertDtoContext<T> context) {
        assertNotNull("context is required", context);
        assertNoArgsConstructor(context);
        assertFullArgsConstructor(context);
        assertSerialization(context);
        assertGetters(context);
        assertSetters(context);
        assertToString(context);
        assertEquals(context);
    }

    // =========================================================================
    // ASSERTS
    // =========================================================================
    private static <T> void assertNoArgsConstructor(final AssertDtoContext<T> context) {
        if (context.getNoArgConstructor() != null) {
            log.info("[{}] assertNoArgsConstructor", context);
            assertNotNull("object shouldn't be null", context.getNoArgConstructor().get());
        }
    }

    private static <T> void assertFullArgsConstructor(final AssertDtoContext<T> context) {
        T instance = null;
        if (context.getFullArgConstructor() != null) {
            log.info("[{}] assertFullArgsConstructor", context);
            instance = context.getFullArgConstructor().get();
            assertNotNull("object shouldn't be null", instance);
            UnitTestHelperText.assertTextRelative(instance, context.getFullArgConstructorRefPath());
        }

        if (context.getCloneFunction() != null) {
            assertNotNull("object shouldn't be null", instance);
            final T newInstance = context.getCloneFunction().apply(instance);
            UnitTestHelperText.assertTextRelative(newInstance, context.getFullArgConstructorRefPath());
        }
    }

    private static <T> void assertSerialization(final AssertDtoContext<T> context) {
        if (context.isCheckSerialization() && context.getFullArgConstructorRefPath() != null) {
            log.info("[{}] assertSerialization", context);
            final T instance = UnitTestHelperJson.loadJson(context.getFullArgConstructorRefPath(), context.getObjectClass());
            UnitTestHelperText.assertTextRelative(instance, context.getFullArgConstructorRefPath());
        }
    }

    private static <T> void assertGetters(final AssertDtoContext<T> context) {
        if (context.getGetterRefPath() != null) {
            log.info("[{}] assertGetters", context);
            final T instance = UnitTestHelperJson.loadJson(context.getFullArgConstructorRefPath(), context.getObjectClass());
            assertNotNull("object shouldn't be null", instance);
            final Map<String, Object> gettersValues = extractGettersValues(instance);
            UnitTestHelperText.assertTextRelative(gettersValues, context.getGetterRefPath());
        }

    }

    private static <T> Map<String, Object> extractGettersValues(final T instance) {
        final Map<String, Object> result = new LinkedHashMap<>();

        final List<Method> methods = ReflectionUtils.extractGetters(instance);
        ReflectionUtils.sortMethods(methods);

        for (final Method method : methods) {
            final Object value = ReflectionUtils.invoke(method, instance);
            result.put(method.getName(), value);
        }

        return result;
    }


    private static <T> void assertSetters(final AssertDtoContext<T> context) {
        if (context.isCheckSetters()) {
            log.info("[{}] assertSetters", context);
            final T instance      = UnitTestHelperJson.loadJson(context.getFullArgConstructorRefPath(), context.getObjectClass());
            final T emptyInstance = context.getNoArgConstructor().get();

            assertNotNull("object shouldn't be null", instance);
            final List<FieldGetterSetter> fieldGettersAndSetters = ReflectionUtils.extractFieldGetterAndSetter(instance);


            for (final FieldGetterSetter field : fieldGettersAndSetters) {
                log.info("[{}] assertGetters : {}", context, field.getField().getName());
                ReflectionUtils.invoke(field.getSetter(), emptyInstance, new Object[]{field.getValue()});

                final Object instanceValue      = ReflectionUtils.invoke(field.getGetter(), instance, new Object[]{});
                final Object emptyInstanceValue = ReflectionUtils.invoke(field.getGetter(), emptyInstance, new Object[]{});
                assertThat(instanceValue).isEqualTo(emptyInstanceValue);
            }
        }
    }

    private static <T> void assertToString(final AssertDtoContext<T> context) {
        if (context.getToStringRefPath() != null) {
            log.info("[{}] assertToString", context);
            final T instance = context.getFullArgConstructor().get();
            UnitTestHelperText.assertTextRelative(String.valueOf(instance), context.getToStringRefPath());
        }
    }

    private static <T> void assertEquals(final AssertDtoContext<T> context) {
        if (context.isCheckEquals() && context.getFullArgConstructor() != null) {
            log.info("[{}] assertEquals", context);
            final T instance = context.getFullArgConstructor().get();
            processAssertEquals(instance, context);
            processAssertNotEquals(instance, context);
        }
    }


    private static <T> void processAssertEquals(final T instance, final AssertDtoContext<T> context) {
        assertThat(instance.equals(instance)).isTrue();
        assertThat(instance).isEqualTo(instance);

        if (context.getNoArgConstructor() != null) {
            assertThat(context.getNoArgConstructor().get()).isEqualTo(context.getNoArgConstructor().get());
            assertThat(context.getNoArgConstructor().get()).hasSameHashCodeAs(context.getNoArgConstructor().get());
        }

        if (context.getFullArgConstructor() != null) {
            assertThat(context.getFullArgConstructor().get()).isEqualTo(context.getFullArgConstructor().get());
            assertThat(context.getFullArgConstructor().get()).hasSameHashCodeAs(context.getFullArgConstructor().get());
        }
        if (context.getCloneFunction() != null) {
            assertThat(instance).isEqualTo(context.getCloneFunction().apply(instance));
            assertThat(context.getCloneFunction().apply(instance)).isEqualTo(instance);

            assertThat(instance).hasSameHashCodeAs(context.getCloneFunction().apply(instance));
            assertThat(context.getCloneFunction().apply(instance)).hasSameHashCodeAs(instance);
        }
        if (context.getEqualsFunction() != null) {
            context.getEqualsFunction().accept(instance);
        }
    }

    private static <T> void processAssertNotEquals(final T instance, final AssertDtoContext<T> context) {
        assertThat(instance).isNotEqualTo(null);

        if (context.getNoArgConstructor() != null && context.getFullArgConstructor() != null) {
            assertThat(context.getNoArgConstructor().get()).isNotEqualTo(context.getFullArgConstructor().get());
            assertThat(context.getNoArgConstructor().get().hashCode()).isNotEqualTo(context.getFullArgConstructor()
                                                                                           .get()
                                                                                           .hashCode());

            assertThat(context.getFullArgConstructor().get()).isNotEqualTo(context.getNoArgConstructor().get());
            assertThat(context.getFullArgConstructor().get().hashCode()).isNotEqualTo(context.getNoArgConstructor()
                                                                                             .get()
                                                                                             .hashCode());
        }
        if (context.getNoEqualsFunction() != null) {
            log.info("[{}] processAssertNotEquals", context);
            context.getNoEqualsFunction().accept(instance);
        }
    }

}
