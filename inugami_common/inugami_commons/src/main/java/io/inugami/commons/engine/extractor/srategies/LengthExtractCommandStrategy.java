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
package io.inugami.commons.engine.extractor.srategies;

import io.inugami.api.tools.NashornTools;
import io.inugami.commons.engine.extractor.ExtractCommand;
import io.inugami.commons.engine.extractor.ExtractCommandStrategy;

import java.util.Collection;

/**
 * LengthExtractCommandStrategy
 *
 * @author patrick_guillerm
 * @since 23 mai 2018
 */
@SuppressWarnings({"java:S1874"})
public class LengthExtractCommandStrategy implements ExtractCommandStrategy {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @Override
    public boolean accept(Object value, ExtractCommand cmd) {
        return "length".equals(cmd.getFieldName()) && (value instanceof Collection || NashornTools.isArray(value));
    }

    @Override
    public Object process(Object value, ExtractCommand cmd) {
        final Collection<?> collection = convertToList(value);
        return collection == null ? 0 : collection.size();
    }

    private Collection<?> convertToList(Object value) {
        Collection<?> result = null;
        if (NashornTools.isArray(value)) {
            result = NashornTools.convertToList(value);
        } else {
            result = (Collection<?>) value;
        }
        return result;
    }
}
