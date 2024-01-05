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
package io.inugami.framework.interfaces.models.basic;

import lombok.*;

/**
 * StringJson
 *
 * @author patrick_guillerm
 * @since 23 janv. 2017
 */
@ToString
@EqualsAndHashCode
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class StringJson implements Dto<StringJson> {

    private static final long serialVersionUID = -974633377401939295L;

    private String value;

    @Override
    public StringJson cloneObj() {
        return toBuilder().build();
    }
}
