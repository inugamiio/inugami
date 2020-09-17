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
package io.inugami.api.models.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javax.script.Bindings;

import io.inugami.api.functionnals.ApplyIfNotNullAndSameType;
import io.inugami.api.mapping.events.ListAlertsMapper;
import io.inugami.api.mapping.events.ListProcessorMapper;
import io.inugami.api.models.Builder;
import io.inugami.api.processors.ProcessorModel;
import io.inugami.api.tools.NashornTools;

/**
 * SimpleEventBuilder
 * 
 * @author patrick_guillerm
 * @since 20 juil. 2017
 */
public class SimpleEventBuilder implements Builder<SimpleEvent>, ApplyIfNotNullAndSameType {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String               name;
    
    private String               from;
    
    private String               until;
    
    private String               provider;
    
    private String               scheduler;
    
    private String               mapper;
    
    private List<ProcessorModel> processors;
    
    private String               query;
    
    private String               parent;
    
    private List<AlertingModel>  alertings;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SimpleEventBuilder() {
    }
    
    public SimpleEventBuilder(final SimpleEvent event) {
        name = event.getName();
        query = event.getQuery();
        scheduler = event.getScheduler();
        
        event.getFrom().ifPresent(s -> from = s);
        event.getUntil().ifPresent(s -> until = s);
        event.getProvider().ifPresent(s -> provider = s);
        event.getProcessors().ifPresent(s -> processors = s);
        event.getParent().ifPresent(s -> parent = s);
        event.getAlertings().ifPresent(s -> alertings = s);
    }
    
    public SimpleEventBuilder(final Bindings data) {
        if (data != null) {
            final ListProcessorMapper processorsMapper = new ListProcessorMapper();
            final ListAlertsMapper alertsMapper = new ListAlertsMapper();
            
            //@formatter:off
            final Function<Object, String> mapString                   = buildStringMapper();
            
            final Function<Object, List<ProcessorModel>> mapProcessors = v -> NashornTools.isArray(v) ? processorsMapper.mapping(NashornTools.convertToList(v)): null;
            final Function<Object, List<AlertingModel>> mapAlerts      = v -> NashornTools.isArray(v) ? alertsMapper.mapping(NashornTools.convertToList(v)): null;
            
            ifNotNullAndSameType(data.get("name")       , mapString     , v -> name = v);
            ifNotNullAndSameType(data.get("from")       , mapString     , v -> from = v);
            ifNotNullAndSameType(data.get("until")      , mapString     , v -> until = v);
            ifNotNullAndSameType(data.get("provider")   , mapString     , v -> provider = v);
            ifNotNullAndSameType(data.get("scheduler")  , mapString     , v -> scheduler = v);
            ifNotNullAndSameType(data.get("mapper")     , mapString     , v -> mapper = v);
            ifNotNullAndSameType(data.get("query")      , mapString     , v -> query = v);
            ifNotNullAndSameType(data.get("processors") , mapProcessors , v -> processors = v);
            ifNotNullAndSameType(data.get("alertings")  , mapAlerts     , v -> alertings = v);
            //@formatter:on            
        }
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public SimpleEventBuilder addName(final String name) {
        this.name = name;
        return this;
    }
    
    public SimpleEventBuilder addFrom(final String from) {
        this.from = from;
        return this;
    }
    
    public SimpleEventBuilder addParent(final String parent) {
        this.parent = parent;
        return this;
    }
    
    public SimpleEventBuilder addUntil(final String until) {
        this.until = until;
        return this;
    }
    
    public SimpleEventBuilder addScheduler(final String scheduler) {
        this.scheduler = scheduler;
        return this;
    }
    
    public SimpleEventBuilder addMapper(final String mapper) {
        this.mapper = mapper;
        return this;
    }
    
    public SimpleEventBuilder addProvider(final String provider) {
        this.provider = provider;
        return this;
    }
    
    public SimpleEventBuilder addProcessors(final List<ProcessorModel> processors) {
        this.processors = processors;
        return this;
    }
    
    public SimpleEventBuilder addProcessors(final ProcessorModel... processors) {
        final List<ProcessorModel> data = Arrays.asList(processors);
        if (this.processors == null) {
            this.processors = data;
        }
        else {
            this.processors.addAll(data);
        }
        return this;
    }
    
    public SimpleEventBuilder addAlertings(final List<AlertingModel> alertings) {
        this.alertings = alertings;
        return this;
    }
    
    public SimpleEventBuilder addAlerting(final AlertingModel alerting) {
        if (alertings == null) {
            alertings = new ArrayList<>();
        }
        if (alerting != null) {
            alertings.add(alerting);
        }
        return this;
    }
    
    public SimpleEventBuilder addQuery(final String query) {
        this.query = query;
        return this;
    }
    
    @Override
    public SimpleEvent build() {
        return new SimpleEvent(name, from, until, provider, processors, query, parent, scheduler, mapper, alertings);
        
    }
    
}
