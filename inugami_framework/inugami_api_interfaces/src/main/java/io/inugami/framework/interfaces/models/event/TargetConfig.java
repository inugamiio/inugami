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
package io.inugami.framework.interfaces.models.event;


import io.inugami.framework.interfaces.processors.ProcessorModel;
import lombok.*;

import java.util.List;
import java.util.Optional;

/**
 * Target
 *
 * @author patrick_guillerm
 * @since 4 oct. 2016
 */
// target
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TargetConfig implements GenericEvent<TargetConfig> {


    private static final long                 serialVersionUID = -8813704499596910994L;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String               name;
    @ToString.Include
    private              String               fromFirstTime;
    @ToString.Include
    private              String               from;
    @ToString.Include
    private              String               until;
    @ToString.Include
    private              String               provider;
    @ToString.Include
    private              String               mapper;
    @Singular("processors")
    private              List<ProcessorModel> processors;
    @Singular("alertings")
    private              List<AlertingModel>  alertings;

    //------------------------------------------------------------------------------------------------------------------
    private String query;
    private String parent;
    private String scheduler;


    @Override
    public TargetConfig cloneObj() {
        final var builder = toBuilder();
        builder.processors(Optional.ofNullable(processors)
                                   .orElse(List.of())
                                   .stream()
                                   .map(ProcessorModel::cloneObj)
                                   .toList());

        builder.alertings(Optional.ofNullable(alertings)
                                  .orElse(List.of())
                                  .stream()
                                  .map(AlertingModel::cloneObj)
                                  .toList());
        return builder.build();
    }
}
