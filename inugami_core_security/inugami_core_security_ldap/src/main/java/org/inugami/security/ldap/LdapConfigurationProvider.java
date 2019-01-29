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
package org.inugami.security.ldap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.inugami.api.exceptions.FatalException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.processors.Config;
import org.inugami.configuration.exceptions.FatalConfigurationException;
import org.inugami.configuration.models.app.SecurityConfiguration;
import org.inugami.core.context.ApplicationContext;
import org.inugami.security.ldap.mapper.DefaultLdapMapper;
import org.inugami.security.ldap.mapper.LdapMapper;

/**
 * LdapConfigurationProvider
 * 
 * @author patrick_guillerm
 * @since 19 déc. 2017  
 */
@ApplicationScoped
public class LdapConfigurationProvider {
    // =========================================================================
    // CONFIG KEYS
    // =========================================================================
    private static final String SECURITY_LDAP_TIMEOUT       = "security.ldap.timeout";
    
    private static final String SECURITY_LDAP_ROLE_SEARCH   = "security.ldap.roleSearch";
    
    private static final String SECURITY_LDAP_ROLE_BASE     = "security.ldap.roleBase";
    
    private static final String SECURITY_LDAP_SEARCH_FILTER = "security.ldap.searchFilter";
    
    private static final String SECURITY_LDAP_REFERRAL      = "security.ldap.referral";
    
    private static final String SECURITY_LDAP_URL           = "security.ldap.url";
    
    private static final String SECURITY_LDAP_ROOT_DN       = "security.ldap.rootDn";
    
    private static final String SECURITY_LDAP_DOMAIN        = "security.ldap.domain";
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @Inject
    private ApplicationContext    applicationCtx;
    
    private LdapConnectorData     connectorData = new LdapConnectorData();
    
    private SecurityConfiguration securityConfig;
    
    private LdapMapper            mapper;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @PostConstruct
    private void init() {
        final List<LdapMapper> mappers = applicationCtx.loadSpiService(LdapMapper.class, new DefaultLdapMapper());
        if (mappers.size() > 1) {
            throw new FatalException("more than one implementation of LdapMapper found! Please check your dependencies.");
        }
        mapper = mappers.get(0);
        
        Optional<SecurityConfiguration> configOpt = applicationCtx.getApplicationConfiguration().getSecurity("ldap");
        securityConfig = configOpt.orElseThrow(() -> new FatalConfigurationException("no configuration found for LDAP provider"));
        assertNotNull("LDAP configuration is mandatory!", securityConfig.getConfigs());
        assertNotNull("LDAP roles mappers is mandatory!", securityConfig.getRoles());
        assertFalse("LDAP roles mappers is mandatory!", securityConfig.getRoles().isEmpty());
        
        final Map<String, String> config = convertToMap(securityConfig.getConfigs());
        validateLdapConfiguration(config);
        
        //@formatter:off
        connectorData.setDomain(        getConfig(SECURITY_LDAP_DOMAIN,config))
                     .setRootDn(        getConfig(SECURITY_LDAP_ROOT_DN,config))
                     .setUrl(           getConfig(SECURITY_LDAP_URL,config))
                     .setReferral(      getConfig(SECURITY_LDAP_REFERRAL,config,"follow"))
                     .setSearchFilter(  getConfig(SECURITY_LDAP_SEARCH_FILTER,config))
                     .setRoleBase(      getConfig(SECURITY_LDAP_ROLE_BASE,config))
                     .setRoleSearch(    getConfig(SECURITY_LDAP_ROLE_SEARCH,config))
                     .setTimeout(       Long.parseLong(getConfig(SECURITY_LDAP_TIMEOUT,config,"5000")));
        //@formatter:on        
        
        Loggers.PROVIDER.debug("");
    }
    
    private void validateLdapConfiguration(final Map<String, String> config) {
        //@formatter:off
        assertNotEmpty(config.get(SECURITY_LDAP_DOMAIN)         ,"LDAP domain is mandatory, please configure : "+SECURITY_LDAP_DOMAIN);
        assertNotEmpty(config.get(SECURITY_LDAP_ROOT_DN)        ,"LDAP root DN is mandatory, please configure : "+SECURITY_LDAP_ROOT_DN);
        assertNotEmpty(config.get(SECURITY_LDAP_URL)            ,"LDAP url is mandatory, please configure : "+SECURITY_LDAP_URL);
        assertNotEmpty(config.get(SECURITY_LDAP_SEARCH_FILTER)  ,"LDAP search filter is mandatory, please configure : "+SECURITY_LDAP_SEARCH_FILTER);
        assertNotEmpty(config.get(SECURITY_LDAP_ROLE_BASE)      ,"LDAP role base is mandatory, please configure : "+SECURITY_LDAP_ROLE_BASE);
        assertNotEmpty(config.get(SECURITY_LDAP_ROLE_SEARCH)    ,"LDAP role search query is mandatory, please configure : "+SECURITY_LDAP_ROLE_SEARCH);
        //@formatter:on
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    // =========================================================================
    // EXCEPTION
    // =========================================================================
    private void assertNotNull(String message, Object value) {
        if (value == null) {
            throw new FatalConfigurationException(message);
        }
    }
    
    private void assertNotEmpty(String value, String message) {
        if (value == null || value.isEmpty()) {
            throw new FatalConfigurationException(message);
        }
    }
    
    private void assertFalse(String message, boolean condition) {
        if (condition) {
            throw new FatalConfigurationException(message);
        }
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private Map<String, String> convertToMap(List<Config> configs) {
        Map<String, String> result = new HashMap<>();
        for (Config conf : configs) {
            result.put(conf.getKey(), conf.getValue());
        }
        return result;
    }
    
    private String getConfig(final String key, final Map<String, String> config) {
        return getConfig(key, config, null);
    }
    
    private String getConfig(final String key, final Map<String, String> config, final String defaultValue) {
        final String value = config.get(key);
        final String currentValue = value == null ? defaultValue : value;
        
        return currentValue == null ? null : applicationCtx.getGlobalConfiguration().applyProperties(currentValue);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    public LdapConnectorData getConnectorData() {
        return connectorData;
    }
    
    public SecurityConfiguration getSecurityConfig() {
        return securityConfig;
    }
    
    public LdapMapper getMapper() {
        return mapper;
    }
    
}
