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
package io.inugami.core.context.loading;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.models.JsonBuilder;
import io.inugami.commons.engine.JavaScriptEngine;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.files.zip.ZipScaner;
import io.inugami.commons.files.zip.ZipScanerBuilder;

/**
 * LoadAlertsResources
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2017
 */
public class AlertsResourcesLoaderZip implements AlertsResourcesLoader {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final JavaScriptEngine javaScriptEngine = JavaScriptEngine.getInstance();
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void loadAlertingsResources(final URL url) throws TechnicalException {
        final File jarPath = FilesUtils.resolveJarFile(url);
        
        final JsonBuilder jsResources = new JsonBuilder();
        
        //@formatter:off
        try {
            ZipScanerBuilder.builder(jarPath)
                            .addSortsFunction(this::sortsJavaScriptResources)
                            .addZipScanProcessor(this::matchesJsResources,(zipEntry, zipFile)->{
                                final String content = this.readZipFile(zipEntry, zipFile);
                                jsResources.write(content==null?"":content);
                                jsResources.addLine();
                            })
                            .scan();
        }
        catch (final IOException e) {
            throw new TechnicalException(e.getMessage(),e);
        }     
        //@formatter:on
        final String script = jsResources.toString();
        javaScriptEngine.register(script, String.valueOf(url));
    }
    
    protected List<ZipEntry> sortsJavaScriptResources(final List<ZipEntry> entries) {
        final List<ZipEntry> mainsNamespaces = new ArrayList<>();
        final List<ZipEntry> namespaces = new ArrayList<>();
        final List<ZipEntry> others = new ArrayList<>();
        
        for (final ZipEntry entry : entries) {
            if (entry.getName().contains("namespace")) {
                final String fileName = ZipScaner.extractFileName(entry.getName());
                if ("namespace.js".equals(fileName)) {
                    mainsNamespaces.add(entry);
                }
                else {
                    namespaces.add(entry);
                }
                
            }
            else {
                others.add(entry);
            }
        }
        
        mainsNamespaces.sort(this::sortsZipEntries);
        namespaces.sort(this::sortsZipEntries);
        others.sort(this::sortsZipEntries);
        
        final List<ZipEntry> result = new ArrayList<>();
        result.addAll(mainsNamespaces);
        result.addAll(namespaces);
        result.addAll(others);
        return result;
    }
    
    private int sortsZipEntries(final ZipEntry ref, final ZipEntry value) {
        return ref.getName().compareTo(value.getName());
    }
    
    private boolean matchesJsResources(final String path) {
        return path.startsWith("META-INF/alertings/") && isJavaScriptFile(path);
    }
    
    private boolean isJavaScriptFile(final String path) {
        if (path == null) {
            return false;
        }
        final String[] parts = path.replaceAll("\\\\", "/").split("/");
        final String fileName = parts[parts.length - 1];
        return fileName == null ? false : notHiddenFile(path) && fileName.endsWith(".js");
    }
    
    private boolean notHiddenFile(final String fileName) {
        return !fileName.startsWith(".");
    }
    
    private String readZipFile(final ZipEntry zipEntry, final ZipFile zipFile) {
        try {
            return ZipScaner.readFromUrl(zipEntry, zipFile);
        }
        catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }
    
}
