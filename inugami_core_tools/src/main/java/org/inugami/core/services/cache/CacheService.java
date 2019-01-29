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
package org.inugami.core.services.cache;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;
import org.ehcache.xml.XmlConfiguration;
import org.inugami.api.loggers.Loggers;

/**
 * CacheService
 * 
 * @author patrick_guillerm
 * @since 17 janv. 2017
 */
public class CacheService {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Map<CacheTypes, Cache> caches = new HashMap<CacheTypes, Cache>();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CacheService() {
        this(null);
    }
    
    private CacheService(final URL cacheConfig) {
        final URL url = cacheConfig == null ? this.getClass().getResource("/META-INF/ehcache.xml") : cacheConfig;
        final Configuration xmlConfig = new XmlConfiguration(url);
        final CacheManager manager = CacheManagerBuilder.newCacheManager(xmlConfig);
        manager.init();
        
        for (final CacheTypes type : CacheTypes.values()) {
            final Cache<String, Serializable> cache = manager.getCache(type.name(), String.class, Serializable.class);
            
            caches.put(type, cache);
        }
        
    }
    
    // =========================================================================
    // DELEGATES
    // =========================================================================
    public List<String> getKeys(final CacheTypes type) {
        final List<String> result = new ArrayList<>();
        getCache(type).forEach(item -> result.add(item.getKey()));
        return result;
    }
    
    public void forEach(final CacheTypes type, final Consumer action) {
        getCache(type).forEach(action);
    }
    
    public <T extends Serializable> T get(final CacheTypes type, final String key) throws CacheLoadingException {
        return (T) getCache(type).get(key);
    }
    
    public void put(final CacheTypes type, final String key, final Serializable value) throws CacheWritingException {
        Loggers.CACHE.debug("[{}]push to cache : {}", type, key);
        getCache(type).put(key, value);
    }
    
    public boolean containsKey(final CacheTypes type, final String key) {
        return getCache(type).containsKey(key);
    }
    
    public synchronized void remove(final CacheTypes type, final String key) throws CacheWritingException {
        Loggers.CACHE.debug("[{}]remove from cache : {}", type, key);
        getCache(type).remove(key);
    }
    
    public Map<String, Serializable> getAll(final CacheTypes type,
                                            final Set<String> keys) throws BulkCacheLoadingException {
        return getCache(type).getAll(keys);
    }
    
    public synchronized void putAll(final CacheTypes type,
                                    final Map<String, Serializable> entries) throws BulkCacheWritingException {
        getCache(type).putAll(entries);
    }
    
    public synchronized void removeAll(final CacheTypes type, final Set<String> keys) throws BulkCacheWritingException {
        Loggers.CACHE.debug("[{}]remove all");
        getCache(type).removeAll(keys);
    }
    
    public synchronized void clear() {
        Loggers.CACHE.debug("clear all");
        for (final CacheTypes type : CacheTypes.values()) {
            getCache(type).clear();
        }
    }
    
    public synchronized void clear(final CacheTypes type) {
        Loggers.CACHE.debug("[{}] clear all", type);
        getCache(type).clear();
    }
    
    public <T extends Serializable> T putIfAbsent(final CacheTypes type, final String key,
                                                  final Serializable value) throws CacheLoadingException,
                                                                            CacheWritingException {
        return (T) getCache(type).putIfAbsent(key, value);
    }
    
    public Cache<String, Serializable> getCache(final CacheTypes type) {
        return caches.get(type);
    }
    
    public List<CacheTypes> cacheTypes() {
        final List<CacheTypes> result = caches.keySet().stream().collect(Collectors.toList());
        result.sort((ref, value) -> ref.compareTo(value));
        return result;
    }
    
    public List<Cache> getAllCaches() {
        final List<Cache> result = new ArrayList<>();
        
        for (final CacheTypes item : cacheTypes()) {
            result.add(caches.get(item));
        }
        return result;
    }
    
    public Map<CacheTypes, Cache> getCaches() {
        final Map<CacheTypes, Cache> result = new HashMap<>();
        result.putAll(caches);
        return result;
    }
}
