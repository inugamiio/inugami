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
package io.inugami.api.models.data.graphite;

import flexjson.JsonNumber;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;
import io.inugami.interfaces.models.number.DataPoint;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DataPointDeserializer
 *
 * @author patrick_guillerm
 * @since 7 oct. 2016
 */
public class DataPointTransformer extends AbstractTransformer implements ObjectFactory {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String KEY_TIMESTAMP = "timestamp";

    private static final String KEY_VALUE = "value";

    // =========================================================================
    // SERIALIZE
    // =========================================================================
    @Override
    public void transform(final Object value) {
        if (value != null) {
            if (value instanceof DataPoint) {
                serializeItem((DataPoint) value);
            } else if (value instanceof List<?>) {
                final List<DataPoint> data = (List<DataPoint>) value;
                getContext().writeOpenArray();
                for (int i = 0; i < data.size(); i++) {
                    if (i != 0) {
                        getContext().write(",");
                    }
                    serializeItem(data.get(i));
                }

                getContext().writeCloseArray();
            }

        }

    }

    protected void serializeItem(final DataPoint value) {
        if (value != null) {

            getContext().writeOpenArray();
            if (value.getValue() == null) {
                getContext().write("null");
            } else {
                getContext().write(BigDecimal.valueOf(value.getValue()).toPlainString());
            }
            getContext().write(",");
            getContext().write(String.valueOf(value.getTimestamp()));
            getContext().writeCloseArray();
        }
    }

    // =========================================================================
    // DESERIALIZE
    // =========================================================================
    @Override
    public Object instantiate(final ObjectBinder context, final Object value, final Type targetType,
                              final Class targetClass) {
        DataPoint result = null;

        if (value != null) {
            if (value instanceof ArrayList) {
                result = new DataPoint();
                final List<JsonNumber> values = (List<JsonNumber>) value;
                if (values.get(0) != null) {
                    result.setValue(values.get(0).doubleValue());
                }
                result.setTimestamp(values.get(1).toLong());
            } else if (value instanceof Map<?, ?>) {
                final Map<String, JsonNumber> realData = (Map<String, JsonNumber>) value;

                final JsonNumber valueNumber = realData.get(KEY_VALUE);
                result = new DataPoint(valueNumber == null ? null : valueNumber.toDouble(),
                                       realData.get(KEY_TIMESTAMP).longValue());
            }

        }
        return result;
    }

}
