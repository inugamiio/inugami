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
package io.inugami.framework.interfaces.models;

import lombok.Builder;
import lombok.*;

import java.io.Serializable;

/**
 * Config
 *
 * @author patrick_guillerm
 * @since 22 déc. 2016
 */
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
// config
public final class Config implements Serializable, ClonableObject<Config> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2342544486726397745L;

    @EqualsAndHashCode.Include
    private String key;
    private String value;

    @Override
    public Config cloneObj() {
        return toBuilder().build();
    }
}
