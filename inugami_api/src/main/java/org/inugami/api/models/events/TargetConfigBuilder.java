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
package org.inugami.api.models.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.inugami.api.models.Builder;
import org.inugami.api.processors.ProcessorModel;

/**
 * TargetConfigBuilder
 * 
 * @author patrick_guillerm
 * @since 24 mai 2017
 */
public class TargetConfigBuilder implements Builder<TargetConfig> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String               name;
    
    private String               from;
    
    private String               until;
    
    private String               provider;
    
    private List<ProcessorModel> processors;
    
    private String               query;
    
    private String               parent;
    
    private String               mapper;
    
    private List<AlertingModel>  alertings;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public static TargetConfigBuilder builder() {
        return new TargetConfigBuilder();
    }
    
    public TargetConfigBuilder() {
    }
    
    public TargetConfigBuilder(final TargetConfig event) {
        name = event.getName();
        query = event.getQuery();
        
        event.getFrom().ifPresent(value -> from = value);
        event.getUntil().ifPresent(value -> until = value);
        event.getProvider().ifPresent(value -> provider = value);
        event.getProcessors().ifPresent(value -> processors = value);
        event.getParent().ifPresent(value -> parent = value);
        event.getFrom().ifPresent(value -> from = value);
        event.getAlertings().ifPresent(s -> alertings = s);
    }
    
    // =========================================================================
    // BUILDER
    // =========================================================================
    public TargetConfigBuilder addName(final String name) {
        this.name = name;
        return this;
    }
    
    public TargetConfigBuilder addFrom(final String from) {
        this.from = from;
        return this;
    }
    
    public TargetConfigBuilder addUntil(final String until) {
        this.until = until;
        return this;
    }
    
    public TargetConfigBuilder addMapper(final String mapper) {
        this.mapper = mapper;
        return this;
    }
    
    public TargetConfigBuilder addProvider(final String provider) {
        this.provider = provider;
        return this;
    }
    
    public TargetConfigBuilder addProcessors(final List<ProcessorModel> processors) {
        this.processors = processors;
        return this;
    }
    
    public TargetConfigBuilder addProcessors(final ProcessorModel... processors) {
        final List<ProcessorModel> data = Arrays.asList(processors);
        if (this.processors == null) {
            this.processors = data;
        }
        else {
            this.processors.addAll(data);
        }
        return this;
    }
    
    public TargetConfigBuilder addAlertings(final List<AlertingModel> alertings) {
        this.alertings = alertings;
        return this;
    }
    
    public TargetConfigBuilder addAlerting(final AlertingModel alerting) {
        if (alertings == null) {
            alertings = new ArrayList<>();
        }
        if (alerting != null) {
            alertings.add(alerting);
        }
        return this;
    }
    
    public TargetConfigBuilder addQuery(final String query) {
        this.query = query;
        return this;
    }
    
    @Override
    public TargetConfig build() {
        return new TargetConfig(name, from, until, provider, processors, query, parent, null, mapper, alertings);
    }
    
}
