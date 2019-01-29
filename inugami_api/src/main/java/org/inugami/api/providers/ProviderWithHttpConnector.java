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
package org.inugami.api.providers;

import org.inugami.api.processors.ConfigHandler;

/**
 * ProviderWithHttpConnector
 * 
 * @author patrick_guillerm
 * @since 23 août 2017
 */
public interface ProviderWithHttpConnector {
    String HTTP_TIMEOUT        = "httpTimeout";
    
    String HTTP_SOCKET_TIMEOUT = "socketTimeout";
    
    String HTP_TTL             = "httpTTL";
    
    String HTTP_MAX_CONNECTION = "httpMaxConnection";
    
    String HTTP_MAX_PER_ROUTE  = "httpMaxPerRoute";
    
    // :::::::::::::: getTimeout :::::::::::::::::::::::::::::::::::::::::::::::
    default int getTimeout(final ConfigHandler<String, String> config, final int defaultValue) {
        return getTimeout(config, "", defaultValue);
    }
    
    default int getTimeout(final ConfigHandler<String, String> config, final String name, final int defaultValue) {
        return Integer.parseInt(config.optionnal().grabOrDefault(buildConfigKey(HTTP_TIMEOUT, name),
                                                                 String.valueOf(defaultValue)));
    }
    
    // :::::::::::::: getTimeout :::::::::::::::::::::::::::::::::::::::::::::::
    default int getMaxConnections(final ConfigHandler<String, String> config, final int defaultValue) {
        return getMaxConnections(config, "", defaultValue);
    }
    
    default int getMaxConnections(final ConfigHandler<String, String> config, final String name,
                                  final int defaultValue) {
        return Integer.parseInt(config.optionnal().grabOrDefault(buildConfigKey(HTTP_MAX_CONNECTION, name),
                                                                 String.valueOf(defaultValue)));
    }
    
    // :::::::::::::: getTimeout :::::::::::::::::::::::::::::::::::::::::::::::
    default int getTTL(final ConfigHandler<String, String> config, final int defaultValue) {
        return getTTL(config, "", defaultValue);
    }
    
    default int getTTL(final ConfigHandler<String, String> config, final String name, final int defaultValue) {
        return Integer.parseInt(config.optionnal().grabOrDefault(buildConfigKey(HTP_TTL, name),
                                                                 String.valueOf(defaultValue)));
    }
    
    // :::::::::::::: getTimeout :::::::::::::::::::::::::::::::::::::::::::::::
    default int getMaxPerRoute(final ConfigHandler<String, String> config, final int defaultValue) {
        return getMaxPerRoute(config, "", defaultValue);
    }
    
    default int getMaxPerRoute(final ConfigHandler<String, String> config, final String name, final int defaultValue) {
        return Integer.parseInt(config.optionnal().grabOrDefault(buildConfigKey(HTTP_MAX_PER_ROUTE, name),
                                                                 String.valueOf(defaultValue)));
    }
    
    // :::::::::::::: getTimeout :::::::::::::::::::::::::::::::::::::::::::::::
    default int getSocketTimeout(final ConfigHandler<String, String> config, final int defaultValue) {
        return getSocketTimeout(config, "", defaultValue);
    }
    
    default int getSocketTimeout(final ConfigHandler<String, String> config, final String name,
                                 final int defaultValue) {
        return Integer.parseInt(config.optionnal().grabOrDefault(buildConfigKey(HTTP_SOCKET_TIMEOUT, name),
                                                                 String.valueOf(defaultValue)));
    }
    
    // :::::::::::::: tools ::::::::::::::::::::::::::::::::::::::::::::::::::::
    default String buildConfigKey(final String baseName, final String name) {
        return name == null ? baseName : String.join("", baseName, name);
    }
}
