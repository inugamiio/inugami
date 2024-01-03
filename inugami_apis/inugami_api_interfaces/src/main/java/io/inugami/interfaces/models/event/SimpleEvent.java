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
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Event
 *
 * @author patrick_guillerm
 * @since 4 oct. 2016
 */
@SuppressWarnings({"java:S1185", "java:S2160"})
// simple-event
public class SimpleEvent extends GenericEvent {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -1139191592954226047L;

    private final String query;
    private final String parent;
    private       String scheduler;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SimpleEvent() {
        super();
        query = null;
        parent = null;
    }

    @Builder(builderMethodName = "simpleEventBuilder")
    @SuppressWarnings({"java:S107"})
    public SimpleEvent(final String name, final String from, final String until, final String provider,
                       final List<ProcessorModel> processors, final String query, final String parent,
                       final String scheduler, final String mapper, final List<AlertingModel> alertings) {
        super(name, from, until, provider, processors, mapper, alertings);
        this.query = query;
        this.parent = parent;
        this.scheduler = scheduler;
    }

    @Override
    public GenericEvent cloneObj() {
        final List<ProcessorModel> cpProcessors = new ArrayList<>();
        getProcessors().ifPresent(cpProcessors::addAll);

        final List<AlertingModel> cpAlertings = new ArrayList<>();
        getAlertings().ifPresent(cpAlertings::addAll);

        final String from     = getFrom().orElse(null);
        final String until    = getUntil().orElse(null);
        final String provider = getProvider().orElse(null);
        final String mapper   = getMapper().orElse(null);

        final SimpleEvent result = new SimpleEvent(getName(), from, until, provider, cpProcessors, query, parent,
                                                   scheduler, mapper, cpAlertings);

        getFromFirstTime().ifPresent(result::buildFromFirstTime);

        return result;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getQuery() {
        return query;
    }

    public Optional<String> getParent() {
        return parent == null ? Optional.empty() : Optional.of(parent);
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(final String scheduler) {
        this.scheduler = scheduler;
    }

    // =========================================================================
    // FOR MAPPING
    // =========================================================================
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Optional<String> getFrom() {
        return super.getFrom();
    }

    @Override
    public Optional<String> getProvider() {
        return super.getProvider();
    }

    @Override
    public Optional<List<ProcessorModel>> getProcessors() {
        return super.getProcessors();
    }

    @Override
    public Optional<String> getUntil() {
        return super.getUntil();
    }

    @Override
    public Optional<String> getFromFirstTime() {
        return super.getFromFirstTime();
    }

    @Override
    public void buildFrom(final String from) {
        super.buildFrom(from);
    }

    @Override
    public void buildUntil(final String until) {
        super.buildUntil(until);
    }

    @Override
    public void buildProvider(final String provider) {
        super.buildProvider(provider);
    }

    @Override
    public void buildFromFirstTime(final String fromFirstTime) {
        super.buildFromFirstTime(fromFirstTime);
    }

    @Override
    public Optional<String> getMapper() {
        return super.getMapper();
    }

    @Override
    public void buildMapper(final String mapper) {
        super.buildMapper(mapper);
    }

    @Override
    public Optional<List<AlertingModel>> getAlertings() {
        return super.getAlertings();
    }

}
