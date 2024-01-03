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
package io.inugami.interfaces.models.event;


import io.inugami.api.processors.ProcessorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Target
 *
 * @author patrick_guillerm
 * @since 4 oct. 2016
 */
// target
public class TargetConfig extends SimpleEvent {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8813704499596910994L;

    public TargetConfig() {
        super();
    }

    public TargetConfig(final String name, final String from, final String until, final String provider,
                        final List<ProcessorModel> processors, final String query, final String parent,
                        final String scheduler, final String mapper, final List<AlertingModel> alertings) {
        super(name, from, until, provider, processors, query, parent, scheduler, mapper, alertings);
    }

    @Override
    public GenericEvent cloneObj() {
        final List<ProcessorModel> cpProcessors = new ArrayList<>();
        getProcessors().ifPresent(cpProcessors::addAll);

        final List<AlertingModel> cpAlertings = new ArrayList<>();
        getAlertings().ifPresent(cpAlertings::addAll);

        final String from      = grabData(getFrom());
        final String until     = grabData(getUntil());
        final String provider  = grabData(getProvider());
        final String parent    = grabData(getParent());
        final String mapper    = grabData(getMapper());
        final String scheduler = getScheduler();

        final TargetConfig result = new TargetConfig(getName(), from, until, provider, cpProcessors, getQuery(), parent,
                                                     scheduler, mapper, cpAlertings);

        getFromFirstTime().ifPresent(result::buildFromFirstTime);
        return result;
    }

    private String grabData(final Optional<String> value) {
        return value.isPresent() ? value.get() : null;
    }
}
