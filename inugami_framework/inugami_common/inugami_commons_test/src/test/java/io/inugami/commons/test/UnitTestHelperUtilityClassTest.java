package io.inugami.commons.test;

import io.inugami.framework.interfaces.exceptions.UncheckedException;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertThrows;
import static io.inugami.commons.test.UnitTestHelperUtilityClass.assertUtilityClassLombok;

class UnitTestHelperUtilityClassTest {

    @Test
    void assertUtilityClassLombok_nominal() {
        assertUtilityClassLombok(SimpleUtilityClass.class);
    }

    @Test
    void assertUtilityClassLombok_withMethodNonStatic() {
        assertThrows("hello isn't static method!", () -> assertUtilityClassLombok(BadUtilityClass.class));
    }

    @Test
    void assertUtilityClassLombok_withoutConstructorPrivate() {
        assertThrows(UncheckedException.class, () -> assertUtilityClassLombok(WithoutConstructorUtilityClass.class));
    }

    @UtilityClass
    public static class SimpleUtilityClass {
        public static String hello() {
            return "Hello";
        }
    }


    public static class BadUtilityClass {
        public String hello() {
            return "Hello";
        }
    }

    public static class WithoutConstructorUtilityClass {
        public static String hello() {
            return "Hello";
        }
    }
}