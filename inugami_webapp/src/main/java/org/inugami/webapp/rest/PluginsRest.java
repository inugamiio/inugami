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
package org.inugami.webapp.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.inugami.api.exceptions.ProcessingRunningException;
import org.inugami.api.exceptions.services.MappingException;
import org.inugami.api.models.Gav;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.api.providers.task.ProviderFutureResultFront;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.configuration.models.plugins.front.MenuLink;
import org.inugami.configuration.services.mapping.PluginMapping;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.context.Context;
import org.inugami.core.security.commons.roles.UserConnected;
import org.inugami.core.services.cache.CacheService;
import org.inugami.core.services.cache.CacheTypes;
import org.inugami.core.services.sse.SseService;

/**
 * PluginRest
 * 
 * @author patrick_guillerm
 * @since 17 janv. 2017
 */
@Path("plugins")
public class PluginsRest {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static AtomicBoolean isProcessEventsDataRunning = new AtomicBoolean();
    
    @Inject
    private ApplicationContext         context;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @GET
    @UserConnected
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPlugins() throws MappingException {
        final List<Plugin> result = new ArrayList<>();
        Context.getInstance().getPlugins().ifPresent(plugins -> {
            result.addAll(Context.getInstance().getPlugins().get());
        });
        return new PluginMapping().marshalling(result);
    }
    
    /**
     * Allow to grab all plugins
     * 
     * @return all plugins contains in classpath
     */
    @GET
    @UserConnected
    @Path("menu-links")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MenuLink> getAllPluginsMenuLinks() {
        final List<MenuLink> routers = new ArrayList<>();
        
        //@formatter:off
        Context.getInstance().getPlugins().ifPresent(plugins->{
            plugins.stream()
                   .filter(plugin  -> plugin.getFrontConfig().isPresent())
                   .filter(plugin  -> plugin.getFrontConfig().get().getMenuLinks()!=null)
                   .forEach(plugin -> routers.addAll(plugin.getFrontConfig().get().getMenuLinks()));
        });
        //@formatter:on
        return routers;
    }
    
    @GET
    @Path("events-data")
    public List<ProviderFutureResultFront> eventsData(@QueryParam("gav")
    final String gav, @QueryParam("exclude")
    final String excludeRegex) throws ProcessingRunningException {
        
        final String key = buildCacheKey(gav, excludeRegex);
        
        final CacheService cache = context.getCache();
        List<ProviderFutureResultFront> result = cache.get(CacheTypes.TEN_SECOND, key);
        
        if (result == null) {
            allowToProcessEventsData();
            result = processEventsData(gav, excludeRegex, key, cache);
            
        }
        
        return result;
    }
    
    private synchronized List<ProviderFutureResultFront> processEventsData(final String gav, final String excludeRegex,
                                                                           final String key, final CacheService cache) {
        isProcessEventsDataRunning.set(true);
        List<ProviderFutureResultFront> result;
        result = new ArrayList<>();
        List<ProviderFutureResult> savedData = null;
        final Gav pluginGav = buildGav(gav);
        if (pluginGav != null) {
            savedData = context.processPluginEvents(pluginGav, excludeRegex);
        }
        if (savedData != null) {
            for (final ProviderFutureResult data : savedData) {
                result.add(new ProviderFutureResultBuilder(data).buildForFront());
                
            }
        }
        cache.put(CacheTypes.TEN_SECOND, key, (ArrayList) result);
        isProcessEventsDataRunning.set(false);
        return result;
    }
    
    private void allowToProcessEventsData() throws ProcessingRunningException {
        if (isProcessEventsDataRunning.get()) {
            throw new ProcessingRunningException();
        }
    }
    
    private String buildCacheKey(final String gav, final String excludeRegex) {
        return String.join("_", PluginsRest.class.getName(), gav, excludeRegex);
    }
    
    @GET
    @Path(SseService.ALL_PLUGINS_DATA)
    public List<ProviderFutureResultFront> allPluginData() {
        final List<ProviderFutureResultFront> result = new ArrayList<>();
        final List<ProviderFutureResult> savedData = context.processEvents();
        if (savedData != null) {
            for (final ProviderFutureResult data : savedData) {
                result.add(new ProviderFutureResultBuilder(data).buildForFront());
            }
        }
        return result == null ? new ArrayList<>() : result;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private Gav buildGav(final String gav) {
        Gav result = null;
        if (gav != null) {
            final String[] parts = gav.split(":");
            if (parts.length >= 3) {
                result = new Gav(parts);
            }
        }
        return result;
    }
}
