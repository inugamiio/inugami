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
package io.inugami.interfaces.models.basic;

import io.inugami.api.exceptions.NotYetImplementedException;
import io.inugami.api.models.JsonBuilder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonObjects
 *
 * @author patrick_guillerm
 * @since 15 mai 2017
 */
@SuppressWarnings({"java:S119"})
public class JsonObjects<TYPE extends JsonObject> implements JsonObject {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2669600647907300725L;

    private final List<TYPE> data;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public JsonObjects(final List<TYPE> data) {
        super();
        this.data = data;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public <T extends JsonObject> T convertToObject(final byte[] data, final Charset encoding) {
        throw new NotYetImplementedException();
    }

    @Override
    public String convertToJson() {
        final JsonBuilder result = new JsonBuilder();
        result.openList();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (i != 0) {
                    result.addSeparator();
                }
                result.write(data.get(i).convertToJson());
            }
        }
        result.closeList();
        return result.toString();
    }

    public List<TYPE> getData() {
        return data;
    }

    @Override
    public JsonObject cloneObj() {
        final List<TYPE> newData = new ArrayList<>();
        if (data != null) {
            for (final JsonObject item : data) {
                newData.add((TYPE) item.cloneObj());
            }
        }
        return new JsonObjects<>(newData);
    }

}
