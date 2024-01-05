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
package io.inugami.framework.interfaces.processors;


import io.inugami.framework.interfaces.models.Config;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * ClassBehavior
 *
 * @author patrick_guillerm
 * @since 26 déc. 2016
 */
public interface ClassBehavior<T> extends Serializable, ClassBehaviorParametersSPI<T> {


    List<Config> getConfigs();

    void setConfigs(final List<Config> configs);

    default Optional<String> getConfig(final String key) {
        Objects.requireNonNull(key);
        Optional<String> result = Optional.empty();
        if (getConfigs() != null) {
            for (final Config conf : getConfigs()) {
                if (key.equals(conf.getKey())) {
                    result = Optional.of(conf.getValue());
                    break;
                }
            }
        }
        return result;
    }

    String getName();

    void setName(final String name);

    String getClassName();

    void setClassName(final String className);

    ManifestInfo getManifest();

    void setManifest(final ManifestInfo manifest);

    @Override
    default boolean accept(final Class<?> clazz) {
        return clazz.isAssignableFrom(this.getClass());
    }

}
