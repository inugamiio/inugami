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
package io.inugami.data.commons;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;
import org.hibernate.jpa.boot.internal.PersistenceXmlParser;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.functionnals.ApplyIfNotNull;
import io.inugami.commons.files.FilesUtils;
import io.inugami.configuration.models.app.DataProviderModel;
import io.inugami.core.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DynamicPluginPersistenceProvider
 * 
 * @author patrick_guillerm
 * @since 15 janv. 2018
 */
public class DynamicPluginPersistenceProvider extends HibernatePersistenceProvider
        implements PersistenceProvider, ApplyIfNotNull {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger log = LoggerFactory.getLogger(PersistenceProvider.class);
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilderOrNull(final String persistenceUnitName,
                                                                               final Map properties,
                                                                               final ClassLoader providedClassLoader) {
        log.trace("Attempting to obtain correct EntityManagerFactoryBuilder for persistenceUnitName : {}",
                  persistenceUnitName);
        
        final Map<String, Object> integration = wrap(properties);
        final List<ParsedPersistenceXmlDescriptor> units;
        try {
            units = PersistenceXmlParser.locatePersistenceUnits(integration);
        }
        catch (final Exception e) {
            log.debug("Unable to locate persistence units", e);
            throw new PersistenceException("Unable to locate persistence units", e);
        }
        
        log.debug("Located and parsed {} persistence units; checking each", units.size());
        
        if (persistenceUnitName == null && units.size() > 1) {
            throw new PersistenceException("No name provided and multiple persistence units found");
        }
        
        for (final ParsedPersistenceXmlDescriptor persistenceUnit : units) {
            log.debug("Checking persistence-unit [name={}, explicit-provider={}] against incoming persistence unit name [{}]",
                      persistenceUnit.getName(), persistenceUnit.getProviderClassName(), persistenceUnitName);
            
            final boolean matches = persistenceUnitName == null
                                    || persistenceUnit.getName().equals(persistenceUnitName);
            if (!matches) {
                log.debug("Excluding from consideration due to name mis-match");
                continue;
            }
            
            return buildEntityManagerFactory(providedClassLoader, integration, persistenceUnit);
        }
        
        log.debug("Found no matching persistence units");
        return null;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private EntityManagerFactoryBuilder buildEntityManagerFactory(final ClassLoader providedClassLoader,
                                                                  final Map<String, Object> integration,
                                                                  final ParsedPersistenceXmlDescriptor persistenceUnit) {
        
        overrideAndAddProperties(persistenceUnit.getProperties());
        
        for (final Class<?> entity : DynamicPluginPersistenceCdi.getEntities()) {
            persistenceUnit.addClasses(entity.getName());
        }
        return getEntityManagerFactoryBuilder(persistenceUnit, integration, providedClassLoader);
    }
    
    private void overrideAndAddProperties(final Properties properties) {
        final String defaultConfig = FilesUtils.readFileFromClassLoader("/META-INF/javax.persistence.provider.properties");
        final Properties defaultProperties = readProperties(defaultConfig);
        
        for (final Map.Entry<Object, Object> entry : defaultProperties.entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }
        
        final DataProviderModel dataStorage = Context.getInstance().getApplicationConfiguration().getDataStorage();
        if (dataStorage != null) {
            //@formatter:off
            applyIfNotNull(dataStorage.getDriver(),   (value) -> properties.put("javax.persistence.jdbc.driver", value));
            applyIfNotNull(dataStorage.getDialect(),  (value) -> properties.put("hibernate.dialect", value));
            applyIfNotNull(dataStorage.getUrl(),      (value) -> properties.put("javax.persistence.jdbc.url", value));
            applyIfNotNull(dataStorage.getUser(),     (value) -> properties.put("javax.persistence.jdbc.user", value));
            applyIfNotNull(dataStorage.getPassword(), (value) -> properties.put("javax.persistence.jdbc.password", value));
            applyIfNotNull(dataStorage.getHbm2ddl(),  (value) -> properties.put("hibernate.hbm2ddl.auto", value));
            //@formatter:on
            
            if (dataStorage.isVerbose()) {
                properties.put("hibernate.show_sql", "true");
                properties.put("hibernate.format_sql", "true");
            }
        }
    }
    
    private Properties readProperties(final String defaultConfig) {
        final Properties result = new Properties();
        final Reader reader = new StringReader(defaultConfig);
        
        try {
            result.load(reader);
        }
        catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
        return result;
    }
    
}
