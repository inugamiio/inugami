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

import io.inugami.api.functionnals.ApplyIfNotNullAndSameType;
import io.inugami.api.mapping.events.ListAlertsMapper;
import io.inugami.api.mapping.events.ListProcessorMapper;
import io.inugami.api.mapping.events.ListTargetsMapper;
import io.inugami.api.models.Builder;
import io.inugami.api.processors.ProcessorModel;
import io.inugami.api.tools.NashornTools;

import javax.script.Bindings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * EventBuilder
 *
 * @author patrick_guillerm
 * @since 24 mai 2017
 */
@SuppressWarnings({"java:S1874", "java:S1874"})
public class EventBuilder implements Builder<Event>, ApplyIfNotNullAndSameType {

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
    private List<TargetConfig>   targets;
    private List<AlertingModel>  alertings;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public EventBuilder() {
    }

    public EventBuilder(final Event event) {
        name = event.getName();
        event.getFrom().ifPresent(value -> from = value);
        event.getUntil().ifPresent(value -> until = value);
        event.getProvider().ifPresent(value -> provider = value);
        event.getProcessors().ifPresent(value -> processors = value);
    }

    public EventBuilder(final Bindings data) {
        if (data != null) {
            final ListProcessorMapper processorsMapper = new ListProcessorMapper();
            final ListAlertsMapper    alertsMapper     = new ListAlertsMapper();
            final ListTargetsMapper   targetsMapper    = new ListTargetsMapper();

            //@formatter:off
            final Function<Object, String> mapString                   = buildStringMapper();
            
            final Function<Object, List<ProcessorModel>> mapProcessors = v -> NashornTools.isArray(v) ? processorsMapper.mapping(NashornTools.convertToList(v)): null;
            final Function<Object, List<AlertingModel>> mapAlerts      = v -> NashornTools.isArray(v) ? alertsMapper.mapping(NashornTools.convertToList(v)): null;
            final Function<Object, List<TargetConfig>> mapTargets      = v -> NashornTools.isArray(v) ? targetsMapper.mapping(NashornTools.convertToList(v)): null;
            
            ifNotNullAndSameType(data.get("name")       , mapString     , v -> name = v);
            ifNotNullAndSameType(data.get("from")       , mapString     , v -> from = v);
            ifNotNullAndSameType(data.get("until")      , mapString     , v -> until = v);
            ifNotNullAndSameType(data.get("provider")   , mapString     , v -> provider = v);
            ifNotNullAndSameType(data.get("targets")    , mapTargets    , v -> targets = v);
            ifNotNullAndSameType(data.get("scheduler")  , mapString     , v -> scheduler = v);
            ifNotNullAndSameType(data.get("mapper")     , mapString     , v -> mapper = v);
            ifNotNullAndSameType(data.get("processors") , mapProcessors , v -> processors = v);
            ifNotNullAndSameType(data.get("alertings")  , mapAlerts     , v -> alertings = v);
            //@formatter:on  
        }
    }

    // =========================================================================
    // METHODS
    // =========================================================================

    public EventBuilder addName(final String name) {
        this.name = name;
        return this;
    }

    public EventBuilder addFrom(final String from) {
        this.from = from;
        return this;
    }

    public EventBuilder addUntil(final String until) {
        this.until = until;
        return this;
    }

    public EventBuilder addProvider(final String provider) {
        this.provider = provider;
        return this;
    }

    public EventBuilder addScheduler(final String scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    public EventBuilder addMapper(final String mapper) {
        this.mapper = mapper;
        return this;
    }

    public EventBuilder addAlertings(final List<AlertingModel> alertings) {
        this.alertings = alertings;
        return this;
    }

    public EventBuilder addAlerting(final AlertingModel alerting) {
        if (alertings == null) {
            alertings = new ArrayList<>();
        }
        if (alerting != null) {
            alertings.add(alerting);
        }
        return this;
    }

    public EventBuilder addProcessors(final List<ProcessorModel> processors) {
        if (this.processors == null) {
            this.processors = new ArrayList<>();
        }
        if (processors != null) {
            this.processors.addAll(processors);
        }
        return this;
    }

    public EventBuilder addProcessors(final ProcessorModel... processors) {
        return addProcessors(Arrays.asList(processors));
    }

    public EventBuilder addTargets(final List<TargetConfig> targets) {
        if (this.targets == null) {
            this.targets = new ArrayList<>();
        }

        if (targets != null) {
            this.targets.addAll(targets);
        }
        return this;
    }

    public EventBuilder addTarget(final String name, final String query) {
        addTargets(TargetConfigBuilder.builder().addName(name).addQuery(query).build());
        return this;
    }

    public EventBuilder addTargets(final TargetConfig... targets) {
        return addTargets(Arrays.asList(targets));
    }

    @Override
    public Event build() {
        return new Event(name, from, until, provider, processors, targets, scheduler, mapper, alertings);
    }
}
