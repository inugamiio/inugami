package io.inugami.framework.api.exceptions;

import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.CheckedException;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChecksTest {

    private static final ErrorCode                                       TEST_ERROR     =
            DefaultErrorCode.buildUndefineError();
    private static final String                                          MSG            = "custom message";
    private static final BiFunction<ErrorCode, String, CheckedException> CUSTOM_BUILDER = CheckedException::new;

    @Test
    void should_respect_utility_class_rules() {
        UnitTestHelper.assertUtilityClassLombok(Checks.class);
    }

    @Test
    void test_isTrue_logic() throws CheckedException {
        // Success
        Checks.isTrue(true);
        Checks.isTrue(MSG, true);
        Checks.isTrue(TEST_ERROR, true);
        Checks.isTrue(true, CUSTOM_BUILDER);

        // Failures
        assertThatThrownBy(() -> Checks.isTrue(false))
                .isInstanceOf(CheckedException.class)
                .hasMessageContaining(Checks.THIS_EXPRESSION_MUST_BE_TRUE);

        assertThatThrownBy(() -> Checks.isTrue(MSG, false))
                .hasMessage(MSG);

        assertThatThrownBy(() -> Checks.isTrue(TEST_ERROR, false))
                .isInstanceOf(CheckedException.class);
    }

    @Test
    void test_isFalse_logic() throws CheckedException {
        // Success
        Checks.isFalse(false);
        Checks.isFalse(MSG, false);
        Checks.isFalse(false, CUSTOM_BUILDER);

        // Failures
        assertThatThrownBy(() -> Checks.isFalse(true))
                .isInstanceOf(CheckedException.class);

        assertThatThrownBy(() -> Checks.isFalse(TEST_ERROR, true, CUSTOM_BUILDER))
                .isInstanceOf(CheckedException.class);
    }



    @Test
    void test_notNull_logic() throws CheckedException {
        // Success
        Checks.notNull("value");
        Checks.notNull(MSG, 1, 2, 3);
        Checks.notNull(TEST_ERROR, new Object());

        // Failures
        assertThatThrownBy(() -> Checks.notNull(new Object(), null))
                .isInstanceOf(CheckedException.class);
    }

    @Test
    void test_notEmpty_String() throws CheckedException {
        // Success
        Checks.notEmpty(MSG, "text");
        Checks.notEmpty(TEST_ERROR, "text", CUSTOM_BUILDER);

        // Failures (null, empty, whitespaces)
        assertThat(Checks.checkIsBlank("  ")).isTrue();
        assertThat(Checks.checkIsBlank("")).isTrue();
        assertThat(Checks.checkIsBlank(null)).isTrue();
        assertThat(Checks.checkIsBlank("a")).isFalse();

        assertThatThrownBy(() -> Checks.notEmpty(MSG, "  "))
                .isInstanceOf(CheckedException.class);
    }

    @Test
    void test_notEmpty_Collections_and_Maps() throws CheckedException {
        List<String>        list = List.of("item");
        Map<String, String> map  = Map.of("k", "v");

        // Success
        Checks.notEmpty(MSG, list);
        Checks.notEmpty(MSG, map);
        Checks.notEmpty(TEST_ERROR, list, CUSTOM_BUILDER);

        // Failures
        assertThatThrownBy(() -> Checks.notEmpty(MSG, Collections.emptyList()))
                .isInstanceOf(CheckedException.class);
        assertThatThrownBy(() -> Checks.notEmpty(MSG, (Map<?, ?>) null))
                .isInstanceOf(CheckedException.class);
        assertThatThrownBy(() -> Checks.notEmpty(TEST_ERROR, (Map<?, ?>) null, CUSTOM_BUILDER))
                .isInstanceOf(CheckedException.class);
    }

    @Test
    void test_equalsObj_logic() throws CheckedException {
        String s1 = "test";
        String s2 = new String("test");

        // Success
        Checks.equalsObj(s1, s2);
        Checks.equalsObj(null, null);
        Checks.equalsObj(MSG, 10, 10, CUSTOM_BUILDER);

        // Failures
        assertThatThrownBy(() -> Checks.equalsObj("a", "b"))
                .isInstanceOf(CheckedException.class)
                .hasMessageContaining("objects must be equals!");
    }

    @Test
    void test_throwException_edge_cases() {
        assertThatThrownBy(() -> Checks.isTrue(MSG, false, null))
                .isInstanceOf(UncheckedException.class);
    }
}