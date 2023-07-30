package io.inugami.api.tools;

import io.inugami.api.spi.SpiPriority;
import io.inugami.api.tools.unit.test.UnitTestHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static io.inugami.api.tools.ReflectionUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"java:S5845"})
class ReflectionUtilsTest {


    // =================================================================================================================
    // getAnnotation
    // =================================================================================================================
    @Test
    void getAnnotation_nominal() {
        final SpiPriority result = getAnnotation(SomeService.class, SpiPriority.class);
        assertThat(result).isNotNull().isNotEqualTo(SpiPriority.class);
    }

    // =================================================================================================================
    // CLASS
    // =================================================================================================================
    @Test
    void scan_nominal() {
        UnitTestHelper.assertTextRelative(scan("io.inugami.api.tools", objClass -> objClass.getSimpleName().endsWith("Test")),
                                          "api/tools/reflectionUtilsTest/scan_nominal.json");

        UnitTestHelper.assertTextRelative(scan("io.inugami.api.tools"),
                                          "api/tools/reflectionUtilsTest/scan_withoutFilter.json");

    }

    // =================================================================================================================
    // setFieldValue
    // =================================================================================================================
    @Test
    void setFieldValue_nominal() {
        final Field       field    = getField("name", SomeService.class);
        final SomeService instance = new SomeService();
        final String      result   = setFieldValue(field, "hello", instance).getName();
        assertThat(result).isEqualTo("hello");
    }


    // =========================================================================
    // ASSERTS ENUM
    // =========================================================================
    @Test
    void assertEnum_nominal() {
        assertThat(convertEnumToMap(Levels.class)).hasToString("{ADMIN={label=admin, level=10}, GUEST={label=guest, level=0}}");
    }

    @Test
    void assertEnum_nullValue() {
        assertThat(convertEnumToMap(null)).isNull();
    }


    @RequiredArgsConstructor
    public enum Levels {
        ADMIN("admin", 10),
        GUEST("guest", 0);

        private final String label;
        private final int    level;
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    @Getter
    @SpiPriority(1)
    static class SomeService {
        private String name;
    }
}