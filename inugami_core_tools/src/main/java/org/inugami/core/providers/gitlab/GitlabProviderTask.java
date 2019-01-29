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
package org.inugami.core.providers.gitlab;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.services.ConnectorException;
import org.inugami.api.exceptions.services.ProviderException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.Gav;
import org.inugami.api.models.Tuple;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.api.providers.task.ProviderTask;
import org.inugami.commons.threads.RunAndCloseService;
import org.inugami.configuration.services.ConfigHandlerHashMap;
import org.inugami.core.providers.gitlab.models.GitlabMergeRequest;
import org.inugami.core.providers.gitlab.models.GitlabMergeRequests;
import org.inugami.core.providers.gitlab.models.QueryJson;
import org.inugami.core.services.connectors.HttpConnector;

import flexjson.JSONDeserializer;
import flexjson.JSONException;

public class GitlabProviderTask implements ProviderTask {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String                 CONFIG_API_TOKEN   = "privateToken";
    
    private static final String                 CONFIG_MERGE_STATE = "mergeState";
    
    private static final String                 CONFIG_TIME_LIMIT  = "timeLimit";
    
    private final HttpConnector                 httpConnector;
    
    private final SimpleEvent                   event;
    
    private final String                        url;
    
    private final String                        providerName;
    
    private final Gav                           pluginGav;
    
    private final ConfigHandler<String, String> config;
    
    private final int                           timeout;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public <T extends SimpleEvent> GitlabProviderTask(final T event, final String url, final String providerName,
                                                      final HttpConnector httpConnector,
                                                      final ConfigHandler<String, String> config, final Gav pluginGav,
                                                      final int timeout) {
        
        this.url = url;
        this.event = event;
        this.providerName = providerName;
        this.httpConnector = httpConnector;
        this.pluginGav = pluginGav;
        this.config = config == null ? new ConfigHandlerHashMap(new HashMap<>()) : config;
        this.timeout = timeout;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public ProviderFutureResult callProvider() {
        ProviderFutureResult result;
        try {
            result = process();
        }
        catch (final Exception e) {
            result = new ProviderFutureResultBuilder().addException(e).build();
        }
        
        return result;
    }
    
    @Override
    public GenericEvent getEvent() {
        return event;
    }
    
    @Override
    public Gav getPluginGav() {
        return pluginGav;
    }
    
    // =========================================================================
    // PROCESS
    // =========================================================================
    private ProviderFutureResult process() throws ProviderException {
        final List<QueryJson> queryList = queryToObject(event.getQuery());
        final List<String> requests = buildRequests(queryList);
        final List<GitlabMergeRequests> allData = this.processCallRequests(requests, queryList);
        
        return this.aggregateOrder(allData, queryList);
    }
    
    private List<GitlabMergeRequests> processCallRequests(final List<String> requests,
                                                          final List<QueryJson> queryList) {
        final List<Callable<GitlabMergeRequests>> tasks = new ArrayList<>();
        requests.forEach(request -> tasks.add(new GitlabProjectTask(request, httpConnector, queryList)));
        
        return new RunAndCloseService<>("gitlabProviderTask", timeout, requests.size(), tasks).run();
    }
    
    // =========================================================================
    // BUILDERS
    // =========================================================================
    private List<String> buildUrls(final List<QueryJson> queryList) {
        Asserts.notNull(queryList);
        final List<String> result = new ArrayList<>();
        queryList.forEach(query -> this.buildUrl(query, result));
        
        return result;
    }
    
    protected void buildUrl(final QueryJson queryObject, final List<String> result) {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.url);
        builder.append(queryObject.getId());
        builder.append("/merge_requests");
        result.add(builder.toString());
    }
    
    protected List<String> buildRequests(final List<QueryJson> queryList) throws ProviderException {
        final List<String> requestsUrls = buildUrls(queryList);
        final List<String> result = new ArrayList<>();
        
        final List<Tuple<String, String>> params = new ArrayList<>();
        params.add(new Tuple<>("state", config.grabOrDefault(CONFIG_MERGE_STATE, "all")));
        params.add(new Tuple<>("private_token", config.grab(CONFIG_API_TOKEN)));
        
        for (final String requestUrl : requestsUrls) {
            try {
                result.add(httpConnector.buildRequest(requestUrl, params));
            }
            catch (final ConnectorException e) {
                throw new GitlabProviderTaskException(e.getMessage(), e);
            }
        }
        
        return result;
    }
    
    // =========================================================================
    // Other
    // =========================================================================
    private ProviderFutureResult aggregateOrder(final List<GitlabMergeRequests> allData,
                                                final List<QueryJson> queryList) {
        final List<GitlabMergeRequest> resultData = new LinkedList<>();
        final int mergeTimeLimit = Integer.parseInt(config.grabOrDefault(CONFIG_TIME_LIMIT, "0"));
        
        queryList.forEach(query -> filterToSortData(query.getId(), resultData, allData));
        
        if (mergeTimeLimit > 0) {
            resultData.removeIf(mergeRequest -> filterDate(mergeRequest, mergeTimeLimit));
        }
        
        final GitlabMergeRequests gitlabData = new GitlabMergeRequests(resultData);
        return new ProviderFutureResultBuilder().addData(gitlabData).build();
    }
    
    /**
     * @param mergeRequest: Merge request object
     * @param timeLimit: In hours, the time that needs to have passed to show
     *            the merge request
     * @return if we remove or not the merge request
     */
    boolean filterDate(final GitlabMergeRequest mergeRequest, final int timeLimit) {
        final LocalDateTime currentDateTime = LocalDateTime.now().minusHours(timeLimit);
        final String mergeDateTimeFormatted = mergeRequest.getCreationDate().substring(0,
                                                                                       mergeRequest.getCreationDate().length()
                                                                                          - 1);
        final LocalDateTime mergeDateTime = LocalDateTime.parse(mergeDateTimeFormatted);
        
        return mergeDateTime.isAfter(currentDateTime);
    }
    
    /**
     * Sort the merge requests according to the Query order. This function
     * return the merge requests corresponding to the @queryId
     * 
     * @param queryId queryId of the merge request to keep
     * @param result merge requests list to fill
     * @param data data to sort
     */
    protected void filterToSortData(final int queryId, final List<GitlabMergeRequest> result,
                                    final List<GitlabMergeRequests> data) {
        //@formatter:off
        data.stream()
             .map(GitlabMergeRequests::getMergeRequests)
             .flatMap(Collection::stream)
             .filter(mergeRequest -> mergeRequest.getProjectId() == queryId)
             .forEach(result::add);
        //@formatter:on
    }
    
    protected List<QueryJson> queryToObject(final String query) {
        Asserts.isFalse(query.isEmpty());
        List<QueryJson> result = null;
        
        try {
            result = new JSONDeserializer<List<QueryJson>>().use(null,
                                                                 ArrayList.class).use("values",
                                                                                      QueryJson.class).deserialize(query);
            
        }
        catch (final JSONException error) {
            Loggers.DEBUG.error("{} : \n payload:\n----------\n{}\n----------\n", error.getMessage(), query);
            Loggers.PROVIDER.error(error.getMessage());
        }
        
        return result;
    }
    
    // =========================================================================
    // Exception
    // =========================================================================
    public class GitlabProviderTaskException extends ProviderException {
        
        private static final long serialVersionUID = 276504864026361026L;
        
        public GitlabProviderTaskException(final String message, final Object... values) {
            super(message, values);
        }
        
        public GitlabProviderTaskException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
