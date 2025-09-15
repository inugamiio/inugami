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
package io.inugami.framework.interfaces.alertings;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.models.ClonableObject;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.processors.ClassBehavior;
import lombok.*;

import java.util.Map;

/**
 * AlertingModel
 *
 * @author patrick_guillerm
 * @since 20 déc. 2017
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AlertingProviderModel implements ClassBehavior<AlertingProviderModel>, ClonableObject<AlertingProviderModel> {

    private static final long                serialVersionUID = 6084938077729235541L;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String              name;
    @ToString.Include
    private              String              className;
    private              Map<String, String> configs;
    private              ManifestInfo        manifest;


    @Override
    public AlertingProviderModel build(final ClassBehavior behavior, final ConfigHandler<String, String> config) {
        final var builder = AlertingProviderModel.builder();
        builder.name(behavior.getName());
        builder.className(behavior.getClassName());
        builder.className(behavior.getClassName());
        builder.configs(behavior.getConfigs());
        builder.manifest(behavior.getManifest());
        return builder.build();
    }

    @Override
    public AlertingProviderModel cloneObj() {
        return toBuilder()
                .configs(cloneConfig())
                .build();
    }
}
