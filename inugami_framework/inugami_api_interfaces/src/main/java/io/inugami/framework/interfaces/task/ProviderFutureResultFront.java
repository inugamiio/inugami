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
import io.inugami.framework.interfaces.models.basic.Dto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ProviderFutureResultFront
 *
 * @author patrick_guillerm
 * @since 30 janv. 2018
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProviderFutureResultFront implements Dto<ProviderFutureResultFront> {

    private static final long serialVersionUID = 7227934237939401748L;

    private String               message;
    private String               channel;
    private Exception       exception;
    private GenericEvent<?> event;
    private String          cronExpression;
    private String               data;
    private List<AlertingResult> alerts = new ArrayList<>();


    @Override
    public ProviderFutureResultFront cloneObj() {
        final var builder = toBuilder();

        if (event != null) {
            builder.event((GenericEvent<?>) event.cloneObj());
        }

        builder.alerts(Optional.ofNullable(alerts)
                               .orElse(List.of())
                               .stream()
                               .map(AlertingResult::cloneObj)
                               .toList());
        return builder.build();
    }
}
