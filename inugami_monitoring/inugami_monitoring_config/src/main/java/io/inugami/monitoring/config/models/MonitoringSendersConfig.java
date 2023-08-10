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
package io.inugami.monitoring.config.models;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * SendersConfig
 *
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MonitoringSendersConfig implements Serializable {


    private static final long serialVersionUID = 1553608492775168744L;

    @XStreamImplicit
    private List<MonitoringSenderConfig> senders;
}
