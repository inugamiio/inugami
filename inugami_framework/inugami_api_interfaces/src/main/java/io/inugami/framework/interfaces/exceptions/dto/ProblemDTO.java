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
package io.inugami.framework.interfaces.exceptions.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://datatracker.ietf.org/doc/rfc9457/
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Setter
@Getter
public class ProblemDTO implements Serializable {
    private static final long                serialVersionUID = 3488896803775818031L;
    public static final  String              FIELD            = "field";
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String              type;
    @ToString.Include
    private              String              message;
    @ToString.Include
    private              String              localizedMessage;
    @EqualsAndHashCode.Include
    private              Integer             status;
    private              String              reasonPhrase;
    private              String              detail;
    private              String              instance;
    private              LocalDateTime       timestamp;
    private              ProblemDTO          cause;
    private              Map<String, Object> details;

    @Singular("errors")
    private Collection<ProblemErrorDTO>     errors;
    @Singular("parameters")
    private Collection<ProblemParameterDTO> parameters;

    public static class ProblemDTOBuilder {
        public ProblemDTOBuilder withField(final Object value) {
            initDetails();
            if (value != null) {
                details.put(FIELD, value);
            }
            return this;
        }

        public ProblemDTOBuilder with(final String key, final Object value) {
            initDetails();
            if (key != null && value != null) {
                details.put(key, value);
            }

            return this;
        }

        private void initDetails() {
            if (details == null) {
                details = new LinkedHashMap<>();
            }
        }
    }
}
