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
package io.inugami.commons.connectors.listeners;

import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.monitoring.MdcService;
import io.inugami.commons.connectors.ConnectorListener;
import io.inugami.commons.connectors.HttpConnectorResult;
import io.inugami.commons.connectors.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class PartnerLogListener implements ConnectorListener {

    public static final String       REQUEST    = "request:";
    public static final String       RESPONSE   = "response:";
    public static final String       STATUS     = "\tstatus:";
    public static final String       MESSAGE    = "\tmessage:";
    public static final String       DELAY      = "\tdelay:";
    public static final String       HEADERS    = "\theaders:";
    public static final String       PAYLOAD    = "\tpayload:";
    public static final String       DOUBLE_TAB = "\t\t";
    public static final String       CLODE_VERB = "] ";
    public static final String       OPEN_VERB  = "[";
    public static final String       SEPARATOR  = ":";
    public static final String       MS         = "ms";
    public static final String       DATA       = "\tdata:";
    public static final String       EXCEPTION  = "exception: ";
    private static      List<String> TEXT_TYPE  = List.of("text", "json", "xml", "javascript", "typescript");

    // =========================================================================
    // API
    // =========================================================================
    public HttpRequest processCalling(final HttpRequest request, HttpConnectorResult connectorResult) {

        final JsonBuilder partnerlog = new JsonBuilder();
        partnerlog.write(OPEN_VERB).write(request.getVerb()).write(CLODE_VERB).write(request.getUrl()).line();
        partnerlog.write(REQUEST).line();

        if (connectorResult.getRequestHeaders() != null) {
            partnerlog.write(HEADERS).line();
            for (Map.Entry<String, String> header : connectorResult.getRequestHeaders().entrySet()) {
                partnerlog.write(DOUBLE_TAB).write(header.getKey()).write(SEPARATOR).write(header.getValue()).line();
            }
        }
        if (connectorResult.getBodyData() != null) {
            partnerlog.write(PAYLOAD).line();
            partnerlog.write(new String(connectorResult.getBodyData()));
        }

        MdcService.getInstance().lifecycleIn();
        Loggers.PARTNERLOG.info(partnerlog.toString());
        MdcService.getInstance().lifecycleRemove();
        return null;
    }


    public void onDone(final HttpConnectorResult stepResult) {
        MdcService.getInstance().lifecycleOut();
        Loggers.PARTNERLOG.info(renderPartnerLog(stepResult, null));
        MdcService.getInstance().lifecycleRemove();
    }

    public void onError(final HttpConnectorResult stepResult) {
        MdcService.getInstance().lifecycleOut();
        Loggers.PARTNERLOG.error(renderPartnerLog(stepResult, null));
        MdcService.getInstance().lifecycleRemove();
    }

    public void onError(final ConnectorException exception) {

        HttpConnectorResult result = null;
        if (exception.getResult() instanceof HttpConnectorResult) {
            result = (HttpConnectorResult) exception.getResult();
            MdcService.getInstance().lifecycleOut();
            Loggers.PARTNERLOG.error(renderPartnerLog(result, exception), exception);
            MdcService.getInstance().lifecycleRemove();
        }
        else {
            MdcService.getInstance().lifecycleOut();
            Loggers.PARTNERLOG.error(exception.getMessage(), exception);
            MdcService.getInstance().lifecycleRemove();
        }


    }


    private String renderPartnerLog(final HttpConnectorResult stepResult, final Exception error) {
        final JsonBuilder partnerlog = new JsonBuilder();
        partnerlog.write(OPEN_VERB).write(stepResult.getVerb()).write(CLODE_VERB).write(stepResult.getUrl()).line();
        partnerlog.write(REQUEST).line();

        if (stepResult.getRequestHeaders() != null && !stepResult.getRequestHeaders().isEmpty()) {
            partnerlog.write(HEADERS).line();
            for (Map.Entry<String, String> header : stepResult.getRequestHeaders().entrySet()) {
                partnerlog.write(DOUBLE_TAB).write(header.getKey()).write(SEPARATOR).write(header.getValue()).line();
            }
        }
        if (stepResult.getBodyData() != null) {
            partnerlog.write(PAYLOAD).line();
            partnerlog.write(new String(stepResult.getBodyData())).line();
        }

        partnerlog.write(RESPONSE).line();
        partnerlog.write(STATUS).write(stepResult.getStatusCode()).line();
        partnerlog.write(MESSAGE).write(stepResult.getMessage()).line();
        partnerlog.write(DELAY).write(stepResult.getDelais()).write(MS).line();
        if (error != null) {
            partnerlog.write(EXCEPTION).write(error).line();
        }

        if (stepResult.getResponseHeaders() != null && !stepResult.getResponseHeaders().isEmpty()) {
            partnerlog.write(HEADERS).line();
            for (Map.Entry<String, String> header : stepResult.getResponseHeaders().entrySet()) {
                partnerlog.write(DOUBLE_TAB).write(header.getKey()).write(SEPARATOR).write(header.getValue()).line();
            }
        }

        if (stepResult.getData() != null) {
            partnerlog.write(DATA).line();
            if (textType(stepResult.getContentType())) {
                partnerlog.write(new String(stepResult.getData())).line();
            }
            else {
                partnerlog.write(stepResult.getData()).line();
            }
        }
        return partnerlog.toString();
    }


    private boolean textType(final String contentType) {
        if (contentType == null) {
            return false;
        }
        final String mimeType = contentType.toLowerCase();
        for (String type : TEXT_TYPE) {
            if (mimeType.contains(type)) {
                return true;
            }
        }
        return false;
    }

}
