/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.framework.commons.spring.data.utils;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import io.inugami.framework.interfaces.tools.ListUtils;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.function.Consumer;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

/**
 * @since 2025-12-28
 */
@UtilityClass
public class PredicateUtils {

    // =================================================================================================================
    // STRING
    // =================================================================================================================
    public static void produceStringContains(final Collection<String> values,
                                             final StringPath field,
                                             final Consumer<BooleanExpression> consumer) {
        if (ListUtils.isEmpty(values)) {
            return;
        }
        BooleanExpression predicate = null;
        for (String value : values) {
            if (predicate == null) {
                predicate = field.contains(value);
            } else {
                predicate.or(field.contains(value));
            }
        }
        applyIfNotNull(predicate, consumer::accept);
    }


    public static void produceStringIn(final Collection<String> values,
                                       final StringPath field,
                                       final Consumer<BooleanExpression> consumer) {
        if (ListUtils.isEmpty(values)) {
            return;
        }
        consumer.accept(field.in(values));
    }

    // =================================================================================================================
    // PREDICATE
    // =================================================================================================================
    public static Predicate mergePredicateAnd(final Collection<BooleanExpression> predicates) {
        if (ListUtils.isEmpty(predicates)) {
            return null;
        }

        BooleanExpression result = null;
        for (BooleanExpression predicate : predicates) {
            if (result == null) {
                result = predicate;
            } else {
                result = result.and(predicate);
            }
        }
        return result;
    }
}
