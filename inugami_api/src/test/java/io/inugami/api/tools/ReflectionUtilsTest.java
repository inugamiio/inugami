package io.inugami.api.tools;

import io.inugami.api.spi.SpiPriority;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static io.inugami.api.tools.ReflectionUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

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
    // setFieldValue
    // =================================================================================================================
    @Test
    void setFieldValue_nominal() {
        final Field       field    = getField("name", SomeService.class);
        final SomeService instance = new SomeService();
        final String      result   = setFieldValue(field, "hello", instance).getName();
        assertThat(result).isEqualTo("hello");
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