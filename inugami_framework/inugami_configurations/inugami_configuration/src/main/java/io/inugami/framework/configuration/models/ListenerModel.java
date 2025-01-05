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
package io.inugami.framework.configuration.models;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.models.Config;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.processors.ClassBehavior;
import lombok.*;

import java.util.List;

/**
 * PostProcessorModel
 *
 * @author patrick_guillerm
 * @since 6 oct. 2016
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ListenerModel implements ClassBehavior<Object> {
    /**
     * The Constant serialVersionUID.
     */
    private static final long         serialVersionUID = -7163537432113412806L;
    private              String       name;
    private              List<Config> configs;
    private              String       className;
    private              ManifestInfo manifest;


    @Override
    public Object build(final ClassBehavior behavior, final ConfigHandler config) {
        return null;
    }
}
