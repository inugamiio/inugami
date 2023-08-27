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
package io.inugami.core.services.generator;

import io.inugami.api.models.JsonBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * GeneratorTest
 *
 * @author pguillerm
 * @since 21 août 2017
 */
@SuppressWarnings({"java:S2699"})
class GeneratorTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger LOGGER = LoggerFactory.getLogger(GeneratorTest.class);

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void test() {

        LOGGER.info("\n\n{}", generateDataPoints(8, 256, 15000, "0"));
    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================

    private String generateDataPoints(final int nbItem, final double minValue, final double maxValue,
                                      final String pattern) {
        final JsonBuilder   result    = new JsonBuilder();
        final double        diff      = maxValue - minValue;
        final DecimalFormat formatter = new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.US));

        for (int i = 0; i < nbItem; i++) {
            final double value = Math.random() * diff;

            result.openObject();
            result.addField("value").write(formatter.format(value)).addSeparator();
            result.addField("timestamp").write(String.valueOf(i));
            result.closeObject();
            if (i < (nbItem - 1)) {
                result.addSeparator();
            }
            result.write("\n");
        }
        return result.toString();
    }

}
