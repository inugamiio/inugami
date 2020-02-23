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
package org.inugami.monitoring.sensors.defaults.mbean;

import org.inugami.api.models.data.basic.JsonObject;

import flexjson.JSON;

public class JmxBeanSensorQuery implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -1555887276893630060L;
    
    private String            path;
    
    @JSON(name = "attribute")
    private String            mbeanAttibute;
    
    @JSON(name = "method")
    private String            mbeanMethod;
    
    public JmxBeanSensorQuery() {
    }
    
    public JmxBeanSensorQuery(final String path, final String attibute, final String method) {
        super();
        this.path = path;
        this.mbeanAttibute = attibute;
        this.mbeanMethod = method;
    }
    
    public JmxBeanSensorQuery(final JmxBeanSensorQuery query) {
        this.path = query.getPath().replaceAll("'", "\"");
        this.mbeanAttibute = query.getMbeanAttibute();
        this.mbeanMethod = query.getMbeanMethod();
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((mbeanAttibute == null) ? 0 : mbeanAttibute.hashCode());
        result = (prime * result) + ((mbeanMethod == null) ? 0 : mbeanMethod.hashCode());
        result = (prime * result) + ((path == null) ? 0 : path.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof JmxBeanSensorQuery)) {
            final JmxBeanSensorQuery other = (JmxBeanSensorQuery) obj;
            result = buildFullPath(this).equals(buildFullPath(other));
        }
        
        return result;
    }
    
    private String buildFullPath(final JmxBeanSensorQuery value) {
        return String.join("@", value.getPath(), value.getMbeanAttibute(), value.getMbeanMethod());
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getPath() {
        return path;
    }
    
    public void setPath(final String path) {
        this.path = path;
    }
    
    public String getMbeanAttibute() {
        return mbeanAttibute;
    }
    
    public void setMbeanAttibute(final String mbeanAttibute) {
        this.mbeanAttibute = mbeanAttibute;
    }
    
    public String getMbeanMethod() {
        return mbeanMethod;
    }
    
    public void setMbeanMethod(final String mbeanMethod) {
        this.mbeanMethod = mbeanMethod;
    }
    
}
