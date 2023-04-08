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
package io.inugami.core.providers.gitlab;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.loggers.Loggers;
import io.inugami.commons.connectors.HttpConnectorResult;
import io.inugami.core.providers.gitlab.models.GitlabMergeRequests;
import io.inugami.core.providers.gitlab.models.QueryJson;
import io.inugami.core.services.connectors.HttpConnector;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Callable;

public class GitlabProjectTask implements Callable<GitlabMergeRequests> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String request;

    private final HttpConnector httpConnector;

    private final List<QueryJson> queryList;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public GitlabProjectTask(final String request, final HttpConnector httpConnector, final List<QueryJson> queryList) {
        this.request = request;
        this.httpConnector = httpConnector;
        this.queryList = queryList;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public GitlabMergeRequests call() throws Exception {
        HttpConnectorResult httpResult = null;
        if ((request == null) || request.isEmpty()) {
            Loggers.DEBUG.info("GitlabProjectTask: no request, nothing to do...");
        } else {
            httpResult = httpConnector.get(request);
        }

        return httpResult == null ? null : buildResult(httpResult);
    }

    // =========================================================================
    // BUILDERS
    // =========================================================================
    protected GitlabMergeRequests buildResult(final HttpConnectorResult httpResult) throws ProviderException {
        final GitlabMergeRequests result;

        if (httpResult.getStatusCode() == 200) {
            result = buildSuccessResult(httpResult.getData(), httpResult.getCharset());
        } else {
            throw new GitlabProjectTaskException("HTTP-{0} on calling {1}", httpResult.getStatusCode(),
                                                 httpResult.getHashHumanReadable());
        }

        return result;
    }

    private GitlabMergeRequests buildSuccessResult(final byte[] data, final Charset charset) {
        final GitlabMergeRequests result = new GitlabMergeRequests().convertToObject(data, charset);
        this.setProjectName(result);

        return result;
    }

    protected void setProjectName(final GitlabMergeRequests eventData) {
        Asserts.assertNotNull(eventData);
        final int projectId = Integer.parseInt(request.substring(request.indexOf("projects/") + 9,
                                                                 request.indexOf("/merge_requests")));
        //@formatter:off
        final QueryJson queryTask = queryList.stream()
                                             .filter(item -> item.getId() == projectId)
                                             .findFirst()
                                             .orElse(null);
        //@formatter:on

        if (queryTask != null) {
            final String projectName = queryTask.getProjectName();
            //@formatter:off
            eventData.getMergeRequests().stream()
                     .filter(mergeRequest -> mergeRequest.getProjectId() == projectId)
                     .forEach(mergeRequest -> mergeRequest.setProjectName(projectName));
            //@formatter:on
        }
    }

    // =========================================================================
    // Exception
    // =========================================================================
    public class GitlabProjectTaskException extends ProviderException {

        private static final long serialVersionUID = -8538310719669789707L;

        public GitlabProjectTaskException(final String message, final Object... values) {
            super(message, values);
        }

        public GitlabProjectTaskException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
