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
package io.inugami.interfaces.processors;


import io.inugami.interfaces.models.ClonableObject;
import io.inugami.interfaces.models.Config;
import io.inugami.interfaces.models.maven.ManifestInfo;
import lombok.*;

import java.util.List;
import java.util.Optional;

/**
 * PostProcessorModel
 *
 * @author patrick_guillerm
 * @since 6 oct. 2016
 */
//processor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProcessorModel implements ClonableObject<ProcessorModel>, ClassBehavior<ProcessorModel> {
    private static final long serialVersionUID = 8318093792064035460L;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String       name;
    @ToString.Include
    private String       className;
    private List<Config> configs;
    private ManifestInfo manifest;


    @Override
    public ProcessorModel cloneObj() {
        return null;
    }

    @Override
    public ProcessorModel build(final ClassBehavior behavior, final ConfigHandler<String, String> config) {
        final var builder = toBuilder();
        builder.configs(Optional.ofNullable(configs)
                                .orElse(List.of())
                                .stream()
                                .map(Config::cloneObj)
                                .toList());
        return builder.build();
    }
}
