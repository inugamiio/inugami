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
import org.inugami.core.providers.gitlab.models.GitlabMergeRequest;
import org.inugami.core.providers.gitlab.models.GitlabMergeRequests;
import org.inugami.core.services.connectors.HttpConnector;

public class GitlabProvider extends AbstractProvider implements Provider, ProviderWithHttpConnector {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String                    TYPE               = "GITLAB";
    
    private static final String                    CONFIG_GITLAB_HOST = "host.gitlab";
    
    private static final String                    CONFIG_REALM       = "realm";
    
    private static final String                    CONFIG_API_VERSION = "apiVersion";
    
    private final FutureData<ProviderFutureResult> futureDataRef;
    
    private final String                           url;
    
    private final int                              timeout;
    
    private final ConfigHandler<String, String>    config;
    
    private final HttpConnector                    httpConnector;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public GitlabProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                          final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        this.config = config;
        
        final StringBuilder builder = new StringBuilder();
        builder.append(config.grab(CONFIG_REALM));
        builder.append("://");
        builder.append(config.grab(CONFIG_GITLAB_HOST));
        builder.append("/api/");
        builder.append(config.grab(CONFIG_API_VERSION));
        builder.append("/projects/");
        url = builder.toString();
        
        timeout = Integer.parseInt(config.optionnal().grabOrDefault(CONFIG_TIMEOUT, "5000"));
        
        final ContextSPI ctx = new SpiLoader().loadSpiSingleService(ContextSPI.class);
        //@formatter:off
        httpConnector = ctx.getHttpConnector(classBehavior.getName(),
                                                  getMaxConnections(config,   5),
                                                  getTimeout(config,          6000),
                                                  getTTL(config,              50),
                                                  getMaxPerRoute(config,      10),
                                                  getSocketTimeout(config,    60000));
        //@formatter:off
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
        final ProviderTask providerTask = new GitlabProviderTask(event, url, getName(), httpConnector,
                config, pluginGav, timeout);
        return runTask(providerTask, event, futureDataRef);
    }

    // =========================================================================
    // AGGREGATE
    // =========================================================================
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        assertDataType(data);
        final List<GitlabMergeRequest> resultData = new ArrayList<>();
        //@formatter:off
        data.stream()
                .filter(this::validResultWithData)
                .map(this::toGitlabMergeRequests)
                .map(GitlabMergeRequests::getMergeRequests)
                .forEach(resultData::addAll);
        //@formatter:on
        
        final GitlabMergeRequests gitlabData = new GitlabMergeRequests(resultData);
        return new ProviderFutureResultBuilder().addData(gitlabData).build();
    }
    
    private GitlabMergeRequests toGitlabMergeRequests(final ProviderFutureResult item) {
        return (GitlabMergeRequests) item.getData().orElse(null);
    }
    
    private void assertDataType(final List<ProviderFutureResult> datas) throws ProviderException {
        //@formatter:off
        final Optional<ProviderFutureResult> anInvalidResult = datas.stream()
                                                            .filter(this::validResultWithData)
                                                            .filter(this::notGitlabMergeRequests)
                                                            .findAny();
        //@formatter:on
        
        if (anInvalidResult.isPresent()) {
            throw new ProviderException("can't aggregate unknow data type : {0}",
                                        anInvalidResult.get().getData().getClass().getName());
        }
    }
    
    private boolean validResultWithData(final ProviderFutureResult item) {
        return !item.getException().isPresent() && item.getData().isPresent();
    }
    
    private boolean notGitlabMergeRequests(final ProviderFutureResult item) {
        Asserts.isTrue(item.getData().isPresent());
        return !(item.getData().get() instanceof GitlabMergeRequests);
    }
}
