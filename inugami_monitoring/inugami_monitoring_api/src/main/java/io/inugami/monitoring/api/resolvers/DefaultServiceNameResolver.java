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
package io.inugami.monitoring.api.resolvers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DefaultServiceNameResolver
 *
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
@SuppressWarnings({"java:S6395", "java:S6397", "java:S6353"})
public class DefaultServiceNameResolver implements ServiceNameResolver {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Pattern REGEX = Pattern.compile("(?:http[s]{0,1}:[\\/]{2})(?:[^\\/]+)([^?]+)(?:.*)");

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String resolve(String uri) {
        String        result  = null;
        final Matcher matcher = uri == null ? null : REGEX.matcher(uri);

        if (matcher != null && matcher.matches()) {
            final String group = matcher.group(1);
            if (group.startsWith("/")) {
                result = group.substring(1);
            } else {
                result = group;
            }

        }
        return result;
    }

}
