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
package org.inugami.api.models;

import java.util.List;

import org.inugami.api.models.data.JsonObject;
import org.inugami.api.models.data.basic.Json;

/**
 * JsonBuilder
 * 
 * @author patrick_guillerm
 * @since 23 févr. 2017
 */
public class JsonBuilder {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    public static final char    SEPARATOR_CHAR = ',';
    
    public static final String  VALUE_NULL     = "null";
    
    private final StringBuilder buffer         = new StringBuilder();
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public JsonBuilder writeFunction(final String name, final String... params) {
        buffer.append(" function ");
        buffer.append(name);
        openTuple();
        for (int i = 0; i < params.length; i++) {
            if (i != 0) {
                addSeparator();
            }
            buffer.append(params[i]);
        }
        closeTuple();
        return this;
    }
    
    public JsonBuilder addReturnKeyword() {
        buffer.append("return ");
        return this;
    }
    
    public JsonBuilder openObject() {
        buffer.append("{");
        return this;
    }
    
    public JsonBuilder closeObject() {
        buffer.append("}");
        return this;
    }
    
    public JsonBuilder openList() {
        buffer.append('[');
        return this;
    }
    
    public JsonBuilder closeList() {
        buffer.append(']');
        return this;
    }
    
    public JsonBuilder openTuple() {
        buffer.append("(");
        return this;
    }
    
    public JsonBuilder closeTuple() {
        buffer.append(")");
        return this;
    }
    
    public JsonBuilder openComment() {
        buffer.append("/*");
        return this;
    }
    
    public JsonBuilder closeComment() {
        buffer.append("*/");
        return this;
    }
    
    public JsonBuilder addField(final String name) {
        valueQuot(name);
        buffer.append(':');
        return this;
    }
    
    public JsonBuilder write(final Object value) {
        buffer.append(value);
        return this;
    }
    
    public JsonBuilder write(final boolean value) {
        buffer.append(value ? "true" : "false");
        return this;
    }
    
    public JsonBuilder write(final byte value) {
        buffer.append(value);
        return this;
    }
    
    public JsonBuilder write(final short value) {
        buffer.append(value);
        return this;
    }
    
    public JsonBuilder write(final int value) {
        buffer.append(value);
        return this;
    }
    
    public JsonBuilder write(final long value) {
        buffer.append(value);
        return this;
    }
    
    public JsonBuilder write(final float value) {
        buffer.append(value);
        return this;
    }
    
    public JsonBuilder write(final double value) {
        buffer.append(value);
        return this;
    }
    
    private JsonBuilder writeParam(final String key, final String value) {
        buffer.append('"');
        buffer.append(key);
        buffer.append('"');
        buffer.append(':');
        buffer.append('"');
        buffer.append(value);
        buffer.append('"');
        
        return this;
    }
    
    public JsonBuilder valueQuot(final Object value) {
        if (value == null) {
            valueNull();
        }
        else {
            buffer.append("\"");
            buffer.append(value);
            buffer.append("\"");
        }
        return this;
    }
    
    public JsonBuilder valueNull() {
        buffer.append(VALUE_NULL);
        return this;
    }
    
    public JsonBuilder addLine() {
        buffer.append("\n");
        return this;
    }
    
    public JsonBuilder addSeparator() {
        buffer.append(SEPARATOR_CHAR);
        return this;
    }
    
    public JsonBuilder addEndLine() {
        buffer.append(";\n");
        return this;
    }
    
    public JsonBuilder writeListJsonObject(final List<? extends JsonObject> values) {
        if (values == null) {
            valueNull();
        }
        else {
            openList();
            final int size = values.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    addSeparator();
                }
                write(values.get(i).convertToJson());
            }
            closeList();
        }
        
        return this;
    }
    
    public JsonBuilder writeListString(final List<String> values) {
        if (values == null) {
            valueNull();
        }
        else {
            openList();
            final int size = values.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    addSeparator();
                }
                valueQuot(values.get(i));
            }
            closeList();
        }
        
        return this;
    }
    
    public JsonBuilder line() {
        buffer.append('\n');
        return this;
    }
    
    public JsonBuilder tab() {
        buffer.append('\t');
        return this;
    }
    
    public StringBuilder append(final Object value) {
        return buffer.append(value);
    }
    
    public StringBuilder append(final String value) {
        return buffer.append(value);
    }
    
    public StringBuilder append(final StringBuffer value) {
        return buffer.append(value);
    }
    
    public StringBuilder append(final CharSequence value) {
        return buffer.append(value);
    }
    
    public StringBuilder append(final CharSequence value, final int start, final int end) {
        return buffer.append(value, start, end);
    }
    
    public StringBuilder append(final char... str) {
        return buffer.append(str);
    }
    
    public StringBuilder append(final char[] str, final int offset, final int len) {
        return buffer.append(str, offset, len);
    }
    
    public StringBuilder append(final boolean value) {
        return buffer.append(value);
    }
    
    public StringBuilder append(final char value) {
        return buffer.append(value);
    }
    
    public StringBuilder append(final int value) {
        return buffer.append(value);
    }
    
    public StringBuilder append(final long value) {
        return buffer.append(value);
    }
    
    public StringBuilder append(final float value) {
        return buffer.append(value);
    }
    
    public StringBuilder append(final double value) {
        return buffer.append(value);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        return buffer.toString();
    }
    
    public JsonObject toJsonObject() {
        return new Json(toString());
    }
    
    public void clear() {
        buffer.setLength(0);
        buffer.trimToSize();
    }
    
}
