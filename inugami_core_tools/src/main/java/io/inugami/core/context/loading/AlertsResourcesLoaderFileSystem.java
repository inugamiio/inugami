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

import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.loggers.Loggers;
import io.inugami.commons.engine.JavaScriptEngine;
import io.inugami.commons.files.FilesUtils;
import io.inugami.configuration.exceptions.FatalConfigurationException;

/**
 * LoadAlertsResources
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2017
 */
public class AlertsResourcesLoaderFileSystem implements AlertsResourcesLoader {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final JavaScriptEngine javaScriptEngine = JavaScriptEngine.getInstance();
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void loadAlertingsResources(final URL url) throws TechnicalException {
        final File manifestFile = new File(url.getFile());
        final File metaInfFolder = manifestFile.getAbsoluteFile().getParentFile();
        final File alertsResources = FilesUtils.buildFile(metaInfFolder, "alertings");
        if (alertsResources.exists()) {
            processLoadingResources(alertsResources);
        }
    }
    
    // =========================================================================
    // PROCESS LOADING
    // =========================================================================
    
    private void processLoadingResources(final File path) {
        final List<File> paths = FilesUtils.scanFilesystem(path, (dir, fileName) -> fileName.endsWith(".js"));
        if (!paths.isEmpty()) {
            final List<File> orderedFiles = sorts(paths);
            
            try {
                for (final File file : orderedFiles) {
                    javaScriptEngine.register(FilesUtils.readContent(file), path.getAbsolutePath());
                }
            }
            catch (final IOException e) {
                Loggers.INIT.error(e.getMessage());
                throw new FatalConfigurationException(e.getMessage(), e);
            }
        }
        
    }
    
    private List<File> sorts(final List<File> paths) {
        
        final List<File> mainsNamespaces = new ArrayList<>();
        final List<File> namespaces = new ArrayList<>();
        final List<File> others = new ArrayList<>();
        
        for (final File file : paths) {
            if ("namespace.js".equals(file.getName())) {
                mainsNamespaces.add(file);
            }
            else if (file.getAbsolutePath().contains("namespace")) {
                namespaces.add(file);
            }
            else {
                others.add(file);
            }
        }
        
        mainsNamespaces.sort(this::sortsFiles);
        namespaces.sort(this::sortsFiles);
        others.sort(this::sortsFiles);
        
        final List<File> result = new ArrayList<>();
        result.addAll(mainsNamespaces);
        result.addAll(namespaces);
        result.addAll(others);
        return result;
    }
    
    private int sortsFiles(final File ref, final File value) {
        return ref.getAbsolutePath().compareTo(value.getAbsolutePath());
    }
    
}
