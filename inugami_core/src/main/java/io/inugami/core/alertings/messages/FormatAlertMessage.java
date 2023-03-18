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
package io.inugami.core.alertings.messages;

import java.util.List;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.MessagesFormatter;
import io.inugami.api.spi.SpiLoader;
import io.inugami.commons.messages.MessagesServices;

/**
 * FormatAlertMessage
 *
 * @author patrick_guillerm
 * @since 22 mars 2018
 */
public final class FormatAlertMessage {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static AlertMessageDataExtractorSPI DEFAULT_DATA_EXCTRACTOR = new DefaultAlertMessageDataExtractor();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private FormatAlertMessage() {
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public static String formatMessage(final String value, final AlertingResult alert) {
        if ((value == null) || (alert == null)) {
            return value;
        }
        return isTemplateMessage(value) ? processTemplate(value, alert) : value;
    }

    // =========================================================================
    // PRIVATE
    // =========================================================================
    private static boolean isTemplateMessage(final String value) {
        return value.trim().startsWith("_");
    }

    private static String processTemplate(final String value, final AlertingResult alert) {
        final String key      = value.substring(1);
        final String template = MessagesServices.getMessage(key);

        final AlertMessageDataExtractorSPI extractor = resolveExtractor(key, alert);
        Asserts.notNull(extractor);
        final List<String> data = extractor.extract(key, alert);

        return data == null ? value : MessagesFormatter.format(template, data.toArray());
    }

    private static AlertMessageDataExtractorSPI resolveExtractor(final String key, final AlertingResult alert) {
        AlertMessageDataExtractorSPI result = null;
        final List<AlertMessageDataExtractorSPI> extractors = SpiLoader.INSTANCE.loadSpiServicesWithDefault(AlertMessageDataExtractorSPI.class,
                                                                                                    DEFAULT_DATA_EXCTRACTOR);

        for (final AlertMessageDataExtractorSPI extractor : extractors) {
            if (extractor.accept(key, alert)) {
                result = extractor;
                break;
            }
        }
        return result;
    }

}
