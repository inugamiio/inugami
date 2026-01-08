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
package io.inugami.framework.interfaces.monitoring.kpi;

import com.fasterxml.jackson.databind.JsonNode;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.data.ResponseData;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @since 2026-01-08
 */
@ToString(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class KpiExtractorContext {
    private RequestData   request;
    private ResponseData  response;
    private LocalDateTime now;
    private JsonNode      requestContent;
    private JsonNode      responseContent;
}
