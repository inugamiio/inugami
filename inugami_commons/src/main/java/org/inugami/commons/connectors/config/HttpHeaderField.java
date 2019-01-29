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
package org.inugami.commons.connectors.config;

import java.io.Serializable;

import org.inugami.api.constants.JvmKeyValues;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.functionnals.PostProcessing;
import org.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * HttpHeaderField
 * 
 * @author patrickguillerm
 * @since 16 déc. 2017
 */
@XStreamAlias("field")
public class HttpHeaderField implements Serializable, PostProcessing<ConfigHandler<String, String>> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 6863843139538948386L;
    
    @XStreamAsAttribute
    private String            name;
    
    @XStreamAsAttribute
    private String            value;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("HttpHeaderField [name=");
        builder.append(name);
        builder.append(", value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }
    
    @Override
    public void postProcessing(final ConfigHandler<String, String> ctx) throws TechnicalException {
        value = ctx.applyProperties(JvmKeyValues.HTTP_CONNECTION_HEADER_FIELD.or(name, value));
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
}
