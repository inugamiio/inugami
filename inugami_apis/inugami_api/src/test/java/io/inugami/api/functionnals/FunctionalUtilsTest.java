package io.inugami.api.functionnals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static io.inugami.interfaces.functionnals.FunctionalUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

class FunctionalUtilsTest {

    public static final String HELLO      = "hello";
    public static final String THE_WORLD  = "the world";
    public static final String LIST_HELLO = "[hello]";
    public static final String MAP_HELLO  = "{key=hello}";

    // =================================================================================================================
    // PROCESS
    // =================================================================================================================
    @Test
    void processIfNull_nominal() {
        final List<String> values = new ArrayList<>();
        processIfNull(null, () -> values.add("nullValue"));
        processIfNull("non null", () -> values.add("nonNullValue"));
        assertThat(values).hasToString("[nullValue]");
    }

    @Test
    void processIfNotNull_nominal() {
        final List<String> values = new ArrayList<>();
        processIfNotNull(null, () -> values.add("nullValue"));
        processIfNotNull("non null", () -> values.add("nonNullValue"));
        assertThat(values).hasToString("[nonNullValue]");
    }


    @Test
    void processIfNotNull_consumer() {
        final List<String> values    = new ArrayList<>();
        String             nullValue = null;
        processIfNotNull(nullValue, values::add);
        processIfNotNull("non null", values::add);
        assertThat(values).hasToString("[non null]");
    }

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
    // APPLY IF NULL
    // =================================================================================================================
    @Test
    void applyIfNull_nominal() {
        final String nullStr = null;
        assertThat(applyIfNull(nullStr, () -> HELLO)).isEqualTo(HELLO);
        assertThat(applyIfNull(THE_WORLD, () -> HELLO)).isEqualTo(THE_WORLD);
    }


    // =================================================================================================================
    // APPLY IF NULL
    // =================================================================================================================
    @Test
    void applyIfEmpty_list() {
        final List<String> value = null;
        assertThat(applyIfEmpty(value, () -> List.of(HELLO))).hasToString(LIST_HELLO);
        assertThat(applyIfEmpty(List.of(), () -> List.of(HELLO))).hasToString(LIST_HELLO);
        assertThat(applyIfEmpty(List.of(HELLO), () -> List.of())).hasToString(LIST_HELLO);
    }

    @Test
    void applyIfEmpty_map() {
        final Map<String, String> value      = null;
        final Map<String, String> valueEmpty = new HashMap<>();
        final Map<String, String> ref        = Map.of("key", HELLO);

        assertThat(applyIfEmpty(value, () -> ref)).hasToString(MAP_HELLO);
        assertThat(applyIfEmpty(valueEmpty, () -> ref)).hasToString(MAP_HELLO);
        assertThat(applyIfEmpty(ref, () -> valueEmpty)).hasToString(MAP_HELLO);
    }


    @Test
    void applyIfEmpty_string() {
        final String value = null;
        assertThat(applyIfEmpty("   ", () -> HELLO)).hasToString(HELLO);
        assertThat(applyIfEmpty(value, () -> HELLO)).hasToString(HELLO);
        assertThat(applyIfEmpty(HELLO, () -> null)).hasToString(HELLO);
    }

    // =================================================================================================================
    // APPLY IF NOT EMPTY
    // =================================================================================================================
    @Test
    void applyIfNotEmpty_list() {
        final List<String> value = null;

        assertThat(applyIfNotEmpty(value, () -> List.of(HELLO))).isNull();
        assertThat(applyIfNotEmpty(List.of(), () -> List.of(HELLO))).isEmpty();
        assertThat(applyIfNotEmpty(List.of(THE_WORLD), () -> List.of(HELLO))).hasToString(LIST_HELLO);
    }

    @Test
    void applyIfNotEmpty_map() {
        final Map<String, String> value      = null;
        final Map<String, String> valueEmpty = new HashMap<>();
        final Map<String, String> ref        = Map.of("key", HELLO);
        final Map<String, String> valueMap   = Map.of("value", HELLO);

        assertThat(applyIfNotEmpty(value, () -> ref)).isNull();
        assertThat(applyIfNotEmpty(valueEmpty, () -> ref)).isEmpty();
        assertThat(applyIfNotEmpty(valueMap, () -> ref)).hasToString(MAP_HELLO);
    }


    @Test
    void applyIfNotEmpty_string() {
        final String value = null;

        assertThat(applyIfNotEmpty("   ", () -> HELLO)).isBlank();
        assertThat(applyIfNotEmpty(value, () -> HELLO)).isNull();
        assertThat(applyIfNotEmpty(THE_WORLD, () -> HELLO)).hasToString(HELLO);
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

    @Test
    void hasChange_withNullValue() {
        assertThat(hasChange(null, null)).isFalse();
    }
}