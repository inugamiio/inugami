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
package io.inugami.framework.interfaces.connectors;

import com.fasterxml.jackson.core.type.TypeReference;
import io.inugami.framework.interfaces.connectors.exceptions.JsonProcessingException;
import io.inugami.framework.interfaces.connectors.exceptions.YamlProcessingException;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

public interface IHttpConnectorResult {

    String getVerb();

    void setVerb(final String value);

    String getUrl();

    void setUrl(final String value);

    String getHashHumanReadable();

    void setHashHumanReadable(final String value);

    String getJsonInputData();

    void getJsonInputData(final String value);

    int getStatusCode();

    void setStatusCode(final int value);

    String getMessage();

    void setMessage(final String value);

    byte[] getData();

    void setData(final byte[] value);

    byte[] getRequestData();

    void setRequestData(final byte[] value);

    int getLength();

    void setLength(final int value);

    String getContentType();

    void setContentType(final String value);

    long getResponseAt();

    void setResponseAt(final long value);

    long getDelay();

    void setDelay(final long value);

    String getEncoding();

    void getEncoding(final String value);

    Exception getError();

    void setError(final Exception value);

    Charset getCharset();

    void setCharset(final Charset value);

    Map<String, String> getRequestHeaders();

    void setRequestHeaders(final Map<String, String> value);

    Map<String, String> getResponseHeaders();

    void setResponseHeaders(final Map<String, String> value);

    <T> T getResponseBody(final Class<? extends T> objectClass) throws JsonProcessingException;

    <T> T getResponseBody(final TypeReference<T> refObjectType) throws JsonProcessingException;

    <T> T getBodyFromJson(final Class<? extends T> objectClass) throws JsonProcessingException;

    <T> T getBodyFromJson(final TypeReference<T> refObjectType) throws JsonProcessingException;

    <T> T getBodyFromYaml(final Class<? extends T> objectClass) throws YamlProcessingException;

    <T> T getBodyFromYaml(final TypeReference<T> refObjectType) throws YamlProcessingException;

    <T> T getResponseFromYaml(final Class<? extends T> objectClass) throws JsonProcessingException;

    <T> T getResponseFromYaml(final TypeReference<T> refObjectType) throws JsonProcessingException;

    Optional<String> getErrorCode();
}
