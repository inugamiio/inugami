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
package io.inugami.configuration.models;

import java.io.Serializable;
import java.util.List;

import io.inugami.api.models.Gav;
import io.inugami.api.models.events.Event;
import io.inugami.api.models.events.SimpleEvent;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * ConfigurationData
 * 
 * @author patrick_guillerm
 * @since 4 oct. 2016
 */
@XStreamAlias("configuration")
public class EventConfig implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long       serialVersionUID = -1501568308837189494L;
    
    @XStreamOmitField
    private final Gav               gav;
    
    @XStreamOmitField
    private final String            name;
    
    @XStreamAsAttribute
    private String                  scheduler;
    
    @XStreamAsAttribute
    private Boolean                 enable;
    
    @XStreamOmitField
    private final String            configFile;
    
    @XStreamImplicit
    private final List<Event>       events;
    
    @XStreamImplicit
    private final List<SimpleEvent> simpleEvents;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public EventConfig() {
        this(null, null, null, null, null, null, null);
    }
    
    public EventConfig(final List<Event> events) {
        this(events, null, null, null, null, null, null);
    }
    
    public EventConfig(final List<Event> events, final List<SimpleEvent> simpleEvents) {
        this(events, simpleEvents, null, null, null, null, null);
    }
    
    public EventConfig(final EventConfig eventConfig, final String filePath, final String fileName, final Gav gav) {
        this(eventConfig.getEvents(), eventConfig.getSimpleEvents(), filePath, fileName, eventConfig.getEnable(), gav,
             eventConfig.getScheduler());
    }
    
    public EventConfig(final List<Event> events, final List<SimpleEvent> simpleEvents, final String configFile,
                       final String name, final Boolean enable, final Gav gav, final String scheduler) {
        this.events = events;
        this.simpleEvents = simpleEvents;
        this.configFile = configFile;
        this.name = name;
        this.enable = enable;
        this.gav = gav;
        this.scheduler = scheduler;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public List<Event> getEvents() {
        return events;
    }
    
    public List<SimpleEvent> getSimpleEvents() {
        return simpleEvents;
    }
    
    public String getConfigFile() {
        return configFile;
    }
    
    public String getName() {
        return name;
    }
    
    public Boolean getEnable() {
        return enable == null ? Boolean.TRUE : enable;
    }
    
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    public Gav getGav() {
        return gav;
    }
    
    public String getScheduler() {
        return scheduler;
    }
    
    public void setScheduler(final String scheduler) {
        this.scheduler = scheduler;
    }
    
}
