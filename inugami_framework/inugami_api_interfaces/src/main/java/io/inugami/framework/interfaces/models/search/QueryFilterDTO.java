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
package io.inugami.framework.interfaces.models.search;

import lombok.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class QueryFilterDTO<T> implements Serializable {
    private static final long                    serialVersionUID = 5248236412319733343L;
    @EqualsAndHashCode.Include
    @ToString.Include
    private              String                  field;
    private              List<T>                 values;
    private              T                       defaultValue;
    @EqualsAndHashCode.Include
    @ToString.Include
    private              MatchType               matchType;
    private              Class<? extends T>      valueType;
    private transient    Collection<Consumer<T>> validators;
}
