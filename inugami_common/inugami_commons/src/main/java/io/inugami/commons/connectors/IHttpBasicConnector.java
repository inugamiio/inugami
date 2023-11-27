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
package io.inugami.commons.connectors;

import io.inugami.api.exceptions.services.ConnectorException;

public interface IHttpBasicConnector {

    HttpConnectorResult get(final HttpRequest request) throws ConnectorException;

    HttpConnectorResult post(final HttpRequest request) throws ConnectorException;

    HttpConnectorResult put(final HttpRequest request) throws ConnectorException;

    HttpConnectorResult patch(final HttpRequest request) throws ConnectorException;

    HttpConnectorResult delete(final HttpRequest request) throws ConnectorException;

    HttpConnectorResult option(final HttpRequest request) throws ConnectorException;

    void close();


}
