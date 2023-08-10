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
package io.inugami.api.mapping.events;

import io.inugami.api.functionnals.ApplyIfNotNullAndSameType;
import io.inugami.api.mapping.Mapper;
import io.inugami.api.models.events.AlertingModel;
import io.inugami.api.models.events.TargetConfig;
import io.inugami.api.models.events.TargetConfigBuilder;
import io.inugami.api.processors.ProcessorModel;
import io.inugami.api.tools.NashornTools;

import javax.script.Bindings;
import java.util.List;
import java.util.function.Function;

/**
 * ListTargetsMapper
 *
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
@SuppressWarnings({"java:S1874"})
public class TargetMapper implements Mapper<TargetConfig, Object>, ApplyIfNotNullAndSameType {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public TargetConfig mapping(final Object data) {
        return data instanceof Bindings ? process((Bindings) data) : null;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private TargetConfig process(final Bindings data) {
        final TargetConfigBuilder builder          = new TargetConfigBuilder();
        final ListProcessorMapper processorsMapper = new ListProcessorMapper();
        final ListAlertsMapper    alertsMapper     = new ListAlertsMapper();

        //@formatter:off
        final Function<Object, String> mapString                   = buildStringMapper();
        
        final Function<Object, List<ProcessorModel>> mapProcessors = v -> NashornTools.isArray(v) ? processorsMapper.mapping(NashornTools.convertToList(v)): null;
        final Function<Object, List<AlertingModel>> mapAlerts      = v -> NashornTools.isArray(v) ? alertsMapper.mapping(NashornTools.convertToList(v)): null;
        
        
        ifNotNullAndSameType(data.get("name")       , mapString     , builder::addName);
        ifNotNullAndSameType(data.get("from")       , mapString     , builder::addFrom);
        ifNotNullAndSameType(data.get("until")      , mapString     , builder::addUntil);
        ifNotNullAndSameType(data.get("provider")   , mapString     , builder::addProvider);
        ifNotNullAndSameType(data.get("mapper")     , mapString     , builder::addMapper);
        ifNotNullAndSameType(data.get("query")      , mapString     , builder::addQuery);
        ifNotNullAndSameType(data.get("processors") , mapProcessors , builder::addProcessors);
        ifNotNullAndSameType(data.get("alertings")  , mapAlerts     , builder::addAlertings);
        //@formatter:on   

        return builder.build();
    }
}
