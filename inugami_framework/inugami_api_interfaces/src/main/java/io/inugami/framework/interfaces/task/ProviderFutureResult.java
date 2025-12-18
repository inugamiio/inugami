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
package io.inugami.framework.interfaces.task;

import io.inugami.framework.interfaces.alertings.AlertingResult;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * ProviderFutureResultModel
 *
 * @author patrick_guillerm
 * @since 9 janv. 2017
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProviderFutureResult implements Serializable {
    private static final long   serialVersionUID = -3593233046062751124L;
    public static final  String DATA_FIELD       = "data";

    private String               message;
    private Exception            exception;
    private String               scheduler;
    @Singular("data")
    private List<Serializable>   data;
    @ToString.Include
    @EqualsAndHashCode.Include
    private GenericEvent         event;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String               channel;
    @ToString.Include
    private String               fieldData;
    @Singular("alerts")
    private List<AlertingResult> alerts;
}
