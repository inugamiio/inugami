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
package io.inugami.monitoring.core.interceptable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DefaultResourceIdentifierTest
 *
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
class DefaultInterceptableIdentifierTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testIsResource() throws Exception {
        final DefaultInterceptableIdentifier resolver = new DefaultInterceptableIdentifier();
        assertTrue(resolver.isResource("http://foobar.org/js/myApp.js?v=1.2.3"));
        assertTrue(resolver.isResource("http://foobar.org/style/application.css"));
        assertTrue(resolver.isResource("http://foobar.org/js/application.ts"));

        assertFalse(resolver.isResource("http://foobar.org/rest/service"));
        assertFalse(resolver.isResource("http://foobar.org/rest/service?id=1"));
    }
}
