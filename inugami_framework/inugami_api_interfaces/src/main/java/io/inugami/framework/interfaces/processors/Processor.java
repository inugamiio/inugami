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

import io.inugami.framework.interfaces.exceptions.services.ProcessorException;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import io.inugami.framework.interfaces.tools.NamedComponent;

/**
 * In Inugami, when a provider retrieve the data, it can invoke a Processor to manipulate
 * and transform the data.
 * For example, if we have a SQL provider how retrieve information from a table,
 * the data should not be formatted directly as time serie data.
 * In this case the Processor will transform your entity to another format.
 *
 * @author patrick_guillerm
 * @since 6 oct. 2016
 */
public interface Processor extends NamedComponent {

    ProviderFutureResult process(final GenericEvent event, final ProviderFutureResult data) throws ProcessorException;

}
