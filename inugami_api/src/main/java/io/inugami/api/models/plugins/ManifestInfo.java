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
package io.inugami.api.models.plugins;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

/**
 * Manifest
 * 
 * @author patrickguillerm
 * @since 26 nov. 2017
 */
public class ManifestInfo implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7900064144760028719L;
    
    private final File        workspace;
    
    private final URL         manifestUrl;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ManifestInfo(final String workspace) {
        super();
        this.workspace = workspace == null ? null : new File(workspace);
        manifestUrl = null;
    }
    
    public ManifestInfo(final ManifestInfo manifest, final URL manifestUrl) {
        workspace = manifest.getWorkspace();
        this.manifestUrl = manifestUrl;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        //@formatter:off
        return new StringBuilder(this.getClass().getSimpleName())
                .append('@')
                .append(System.identityHashCode(this))
                .append('[')
                .append("workspace=").append(workspace)
                .append(", manifestUrl=").append(manifestUrl)
                .append(']')
                .toString();
        //@formatter:on
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public File getWorkspace() {
        return workspace;
    }
    
    public URL getManifestUrl() {
        return manifestUrl;
    }
    
}
