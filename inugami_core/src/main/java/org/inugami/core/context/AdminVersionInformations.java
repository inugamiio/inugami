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
package org.inugami.core.context;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.inugami.api.models.ClonableObject;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.core.model.system.SystemInformations;

/**
 * AdminVersionInformations
 * 
 * @author pguillerm
 * @since 28 août 2017
 */
public class AdminVersionInformations implements Serializable, ClonableObject<AdminVersionInformations> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long  serialVersionUID = 6157012429490124050L;
    
    private final Calendar     startTime;
    
    private String             currentVersion;
    
    private List<Plugin>       plugins;
    
    private SystemInformations systemInfo;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public AdminVersionInformations() {
        super();
        startTime = Calendar.getInstance();
    }
    
    public AdminVersionInformations(final AdminVersionInformations ref) {
        startTime = Calendar.getInstance();
        startTime.setTimeInMillis(ref.getStartTime().getTimeInMillis());
        
        currentVersion = ref.getCurrentVersion();
        systemInfo = ref.getSystemInfo() == null ? null : (SystemInformations) ref.getSystemInfo().cloneObj();
        
        plugins = ref.getPlugins();
        
    }
    
    @Override
    public AdminVersionInformations cloneObj() {
        return new AdminVersionInformations(this);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AdminVersionInformations [startTime=");
        builder.append(startTime);
        builder.append(", currentVersion=");
        builder.append(currentVersion);
        builder.append(", plugins=");
        builder.append(plugins);
        builder.append(", systemInfo=");
        builder.append(systemInfo);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Calendar getStartTime() {
        return startTime;
    }
    
    public String getCurrentVersion() {
        return currentVersion;
    }
    
    /* package */ void setCurrentVersion(final String currentVersion) {
        this.currentVersion = currentVersion;
    }
    
    public List<Plugin> getPlugins() {
        return plugins;
    }
    
    /* package */ void setPlugins(final List<Plugin> plugins) {
        this.plugins = plugins;
    }
    
    public SystemInformations getSystemInfo() {
        return systemInfo;
    }
    
    /* package */ void setSystemInfo(final SystemInformations systemInfo) {
        this.systemInfo = systemInfo;
    }
    
}
