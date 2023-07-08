package io.inugami.api.functionnals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static io.inugami.api.functionnals.FunctionalUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

class FunctionalUtilsTest {

    public static final String HELLO     = "hello";
    public static final String THE_WORLD = "the world";

    // =================================================================================================================
    // APPLY IF NOT NULL
    // =================================================================================================================
    @Test
    void applyIfNotNull_nominal() {
        final List<String> values = new ArrayList<>();
        assertThat(applyIfNotNull(HELLO, null, values::add)).isTrue();
        assertThat(values.get(0)).isEqualTo(HELLO);
    }

    @Test
    void applyIfNotNull_withNullValue() {
        final List<String> values = new ArrayList<>();
        assertThat(applyIfNotNull(null, HELLO, values::add)).isTrue();
        assertThat(values.get(0)).isEqualTo(HELLO);
    }

    @Test
    void applyIfNotNull_withAnyValue() {
        final List<String> values = new ArrayList<>();
        final String       value  = null;
        assertThat(applyIfNotNull(value, null, values::add)).isFalse();
        assertThat(values).isEmpty();
    }

    // =================================================================================================================
    // APPLY IF CHANGE
    // =================================================================================================================
    @Test
    void applyIfChange_nominal() {
        final List<String> values = new ArrayList<>();
        assertThat(applyIfChange(HELLO, THE_WORLD, values::add)).isTrue();
        assertThat(values.get(0)).isEqualTo(THE_WORLD);
    }

    @Test
    void applyIfChange_nominal_withNullValue() {
        final AtomicReference<String> ref = new AtomicReference<>();
        assertThat(applyIfChange(HELLO, null, ref::set)).isTrue();
        assertThat(applyIfChange(null, HELLO, ref::set)).isTrue();
    }

    @Test
    void applyIfChange_withoutChange() {
        final List<String> values = new ArrayList<>();
        assertThat(applyIfChange(HELLO, HELLO, values::add)).isFalse();
        assertThat(values).isEmpty();
    }

    @Test
    void applyIfChangeAndNotNull_nominal() {
        final AtomicReference<String> ref = new AtomicReference<>();
        assertThat(applyIfChangeAndNotNull(HELLO, THE_WORLD, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo(THE_WORLD);
    }

    @Test
    void applyIfChangeAndNotNull_withoutChange() {
        final AtomicReference<String> ref = new AtomicReference<>();
        assertThat(applyIfChangeAndNotNull(HELLO, HELLO, ref::set)).isFalse();
        assertThat(ref.get()).isNull();
    }

    @Test
    void applyIfChangeAndNotNull_withoutNull() {
        final AtomicReference<String> ref = new AtomicReference<>();
        assertThat(applyIfChangeAndNotNull(HELLO, null, ref::set)).isFalse();
        assertThat(ref.get()).isNull();
    }

    @Test
    void applyIfChange_nominal_boolean() {
        final AtomicReference<Boolean> ref = new AtomicReference<>();
        assertThat(applyIfChange(true, false, ref::set)).isTrue();
        assertThat(ref.get()).isFalse();
        assertThat(applyIfChange(false, true, ref::set)).isTrue();
        assertThat(ref.get()).isTrue();
        assertThat(applyIfChange(true, true, ref::set)).isFalse();
    }

    @Test
    void applyIfChange_nominal_short() {
        final AtomicReference<Short> ref = new AtomicReference<>();
        assertThat(applyIfChange((short) 1, (short) 2, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo((short) 2);
        assertThat(applyIfChange((short) 2, (short) 1, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo((short) 1);
        assertThat(applyIfChange((short) 2, (short) 2, ref::set)).isFalse();
    }


    @Test
    void applyIfChange_nominal_int() {
        final AtomicReference<Integer> ref = new AtomicReference<>();
        assertThat(applyIfChange(1, 2, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo(2);
        assertThat(applyIfChange(2, 1, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo(1);
        assertThat(applyIfChange(2, 2, ref::set)).isFalse();
    }


    @Test
    void applyIfChange_nominal_long() {
        final AtomicReference<Long> ref = new AtomicReference<>();
        assertThat(applyIfChange(1L, 2L, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo(2L);
        assertThat(applyIfChange(2L, 1L, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo(1L);
        assertThat(applyIfChange(2L, 2L, ref::set)).isFalse();
    }

    @Test
    void applyIfChange_nominal_float() {
        final AtomicReference<Float> ref = new AtomicReference<>();
        assertThat(applyIfChange(1.0f, 2.0f, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo(2.0f);
        assertThat(applyIfChange(2.0f, 1.0f, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo(1.0f);
        assertThat(applyIfChange(2.0f, 2.0f, ref::set)).isFalse();
    }

    @Test
    void applyIfChange_nominal_double() {
        final AtomicReference<Double> ref = new AtomicReference<>();
        assertThat(applyIfChange(1.0, 2.0, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo(2.0);
        assertThat(applyIfChange(2.0, 1.0, ref::set)).isTrue();
        assertThat(ref.get()).isEqualTo(1.0);
        assertThat(applyIfChange(2.0, 2.0, ref::set)).isFalse();
    }
}