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
package io.inugami.core.alertings.senders.email.sender;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Email
 *
 * @author patrick_guillerm
 * @since 16 mars 2018
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmailModel implements Serializable {

    private static final long serialVersionUID = -1001114973632946955L;

    private String       from;
    private String       fromName;
    @ToString.Include
    @EqualsAndHashCode.Include
    private List<String> to;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String       subject;
    private String       body;
    private Date         date;


    public static class EmailModelBuilder {

        public EmailModelBuilder addTo(final String value) {
            if (this.to == null) {
                this.to = new ArrayList<>();
            }
            if (value != null) {
                this.to.add(value);
            }
            return this;
        }
    }
}
