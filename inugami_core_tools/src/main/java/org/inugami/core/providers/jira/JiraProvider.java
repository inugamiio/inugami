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
package org.inugami.core.providers.jira;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.services.ProviderException;
import org.inugami.api.models.Gav;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ClassBehavior;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.AbstractProvider;
import org.inugami.api.providers.Provider;
import org.inugami.api.providers.ProviderRunner;
import org.inugami.api.providers.ProviderWithHttpConnector;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.concurrent.FutureDataBuilder;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.api.providers.task.ProviderTask;
import org.inugami.commons.spi.SpiLoader;
import org.inugami.core.context.ContextSPI;
import org.inugami.core.providers.jira.models.JiraIssue;
import org.inugami.core.providers.jira.models.JiraSearch;
import org.inugami.core.services.connectors.HttpConnector;

public class JiraProvider extends AbstractProvider implements Provider, ProviderWithHttpConnector {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String                    TYPE               = "JIRA";
    
    private static final String                    CONFIG_JIRA_HOST   = "host.jira";
    
    private static final String                    CONFIG_REALM       = "realm";
    
    private static final String                    CONFIG_MAX_RESULTS = "maxResults";
    
    private final FutureData<ProviderFutureResult> futureDataRef;
    
    private final String                           url;
    
    private final int                              timeout;
    
    private final ConfigHandler<String, String>    config;
    
    private final HttpConnector                    httpConnector;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public JiraProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                        final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        this.config = config;
        
        final StringBuilder builder = new StringBuilder();
        builder.append(config.grab(CONFIG_REALM));
        builder.append("://");
        builder.append(config.grab(CONFIG_JIRA_HOST));
        builder.append("/rest/api/2/search");
        url = builder.toString();
        
        timeout = Integer.parseInt(config.optionnal().grabOrDefault(CONFIG_TIMEOUT, "5000"));
        
        final ContextSPI ctx = new SpiLoader().loadSpiSingleService(ContextSPI.class);
        httpConnector = ctx.getHttpConnector(classBehavior.getName(), getMaxConnections(config, 5),
                                             getTimeout(config, 6000), getTTL(config, 50), getMaxPerRoute(config, 10),
                                             getSocketTimeout(config, 60000));
        
        futureDataRef = FutureDataBuilder.buildDefaultFuture(timeout);
    }
    
    @Override
    public String getType() {
        return TYPE;
    }
    
    // =========================================================================
    // CALL EVENT
    // =========================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        final ProviderTask providerTask = new JiraProviderTask(event, url, getName(), httpConnector, config, pluginGav);
        return runTask(providerTask, event, futureDataRef);
    }
    
    // =========================================================================
    // AGGREGATE
    // =========================================================================
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        assertDataType(data);
        final List<JiraIssue> issues = new ArrayList<>();
        
        data.stream().filter(this::validResultWithData).map(this::toJiraSearch).map(JiraSearch::getIssues).distinct().forEach(issues::addAll);
        
        final JiraSearch jiraData = new JiraSearch(0, Integer.parseInt(config.grab(CONFIG_MAX_RESULTS)), issues.size(),
                                                   issues);
        return new ProviderFutureResultBuilder().addData(jiraData).build();
    }
    
    private JiraSearch toJiraSearch(final ProviderFutureResult item) {
        return (JiraSearch) item.getData().orElse(null);
    }
    
    private void assertDataType(final List<ProviderFutureResult> datas) throws ProviderException {
        final Optional<ProviderFutureResult> anInvalidResult = datas.stream().filter(this::validResultWithData).filter(this::notJiraSearch).findAny();
        if (anInvalidResult.isPresent()) {
            throw new ProviderException("can't aggregate unknow data type : {0}",
                                        anInvalidResult.get().getData().getClass().getName());
        }
    }
    
    private boolean validResultWithData(final ProviderFutureResult item) {
        return !item.getException().isPresent() && item.getData().isPresent();
    }
    
    private boolean notJiraSearch(final ProviderFutureResult item) {
        Asserts.isTrue(item.getData().isPresent());
        return !(item.getData().get() instanceof JiraSearch);
    }
}
