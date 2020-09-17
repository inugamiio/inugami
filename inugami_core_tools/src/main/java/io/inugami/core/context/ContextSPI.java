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
package io.inugami.core.context;

import io.inugami.api.handlers.Handler;
import io.inugami.api.providers.Provider;
import io.inugami.commons.threads.ThreadsExecutorService;
import io.inugami.core.services.cache.CacheService;
import io.inugami.core.services.connectors.HttpConnector;

/**
 * ContextWithCache
 * 
 * @author patrick_guillerm
 * @since 11 avr. 2018
 */
public interface ContextSPI {
    ContextSPI initializeStandalone();
    
    CacheService getCache();
    
    boolean isVerbose();
    
    ThreadsExecutorService getThreadsExecutor(String name, int maxThreads);
    
    HttpConnector getHttpConnector(final String name, final int maxConnections, final int timeout, final int ttl,
                                   final int maxPerRoute, final int socketTimeout);
    
    HttpConnector getHttpConnector(String name, int maxConnections, int timeout, int ttl, int maxPerRoute);
    
    Provider getProvider(String providerName);
    
    <T extends Handler> T getHandler(final String name);
    
}
