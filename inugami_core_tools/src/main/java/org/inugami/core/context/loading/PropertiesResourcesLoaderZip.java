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
package org.inugami.core.context.loading;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.inugami.api.exceptions.FatalException;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.loggers.Loggers;
import org.inugami.commons.files.zip.ZipScaner;
import org.inugami.commons.files.zip.ZipScanerBuilder;

/**
 * PropertiesResourcesLoaderZip
 * 
 * @author patrick_guillerm
 * @since 5 janv. 2018
 */
public class PropertiesResourcesLoaderZip implements PropertiesResourcesLoader {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Pattern REGEX = Pattern.compile("(?:.*)(?:_)([a-zA-Z]{2,3})(?:.properties)");
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Map<String, Map<String, String>> loadResources(final File file) throws TechnicalException {
        final Map<String, Map<String, String>> result = new HashMap<>();
        
        Map<String, String> propertiesContent = null;
        try {
            propertiesContent = searchAndReadProperties(file);
        }
        catch (final IOException e) {
            throw new TechnicalException(e.getMessage(), e);
        }
        
        if (propertiesContent != null) {
            for (final Map.Entry<String, String> entry : propertiesContent.entrySet()) {
                final String key = resolveLanguageKey(entry.getKey());
                final Map<String, String> data = loadProperties(entry.getValue());
                if (result.containsKey(key)) {
                    result.get(key).putAll(data);
                }
                else {
                    result.put(key, data);
                }
            }
        }
        return result;
    }
    
    private Map<String, String> searchAndReadProperties(final File file) throws IOException {
        final Map<String, String> result = new HashMap<>();
        ZipScanerBuilder.builder(file).addZipScanProcessor(this::matchesPropertiesResources, (zipEntry, zipFile) -> {
            final String content = this.readZipFile(zipEntry, zipFile);
            if (content != null) {
                result.put(zipEntry.getName(), content);
            }
        }).scan();
        
        return result;
    }
    
    private boolean matchesPropertiesResources(final String path) {
        return !path.contains("pom.properties") && path.endsWith(".properties");
    }
    
    // =========================================================================
    // PROPERTIES
    // =========================================================================
    private String resolveLanguageKey(final String key) {
        String result = "default";
        final Matcher matcher = REGEX.matcher(key);
        if (matcher.matches()) {
            result = matcher.group(1);
        }
        return result;
    }
    
    private Map<String, String> loadProperties(final String value) {
        final Map<String, String> result = new HashMap<>();
        final Reader reader = new StringReader(value);
        final Properties properties = new Properties();
        try {
            properties.load(reader);
        }
        catch (final IOException e) {
            Loggers.PLUGINS.error(e.getMessage());
        }
        
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            result.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        
        return result;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private String readZipFile(final ZipEntry zipEntry, final ZipFile zipFile) {
        try {
            return ZipScaner.readFromUrl(zipEntry, zipFile);
        }
        catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }
}
