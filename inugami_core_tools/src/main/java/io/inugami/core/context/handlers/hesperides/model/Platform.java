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
package io.inugami.core.context.handlers.hesperides.model;

import java.nio.charset.Charset;
import java.util.List;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.data.basic.JsonObject;

import flexjson.JSON;
import flexjson.JSONDeserializer;
import flexjson.JSONException;

public class Platform implements JsonObject {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 7399778212307079565L;
    
    @JSON(name = "platform_name")
    private String            platformName;
    
    @JSON(name = "application_name")
    private String            applicationName;
    
    @JSON(name = "application_version")
    private String            applicationVersion;
    
    @JSON(name = "version_id")
    private int               versionId;
    
    private boolean           production;
    
    private List<Module>      modules;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Platform() {
    }
    
    public Platform(final String platformName, final String applicationName, final String applicationVersion,
                    final int versionId, final boolean production, final List<Module> modules) {
        this.platformName = platformName;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.versionId = versionId;
        this.production = production;
        this.modules = modules;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public <T extends JsonObject> T convertToObject(final byte[] data, final Charset charset) {
        Platform result = null;
        
        if (data != null) {
            final String json = charset == null ? new String(data).trim() : new String(data, charset).trim();
            
            if (!json.isEmpty()) {
                try {
                    result = new JSONDeserializer<Platform>().deserialize(json, Platform.class);
                    
                }
                catch (final JSONException error) {
                    Loggers.DEBUG.error("{} : \n payload:\n----------\n{}\n----------\n", error.getMessage(), json);
                }
            }
        }
        
        return (T) result;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getPlatformName() {
        return platformName;
    }
    
    public void setPlatformName(final String platformName) {
        this.platformName = platformName;
    }
    
    public String getApplicationName() {
        return applicationName;
    }
    
    public void setApplicationName(final String applicationName) {
        this.applicationName = applicationName;
    }
    
    public String getApplicationVersion() {
        return applicationVersion;
    }
    
    public void setApplicationVersion(final String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }
    
    public int getVersionId() {
        return versionId;
    }
    
    public void setVersionId(final int versionId) {
        this.versionId = versionId;
    }
    
    public boolean getProduction() {
        return production;
    }
    
    public void setProduction(final boolean production) {
        this.production = production;
    }
    
    public List<Module> getModules() {
        return modules;
    }
    
    public void setModules(final List<Module> modules) {
        this.modules = modules;
    }
    
    // =========================================================================
    // OTHER
    // =========================================================================
    @Override
    public boolean equals(final Object other) {
        boolean result = this == other;
        if (!result && (other instanceof Platform)) {
            final Platform obj = (Platform) other;
            result = this.platformName.equals(obj.getPlatformName());
        }
        return result;
    }
    
    @Override
    public int hashCode() {
        return 31 * ((this.platformName == null) ? 0 : this.platformName.hashCode());
    }
}
