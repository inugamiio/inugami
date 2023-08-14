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
package io.inugami.commons.spring.feature;

import io.inugami.api.feature.Feature;
import io.inugami.api.feature.FeatureContext;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;

@SuppressWarnings({"java:S3358"})
@UtilityClass
public class FeatureUtils {


    public static FeatureContext buildFeatureContext(final Method method,
                                                     final Class<?> bean,
                                                     final Object[] args,
                                                     final Object instance) {
        final Feature feature = method.getAnnotation(Feature.class);

        return feature == null
                ? null
                : FeatureContext.builder()
                                .featureName(feature.value().isEmpty() ? method.getName() : feature.value())
                                .enabledByDefault(feature.enabledByDefault())
                                .propertyPrefix(feature.propertyPrefix()
                                                       .isEmpty() ? bean.getName() : feature.propertyPrefix())
                                .fallback(feature.fallback())
                                .monitored(feature.monitored())
                                .bean(bean)
                                .method(method)
                                .args(args)
                                .instance(instance)
                                .build();
    }
}
