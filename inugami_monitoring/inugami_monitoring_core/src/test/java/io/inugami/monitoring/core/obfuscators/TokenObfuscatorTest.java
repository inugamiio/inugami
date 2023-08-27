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
package io.inugami.monitoring.core.obfuscators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TokenObfuscatorTest
 *
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
class TokenObfuscatorTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testClean() throws Exception {
        final TokenObfuscator obfuscator = new TokenObfuscator();
        assertEquals("\"token\":\"xxxxx\"", obfuscator.clean("\"token\":\"azerty12345\""));
    }
}
