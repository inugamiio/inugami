package io.inugami.framework.api.tools.unit.test;

import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.FatalException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@UtilityClass
public final class UnitTestHelperUtilityClass {
    public static void assertUtilityClass(final Class<?> utilityClass) {
        Asserts.assertNotNull("utility class mustn't be null", utilityClass);

        for (final Method method : utilityClass.getDeclaredMethods()) {
            Asserts.assertTrue(String.format("%s isn't static method!", method.getName()), Modifier.isStatic(method.getModifiers()));
        }

        for (final Constructor<?> constructor : utilityClass.getDeclaredConstructors()) {
            Asserts.assertTrue(String.format("constructor %s isn't private!", constructor.getName()), Modifier.isPrivate(constructor.getModifiers()));
        }
    }

    public static void assertUtilityClassLombok(final Class<?> utilityClass) {
        assertUtilityClass(utilityClass);

        final Constructor<?>[] constructors = utilityClass.getDeclaredConstructors();
        assertThat(constructors.length).isOne();

        for (final Constructor<?> constructor : constructors) {
            try {
                constructor.setAccessible(true);
                constructor.newInstance();
                throw new FatalException("constructor should throws");
            } catch (final FatalException e) {
                throw e;
            } catch (final Throwable e) {
                log.trace(e.getMessage());
            }

        }
    }
}
