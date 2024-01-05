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
package io.inugami.framework.api.tools;

import io.inugami.framework.interfaces.exceptions.Asserts;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ConfigTemplateValuesTest
 *
 * @author patrick_guillerm
 * @since 9 mai 2017
 */
class ConfigTemplateValuesTest {

    public static final String KEY = "key";

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testSearchKeys() {
        final ConfigTemplateValues conf = new ConfigTemplateValues();
        assertEquals(toList("foo"), conf.searchKeys("{{foo}}"));
        assertEquals(toList("foo", "bar"), conf.searchKeys("{{foo}} aaaa {{bar}}"));
        assertEquals(toList("foo", "bar", "joe"), conf.searchKeys("{{foo}} aaaa {{bar}}\n{{joe}}"));
    }

    @Test
    void testApplyProperties() {
        final ConfigTemplateValues conf = new ConfigTemplateValues();

        //@formatter:off

        assertEquals(null, conf.applyProperties(null, buildMap("bar", "bar")));
        assertEquals("foo.{{bar}}", conf.applyProperties("foo.{{bar}}", null));

        assertEquals("foo.bar", conf.applyProperties("foo.{{bar}}", buildMap("bar", "bar")));
        assertEquals("foo - hello the world", conf.applyProperties("foo - {{bar}}", buildMap("bar", "hello the world")));

        assertEquals("foo - hello the world - : foooooTtiti", conf.applyProperties("foo - {{bar}} - {{titi}}",
                                                                                   buildMap("bar", "hello the world",
                                                                                            "titi", ": foooooTtiti")));

        assertEquals("foo - hello the world - @azerty@", conf.applyProperties("foo - {{bar}} - {{joe}}",
                                                                              buildMap("bar", "hello the world",
                                                                                       "joe", "@{{titi}}@",
                                                                                       "titi", "azerty")));
        //@formatter:on
    }

    @Test
    void replacePropertyValue_nominal() {
        final ConfigTemplateValues conf = new ConfigTemplateValues();
        assertThat(conf.replacePropertyValue(null, KEY, "some.value")).isNull();
        assertThat(conf.replacePropertyValue("value", KEY, "some.value")).isEqualTo("value");
        assertThat(conf.replacePropertyValue("value {{key}}", KEY, "some.value")).isEqualTo("value some.value");
        assertThat(conf.replacePropertyValue("value {{key}}", KEY, "some\\value")).isEqualTo("value some\\value");
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private List<String> toList(final String... values) {
        return Arrays.asList(values);
    }

    private Map<String, String> buildMap(final String... values) {
        final Map<String, String> result = new HashMap<>();
        Asserts.assertTrue("error some parameters are missing!", (values.length % 2) == 0);

        final int nbKeys = values.length / 2;
        for (int i = 0; i < nbKeys; i++) {
            final int indexKey   = i * 2;
            final int indexValue = indexKey + 1;
            result.put(values[indexKey], values[indexValue]);
        }
        return result;
    }
}
