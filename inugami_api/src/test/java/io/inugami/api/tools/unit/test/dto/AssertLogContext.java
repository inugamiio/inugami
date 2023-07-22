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
package io.inugami.api.tools.unit.test.dto;

import io.inugami.api.functionnals.VoidFunctionWithException;
import io.inugami.api.tools.unit.test.api.LineMatcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class AssertLogContext {

    private String                    logs;
    private String                    path;
    private VoidFunctionWithException process;
    private List<Class<?>>            classes;
    private List<String>              patterns;
    private List<LineMatcher>         lineMatchers;
    private Boolean                   cleanMdc;
    private Boolean                   integrationTest;


    public static class AssertLogContextBuilder {
        public AssertLogContextBuilder addClass(final Class<?> classToIntercept) {
            if (classes == null) {
                classes = new ArrayList<>();
            }
            if (classToIntercept != null) {
                classes.add(classToIntercept);
            }
            return this;
        }

        public AssertLogContextBuilder addPattern(final String pattern) {
            if (patterns == null) {
                patterns = new ArrayList<>();
            }
            if (pattern != null) {
                patterns.add(pattern);
            }
            return this;
        }

        public AssertLogContextBuilder cleanMdcDisabled() {
            cleanMdc = false;
            return this;
        }

        public AssertLogContextBuilder cleanMdcEnabled() {
            cleanMdc = true;
            return this;
        }

        public AssertLogContextBuilder integrationTestEnabled() {
            integrationTest = true;
            return this;
        }

        public AssertLogContextBuilder unitTest() {
            integrationTest = false;
            return this;
        }

        public AssertLogContextBuilder addLineMatcher(final LineMatcher matcher) {
            if (lineMatchers == null) {
                lineMatchers = new ArrayList<>();
            }

            if (matcher != null) {
                lineMatchers.add(matcher);
            }

            return this;
        }
    }
}
