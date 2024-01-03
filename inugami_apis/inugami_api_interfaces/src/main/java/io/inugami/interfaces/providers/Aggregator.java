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
package io.inugami.interfaces.providers;

import io.inugami.interfaces.exceptions.services.ProviderException;
import io.inugami.interfaces.task.ProviderFutureResult;

import java.util.List;

/**
 * In Inugami, a lot of events are managed. Each event retrieves information from a provider.
 * Inugami has two event types:
 * <ul>
 *     <li>
 *         simple event : which only one query for a provider
 *     </li>
 *     <li>
 *         composite :  which multi queries on multi providers. However, to ensure coherent data,
 *         an <strong>Aggregator</strong> is required to aggreate results form each provider.
 *     </li>
 * </ul>
 *
 * @author patrick_guillerm
 * @since 29 mai 2017
 */
public interface Aggregator {

    ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException;
}
