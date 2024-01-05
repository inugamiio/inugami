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
package io.inugami.framework.interfaces.feature;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Feature {
    /**
     * The feature name will take the method name. If you want to override this behaviour your need to
     * specify the feature name
     *
     * @return feature name
     */
    String value() default "";

    boolean enabledByDefault() default true;

    String propertyPrefix() default "";

    String fallback() default "";

    /**
     * Allows to disabled monitoring on feature. Property value <strong>propertyPrefix.value.monitoring.enabled</strong>
     * will override this value if present;
     *
     * @return if inugami should monitore this feature
     */
    boolean monitored() default true;
}
