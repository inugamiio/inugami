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

import java.util.List;
import java.util.Optional;

/**
 * JsonObjects
 *
 * @author patrick_guillerm
 * @since 15 mai 2017
 */
@SuppressWarnings({"java:S119"})
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class JsonObjects<TYPE extends Dto> implements Dto<JsonObjects<TYPE>> {

    private static final long serialVersionUID = 2669600647907300725L;

    private List<TYPE> data;


    @Override
    public JsonObjects<TYPE> cloneObj() {
        final var builder = toBuilder();
        builder.data(Optional.ofNullable(data)
                             .orElse(List.of())
                             .stream()
                             .map(TYPE::cloneObj)
                             .map(item -> (TYPE) item)
                             .toList());
        return builder.build();

    }

}
