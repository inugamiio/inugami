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
package io.inugami.monitoring.providers.els;


import io.inugami.framework.api.connectors.HttpBasicConnector;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.connectors.HttpRequest;
import io.inugami.framework.interfaces.dao.Identifiable;
import io.inugami.framework.interfaces.exceptions.services.ConnectorException;
import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * ElasticSearchWriterTask
 *
 * @author patrick_guillerm
 * @since 3 oct. 2018
 */
@Slf4j
@Builder
@RequiredArgsConstructor
@SuppressWarnings({"java:S1874"})
public class ElasticSearchWriterTask implements Callable<Void> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final HttpBasicConnector httpConnector;

    private final String       url;
    @Singular("values")
    private final List<Object> values;

    private final ElsData data;
    @Builder.Default
    private final String  bulkCommand = "index";


    // =========================================================================
    // METHODS
    // =========================================================================

    @Override
    public Void call() throws Exception {

        final String jsonBulk = renderBulk(data, values);

        try {
            if (jsonBulk != null && !jsonBulk.isEmpty()) {
                httpConnector.post(HttpRequest.builder()
                                              .url(url)
                                              .body(jsonBulk)
                                              .build());
                Loggers.PROVIDER.info("send {} documents to ELS {} : {}", values.size(), data.getIndex(),
                                      data.getType());
            }
        } catch (ConnectorException e) {
            Loggers.PROVIDER.error(e.getMessage());
            Loggers.DEBUG.error(e.getMessage(), e);
        }

        return null;
    }

    // =========================================================================
    // RENDER BULK
    // =========================================================================
    private String renderBulk(ElsData data, List<Object> values) {
        String result = null;

        if (data.getValues() != null) {
            final JsonBuilder json = new JsonBuilder();

            for (Object value : values) {
                json.write(encodeBulkAction(data, value)).addLine();
                final String jsonValue = convertToJson(value);
                if (jsonValue == null) {
                    continue;
                }
                switch (bulkCommand) {
                    case "index":
                        json.write(jsonValue).addLine();
                        break;
                    case "update":
                        json.openObject().addField("doc").write(jsonValue).closeObject().addLine();
                        break;
                    default:
                        break;
                }

            }

            result = json.toString();
        }
        return result;
    }

    private String convertToJson(final Object value) {
        try {
            return JsonMarshaller.getInstance().getDefaultObjectMapper().writeValueAsString(value);
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }

    private String encodeBulkAction(ElsData data, Object value) {
        final JsonBuilder json = new JsonBuilder();
        json.openObject();
        json.addField(bulkCommand);

        json.openObject();
        // data.getIndex().toLowerCase()
        json.addField("_index").valueQuot(data.getIndex()).addSeparator();
        json.addField("_type").valueQuot(data.getType());

        Serializable uid = null;
        if (value instanceof Identifiable) {
            uid = ((Identifiable<Serializable>) value).getUid();
        }
        if (uid != null) {
            json.addSeparator().addField("_id").valueQuot(uid);
        }
        json.closeObject();

        json.closeObject();
        return json.toString();
    }

    // =========================================================================
    // UPDATE DATE
    // =========================================================================
    public void updateData(List<? extends Object> data) {
        this.values.clear();
        if (data != null) {
            this.values.addAll(data);
        }
    }
}
