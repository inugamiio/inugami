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

import org.inugami.api.models.data.basic.Json;
import org.inugami.api.models.data.basic.JsonObject;

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
    public <E extends JsonBuilder>  E returnInstance() {
        return (E)this;
    }
    
    public <E extends JsonBuilder>  E writeFunction(final String name, final String... params) {
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
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E addReturnKeyword() {
        buffer.append("return ");
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E openObject() {
        buffer.append("{");
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E closeObject() {
        buffer.append("}");
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E openList() {
        buffer.append('[');
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E closeList() {
        buffer.append(']');
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E openTuple() {
        buffer.append("(");
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E closeTuple() {
        buffer.append(")");
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E openComment() {
        buffer.append("/*");
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E closeComment() {
        buffer.append("*/");
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E addField(final String name) {
        valueQuot(name);
        buffer.append(':');
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E write(final Object value) {
        buffer.append(value);
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E write(final boolean value) {
        buffer.append(value ? "true" : "false");
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E write(final byte value) {
        buffer.append(value);
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E write(final short value) {
        buffer.append(value);
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E write(final int value) {
        buffer.append(value);
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E write(final long value) {
        buffer.append(value);
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E write(final float value) {
        buffer.append(value);
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E write(final double value) {
        buffer.append(value);
        return returnInstance();
    }
    
    private <E extends JsonBuilder>  E writeParam(final String key, final String value) {
        buffer.append('"');
        buffer.append(key);
        buffer.append('"');
        buffer.append(':');
        buffer.append('"');
        buffer.append(value);
        buffer.append('"');
        
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E valueQuot(final Object value) {
        if (value == null) {
            valueNull();
        }
        else {
            buffer.append("\"");
            buffer.append(value);
            buffer.append("\"");
        }
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E valueNull() {
        buffer.append(VALUE_NULL);
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E addLine() {
        buffer.append("\n");
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E addSeparator() {
        buffer.append(SEPARATOR_CHAR);
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E addEndLine() {
        buffer.append(";\n");
        return returnInstance();
    }
    

    
    public <E extends JsonBuilder>  E writeListString(final List<String> values) {
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
        
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E line() {
        buffer.append('\n');
        return returnInstance();
    }
    
    public <E extends JsonBuilder>  E tab() {
        buffer.append('\t');
        return returnInstance();
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
    
    public JsonObject toJsonObject() {
        return new Json(toString());
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
    
  
    public void clear() {
        buffer.setLength(0);
        buffer.trimToSize();
    }
}
