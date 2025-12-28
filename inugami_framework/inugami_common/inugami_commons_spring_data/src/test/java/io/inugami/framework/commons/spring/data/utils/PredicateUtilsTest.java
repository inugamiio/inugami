package io.inugami.framework.commons.spring.data.utils;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class PredicateUtilsTest {
    private final StringPath field = Expressions.stringPath("userName");

    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(PredicateUtils.class);
    }

    @Test
    void produceStringContains_withoutData() {
        AtomicReference<BooleanExpression> result = new AtomicReference<>();
        PredicateUtils.produceStringContains(Collections.emptyList(), field, result::set);
        assertThat(result.get()).isNull();
    }

    @Test
    void should_produce_contains_predicate_for_single_value() {
        AtomicReference<BooleanExpression> result = new AtomicReference<>();
        PredicateUtils.produceStringContains(List.of("admin"), field, result::set);

        assertThat(result.get()).isNotNull();
        assertThat(result.get().toString()).isEqualTo("contains(userName,admin)");
    }

    @Test
    void should_produce_or_predicate_for_multiple_values() {
        AtomicReference<BooleanExpression> result = new AtomicReference<>();
        PredicateUtils.produceStringContains(Arrays.asList("admin", "guest"), field, result::set);

        assertThat(result.get()).isNotNull();
        assertThat(result.get().toString()).contains("contains(userName,admin)");
    }


    @Test
    void should_do_nothing_when_values_are_empty() {
        AtomicReference<BooleanExpression> result = new AtomicReference<>();
        PredicateUtils.produceStringIn(null, field, result::set);
        assertThat(result.get()).isNull();
    }

    @Test
    void should_produce_in_predicate_when_values_present() {
        AtomicReference<BooleanExpression> result = new AtomicReference<>();
        List<String>                       values = Arrays.asList("A", "B");

        PredicateUtils.produceStringIn(values, field, result::set);

        assertThat(result.get()).isNotNull();
        assertThat(result.get().toString()).isEqualTo("userName in [A, B]");
    }


    @Test
    void should_return_null_when_list_is_empty() {
        Predicate result = PredicateUtils.mergePredicateAnd(new ArrayList<>());
        assertThat(result).isNull();
    }

    @Test
    void should_return_single_predicate_when_one_value() {
        BooleanExpression p1     = field.eq("test");
        Predicate         result = PredicateUtils.mergePredicateAnd(Collections.singletonList(p1));

        assertThat(result).isEqualTo(p1);
        assertThat(result.toString()).isEqualTo("userName = test");
    }

    @Test
    void should_merge_multiple_predicates_with_and() {
        BooleanExpression p1 = field.startsWith("A");
        BooleanExpression p2 = field.endsWith("Z");

        Predicate result = PredicateUtils.mergePredicateAnd(Arrays.asList(p1, p2));

        assertThat(result).isNotNull();
        assertThat(result.toString()).isEqualTo("startsWith(userName,A) && endsWith(userName,Z)");
    }
}