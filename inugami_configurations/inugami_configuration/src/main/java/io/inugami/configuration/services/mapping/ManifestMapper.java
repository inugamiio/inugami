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
package io.inugami.configuration.services.mapping;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

import io.inugami.api.mapping.Mapper;
import io.inugami.api.models.plugins.ManifestInfo;

/**
 * ManifestMapper
 * 
 * @author patrickguillerm
 * @since 26 nov. 2017
 */
public class ManifestMapper implements Mapper<ManifestInfo, Manifest> {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public ManifestInfo mapping(final Manifest data) {
        final Attributes mainData = data.getMainAttributes();
        return new ManifestInfo(extractWorkspace(mainData));
    }
    
    // =========================================================================
    // EXTRACTOR
    // =========================================================================
    private String extractWorkspace(final Attributes data) {
        return data.getValue(new Attributes.Name("workspace"));
    }
}
