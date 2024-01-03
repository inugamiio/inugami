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
package io.inugami.interfaces.task;

import io.inugami.interfaces.alertings.AlertingResult;
import io.inugami.interfaces.models.basic.JsonObject;
import io.inugami.interfaces.models.event.GenericEvent;
import lombok.*;

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
public class ProviderFutureResult implements JsonObject {

    private static final long   serialVersionUID = -3593233046062751124L;
    public static final  String DATA_FIELD       = "data";

    private String               message;
    private Exception            exception;
    private String               scheduler;
    private JsonObject           data;
    @ToString.Include
    @EqualsAndHashCode.Include
    private GenericEvent         event;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String               channel;
    @ToString.Include
    private String               fieldData;
    private List<AlertingResult> alerts;
}
