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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * TokenObfuscatorTest
 *
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
@ExtendWith(MockitoExtension.class)
class TokenObfuscatorTest {
    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void accept_nominal() {
        final var obfuscator = buildObfuscator();
        assertThat(obfuscator.accept(null)).isFalse();
        assertThat(obfuscator.accept("")).isFalse();

        assertThat(obfuscator.accept("TOKEN")).isTrue();
        assertThat(obfuscator.accept("token")).isTrue();
    }


    @Test
    void clean_nominal() {
        final var obfuscator = buildObfuscator();
        assertText(obfuscator.clean("""
                                            "token": "azerty12345"   
                                            """),
                   """
                           "token":"xxxxx"
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    TokenObfuscator buildObfuscator() {
        return new TokenObfuscator();
    }

}
