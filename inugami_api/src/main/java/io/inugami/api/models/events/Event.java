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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import io.inugami.api.processors.ProcessorModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Event
 *
 * @author patrick_guillerm
 * @since 4 oct. 2016
 */
@XStreamAlias("event")
public class Event extends GenericEvent {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1783705856498891253L;

    @XStreamImplicit
    private final List<TargetConfig> targets;

    @XStreamAsAttribute
    private String scheduler;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Event() {
        super();
        targets = new ArrayList<>();
    }

    //@formatter:off
    public Event(final String name, final String from, final String until, final String provider,
                 final List<ProcessorModel> processors, final List<TargetConfig> targets,
                 final String scheduler, final String mapper, final List<AlertingModel> alertings) {
        //@formatter:on
        super(name, from, until, provider, processors, mapper, alertings);
        this.targets = targets == null ? null : Collections.unmodifiableList(targets);
        this.scheduler = scheduler;
    }

    @Override
    public GenericEvent cloneObj() {
        final List<ProcessorModel> cpProcessors = new ArrayList<>();
        getProcessors().ifPresent(cpProcessors::addAll);

        final List<AlertingModel> cpAlertings = new ArrayList<>();
        getAlertings().ifPresent(cpAlertings::addAll);

        List<TargetConfig> newTarget = null;
        if (targets != null) {
            newTarget = new ArrayList<>();
            for (final TargetConfig target : targets) {
                newTarget.add((TargetConfig) target.cloneObj());
            }
        }

        final String mapper   = getMapper().orElse(null);
        final String from     = getFrom().orElse(null);
        final String until    = getUntil().orElse(null);
        final String provider = getProvider().orElse(null);

        //@formatter:off
        final Event result = new Event(getName(), from, until, provider, cpProcessors, newTarget, scheduler, mapper, cpAlertings);
        //@formatter:on

        getFromFirstTime().ifPresent(result::buildFromFirstTime);

        return result;
    }

    // =========================================================================
    // GETTERS
    // =========================================================================
    public List<TargetConfig> getTargets() {
        List<TargetConfig> result = null;
        if (targets != null) {
            result = targets;
        }
        return result;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(final String scheduler) {
        this.scheduler = scheduler;
    }

}
