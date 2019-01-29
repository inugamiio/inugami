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
package org.inugami.configuration;

import org.inugami.configuration.models.ProviderConfig;

/**
 * Pojo
 *
 * @author rachid_nidsaid
 * @since 22 sept. 2016
 */
public class MetricData {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    protected String         name;
    
    protected String         request;
    
    protected ProviderConfig config;
    
    protected Long           lastCallTime;
    
    protected String         from;
    
    protected String         until;
    
    protected String         groupName;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MetricData() {
        super();
    }
    
    public MetricData(final String name) {
        this();
        this.name = name;
    }
    
    public MetricData(final String name, final ProviderConfig config) {
        this(name);
        this.config = config;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getName() {
        return name;
    }
    
    public String getRequset() {
        return request;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(final String from) {
        this.from = from;
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public String getRequest() {
        return request;
    }
    
    public void setRequest(final String request) {
        this.request = request;
    }
    
    public void updateLastCallTime(final Long lastCallTime) {
        this.lastCallTime = lastCallTime;
    }
    
    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }
    
    public Long getLastCallTime() {
        return lastCallTime;
    }
    
    public String getUntil() {
        return until;
    }
    
    public void setUntil(final String until) {
        this.until = until;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("[").append(name).append(":").append(request).append(":").append(config.toString()).append("]").toString();
    }
    
    public ProviderConfig getConfig() {
        return config;
    }
    
}
