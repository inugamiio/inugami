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
import io.inugami.interfaces.models.ClonableObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * GenericEvent
 *
 * @author patrick_guillerm
 * @since 6 oct. 2016
 */
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GenericEvent implements Serializable, ClonableObject<GenericEvent> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -355683229478830003L;


    @EqualsAndHashCode.Include
    private   String               name;
    protected String               fromFirstTime;
    protected String               from;
    private   String               until;
    private   String               provider;
    private   String               mapper;
    private   List<ProcessorModel> processors;
    private   List<AlertingModel>  alertings;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================


    public GenericEvent(final String name) {
        this(name, null, null, null, null, null, null);
    }

    public GenericEvent(final String name, final String from, final String unitl, final String provider,
                        final List<ProcessorModel> processors, final String mapper,
                        final List<AlertingModel> alertings) {
        super();
        this.name = name;
        this.from = from;
        this.provider = provider;
        this.processors = processors == null ? null : Collections.unmodifiableList(processors);
        this.alertings = alertings == null ? null : Collections.unmodifiableList(alertings);
        until = unitl;
        this.mapper = mapper;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        //@formatter:off
        return new StringBuilder(this.getClass().getSimpleName())
                .append('@')
                .append(System.identityHashCode(this))
                .append('[')
                .append("name=").append(name)
                .append(", from=").append(from)
                .append(", provider=").append(provider)
                .append(", processors=").append(processors)
                .append(']')
                .toString();
        //@formatter:on
    }


    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================


    public Optional<String> getFrom() {
        return Optional.ofNullable(from);
    }

    public Optional<String> getProvider() {
        return Optional.ofNullable(provider);
    }

    public Optional<List<ProcessorModel>> getProcessors() {
        return Optional.ofNullable(processors);
    }

    public Optional<String> getUntil() {
        return Optional.ofNullable(until);
    }

    public Optional<String> getFromFirstTime() {
        return Optional.ofNullable(fromFirstTime);
    }

    public void buildFrom(final String from) {
        this.from = from;
    }

    public void buildUntil(final String until) {
        this.until = until;
    }

    public void buildProvider(final String provider) {
        this.provider = provider;
    }

    public void buildFromFirstTime(final String fromFirstTime) {
        this.fromFirstTime = fromFirstTime;
    }

    public Optional<String> getMapper() {
        return Optional.ofNullable(mapper);
    }

    public void buildMapper(final String mapper) {
        this.mapper = mapper;
    }

    public Optional<List<AlertingModel>> getAlertings() {
        return Optional.ofNullable(alertings);
    }

    @Override
    public GenericEvent cloneObj() {
        List<ProcessorModel> cpProcessors = null;
        if (processors != null) {
            cpProcessors = new ArrayList<>();
            cpProcessors.addAll(processors);
        }

        return new GenericEvent(name, from, until, provider, cpProcessors, mapper, alertings);
    }

}
