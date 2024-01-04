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
import lombok.*;

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
public class SseProviderResult implements JsonObject {

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
    private JsonObject           values;
    private String               scheduler;


    @Override
    public JsonObject cloneObj() {
        final var builder = toBuilder();
        builder.alerts(Optional.ofNullable(alerts)
                               .orElse(List.of())
                               .stream()
                               .map(AlertingResult::cloneObj)
                               .toList());
        return builder.build();
    }

}
