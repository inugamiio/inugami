package io.inugami.framework.api.metrics;

import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.interfaces.metrics.DoubleNumberObject;
import io.inugami.framework.interfaces.metrics.LongNumberObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.inugami.framework.api.metrics.MetricsUtils.*;
import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class MetricsUtilsTest {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final LongNumberObject   LONG_NUMBER   = () -> 99L;
    public static final DoubleNumberObject DOUBLE_NUMBER = () -> 99.9;
    public static final List<Object>       VALUES        = List.of(1,
                                                                   1L,
                                                                   2.15,
                                                                   Integer.MAX_VALUE,
                                                                   Long.MAX_VALUE,
                                                                   BigDecimal.valueOf(10.5),
                                                                   LONG_NUMBER,
                                                                   DOUBLE_NUMBER,
                                                                   "Hello",
                                                                   (short) 100);

    //==================================================================================================================
    // DOUBLE
    //==================================================================================================================
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(MetricsUtils.class);
    }


    @Test
    void convertToLong_nominal() {
        assertText(convertToLong(VALUES),
                   """
                           [ 1, 1, 2, 2147483647, 9223372036854775807, 10, 99, 100 ]
                           """);
    }

    @Test
    void convertToDouble_nominal() {
        assertText(convertToDouble(VALUES),
                   """
                          [ 1.0, 1.0, 2.15, 2.147483647E9, 9.223372036854776E18, 10.5, 99.9, 100.0 ]
                           """);
    }

    @Test
    void isDouble_nominal() {
        assertThat(isDouble(null)).isFalse();
        assertThat(isDouble("other")).isFalse();
        assertThat(isDouble(15)).isFalse();
        assertThat(isDouble(List.of())).isFalse();

        assertThat(isDouble(10.5)).isTrue();
        assertThat(isDouble(10.5f)).isTrue();
        assertThat(isDouble(DOUBLE_NUMBER)).isTrue();
        assertThat(isDouble(BigDecimal.valueOf(45.2))).isTrue();
    }
}