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
package io.inugami.api.mapping;

import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Transformer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateTransformer
 *
 * @author patrick_guillerm
 * @since 18 janv. 2019
 */
public class DateTransformer extends AbstractTransformer implements Transformer {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        getContext().write(processTransfo(object));
    }

    public static String processTransfo(final Object object) {
        String result = null;
        if (object instanceof Date) {
            result = "\"" + new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSZ").format((Date) object) + "\"";
        } else {
            result = "null";
        }
        return result;
    }
}
