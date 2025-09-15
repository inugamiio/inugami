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
import io.inugami.framework.interfaces.models.basic.Dto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SseProviderResult
 *
 * @author patrick_guillerm
 * @since 24 janv. 2017
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings({"java:S3655", "java:S107"})
public class SseProviderResult implements Dto<SseProviderResult> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -1676324414315300420L;

    private String               chanel;
    private String               event;
    private String               message;
    private String               errorMessage;
    @Singular("alerts")
    private List<AlertingResult> alerts;
    private boolean              error;
    private List<Dto<?>>         values;
    private String               scheduler;


    @Override
    public SseProviderResult cloneObj() {
        final var builder = toBuilder();
        builder.alerts(Optional.ofNullable(alerts)
                               .orElse(List.of())
                               .stream()
                               .map(AlertingResult::cloneObj)
                               .toList());

        final List<Dto<?>> newValues = new ArrayList<>();
        if (values != null) {
            for (Dto<?> item : values) {
                newValues.add(item.cloneObj());
            }
        }

        return builder.build();
    }

}
