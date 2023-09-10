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
package io.inugami.api.feature;

import io.inugami.api.models.tools.Chrono;

@SuppressWarnings({"java:S112"})
public interface FeatureInterceptor {

    default void assertAllowsToInvoke(final FeatureContext featureContext) throws Exception {
    }

    default boolean allowFallback(final Throwable error, final FeatureContext featureContext) {
        return true;
    }

    default void onBegin(final FeatureContext featureContext) {

    }

    default Object onDone(final Object result,
                          final Throwable error,
                          final Chrono chrono,
                          final FeatureContext featureContext) {
        return result;
    }
}
