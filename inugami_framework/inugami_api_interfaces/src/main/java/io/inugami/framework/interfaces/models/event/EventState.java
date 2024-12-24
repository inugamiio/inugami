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
package io.inugami.framework.interfaces.models.event;

import io.inugami.framework.interfaces.models.basic.Dto;
import lombok.*;

/**
 * EventState
 *
 * @author patrick_guillerm
 * @since 8 août 2017
 */
@ToString
@EqualsAndHashCode
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EventState implements Dto<EventState> {


    private static final long serialVersionUID = -5004046074596299588L;

    private String eventName;

    private long start;

    private long end;

    private boolean running;

    private long delais;


    @Override
    public EventState cloneObj() {
        return toBuilder().build();
    }
}
