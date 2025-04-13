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
package io.inugami.monitoring.api.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class InterceptorContextDto implements Serializable {
    private static final long                      serialVersionUID = 6234669334424899059L;
    @EqualsAndHashCode.Include
    @ToString.Include
    private              String                    url;
    private              String                    contentType;
    @EqualsAndHashCode.Include
    @ToString.Include
    private              String                    verb;
    private              byte[]                    content;
    private              Map<String, String>       headers;
    private              Map<String, List<String>> options;
}
