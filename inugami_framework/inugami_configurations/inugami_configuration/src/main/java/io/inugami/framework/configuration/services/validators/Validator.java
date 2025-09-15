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
package io.inugami.framework.configuration.services.validators;

import io.inugami.framework.configuration.exceptions.ConfigurationException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;


/**
 * Validator
 *
 * @author patrick_guillerm
 * @since 20 déc. 2017
 */
@FunctionalInterface
public interface Validator {

    void validate() throws ConfigurationException;

    default <T> List<Condition> validate(final T data, final BiConsumer<T, List<Condition>> validateFunction) {
        final List<Condition> result = new ArrayList<>();
        if (data != null) {
            validateFunction.accept(data, result);
        }
        return result;
    }
}
