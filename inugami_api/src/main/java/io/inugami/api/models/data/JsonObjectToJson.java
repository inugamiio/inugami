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
package io.inugami.api.models.data;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import io.inugami.api.models.JsonBuilder;
import io.inugami.api.models.data.basic.JsonSerializerSpi;
import io.inugami.api.spi.SpiLoader;


/**
 * JsonObject
 * 
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public interface JsonObjectToJson extends Serializable {
    
    default String convertToJson() {
        final JsonSerializerSpi jsonSerializer = SpiLoader.getInstance().loadSpiSingleService(JsonSerializerSpi.class);
        return jsonSerializer.serialize(this);
    }
    
    default String buildJsonError(final Exception error) {
        final JsonSerializerSpi jsonSerializer = SpiLoader.getInstance().loadSpiSingleService(JsonSerializerSpi.class);
        final JsonBuilder json = new JsonBuilder();
        json.openObject();
        json.addField("message").valueQuot(error.getMessage());
        if (error.getCause() != null) {
            json.addSeparator();
            json.addField("cause").valueQuot(error.getCause().getMessage());
        }
        
        json.addSeparator();
        json.addField("stack").write(jsonSerializer.serialize(extractStack(error)));
        
        json.closeObject();
        return json.toString();
    }
    
    default String extractStack(final Exception error) {
        final StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        final String exceptionAsString = writer.toString();
        return exceptionAsString;
    }
}
