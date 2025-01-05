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
package io.inugami.framework.configuration.models;

import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.maven.Gav;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * ConfigurationData
 *
 * @author patrick_guillerm
 * @since 4 oct. 2016
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventConfig implements Serializable {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long              serialVersionUID = -1501568308837189494L;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              Gav               gav;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String            name;
    @ToString.Include
    private              String            scheduler;
    @ToString.Include
    private              Boolean           enable;
    @ToString.Include
    private              String            configFile;
    @Singular("events")
    private              List<Event>       events;
    @Singular("simpleEvents")
    private              List<SimpleEvent> simpleEvents;
}
