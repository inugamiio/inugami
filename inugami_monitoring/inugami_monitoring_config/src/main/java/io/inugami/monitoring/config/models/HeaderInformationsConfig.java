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
package io.inugami.monitoring.config.models;

import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;

/**
 * HeaderInformations
 * 
 * @author patrickguillerm
 * @since Jan 14, 2019
 */
public class HeaderInformationsConfig implements PostProcessing<ConfigHandler<String, String>> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private DefaultHeaderInformation defaultHeader;
    
    private SpecificHeaders          specificHeader;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public void postProcessing(ConfigHandler<String, String> context) throws TechnicalException {
        if (defaultHeader == null) {
            defaultHeader = new DefaultHeaderInformation();
        }
        defaultHeader.postProcessing(context);
        if (specificHeader == null) {
            specificHeader = new SpecificHeaders();
        }
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public DefaultHeaderInformation getDefaultHeader() {
        return defaultHeader;
    }
    
    public void setDefaultHeader(DefaultHeaderInformation defaultHeader) {
        this.defaultHeader = defaultHeader;
    }
    
    public SpecificHeaders getSpecificHeader() {
        return specificHeader;
    }
    
    public void setSpecificHeader(SpecificHeaders specificHeader) {
        this.specificHeader = specificHeader;
    }
    
}
