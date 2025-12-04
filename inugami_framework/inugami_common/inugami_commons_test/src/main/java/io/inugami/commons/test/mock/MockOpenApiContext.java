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
package io.inugami.commons.test.mock;

import lombok.*;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MockOpenApiContext implements Serializable {
    private List<String> customAnnotations;


    public static class MockOpenApiContextBuilder {
        public MockOpenApiContextBuilder addCustomAnnotation(@NonNull final String value) {
            initCustomAnnotations();
            applyIfNotNull(value, customAnnotations::add);
            return this;
        }

        public MockOpenApiContextBuilder addCustomAnnotation(@NonNull final Class<? extends Annotation> annotation) {
            initCustomAnnotations();
            customAnnotations.add("@" + annotation.getName());
            return this;
        }

        private void initCustomAnnotations() {
            if (customAnnotations == null) {
                customAnnotations = new ArrayList<>();
            }
        }
    }

}
