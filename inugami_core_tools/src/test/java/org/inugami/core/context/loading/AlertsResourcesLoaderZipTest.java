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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;

import org.inugami.commons.engine.JavaScriptEngine;
import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.junit.Test;

/**
 * AlertsResourcesLoaderTest
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2017
 */
public class AlertsResourcesLoaderZipTest implements TestUnitResources {
    
    // =========================================================================
    // LOAD ALERTING RESOURCES
    // =========================================================================
    @Test
    public void testResolveJarFile() throws Exception {
        final String path = "jar:file:/Users/joeFoobar/dev/serveurs/inugami-server/plugins/my-plugin-0.1.0-SNAPSHOT.jar!/META-INF/MANIFEST.MF";
        final File filePath = FilesUtils.resolveJarFile(new URL(path));
        assertNotNull(filePath);
        
        if (FilesUtils.isWindows()) {
            assertEquals("/Users/joeFoobar/dev/serveurs/inugami-server/plugins/my-plugin-0.1.0-SNAPSHOT.jar",
                         filePath.getAbsolutePath().substring(2).replaceAll("\\\\", "/"));
        }
        else {
            assertEquals("/Users/joeFoobar/dev/serveurs/inugami-server/plugins/my-plugin-0.1.0-SNAPSHOT.jar",
                         filePath.getAbsolutePath());
        }
        
    }
    
    @Test
    public void testLoadAlertingsResources() throws Exception {
        final File resourcesPath = initResourcesPath();
        final File pluginJar = FilesUtils.buildFile(resourcesPath, "plugin_for_scan_test-0.1.0-SNAPSHOT.jar");
        
        final AlertsResourcesLoaderZip alertsLoader = new AlertsResourcesLoaderZip();
        alertsLoader.loadAlertingsResources(pluginJar.toURI().toURL());
        
        final JavaScriptEngine engine = JavaScriptEngine.getInstance();
        final String scriptResultMfo = engine.execute("foobar.tv.default.service");
        assertNotNull(scriptResultMfo);
        assertEquals("{\"level\":\"info\",\"message\":\"Service error is growing\"}", scriptResultMfo);
        
        //@formatter:off
        final String scriptResultSimple = engine.execute("simpleFunction");
        assertNotNull(scriptResultSimple);
        assertEquals("{\"level\":\"error\",\"message\":\"please check your Apps!\",\"data\":{\"display\":\"main\",\"sound\":\"main-error\"}}",scriptResultSimple);
        //@formatter:on
    }
    
    @Test
    public void testSortsJavaScriptResources() throws Exception {
        final List<ZipEntry> entries = new ArrayList<>();
        
        entries.add(new JarEntry("META-INF/alertings/namespaces/namespace.js"));
        entries.add(new JarEntry("META-INF/alertings/prd.service.js"));
        entries.add(new JarEntry("META-INF/alertings/namespace.js"));
        entries.add(new JarEntry("META-INF/alertings/application.alerts.js"));
        entries.add(new JarEntry("META-INF/alertings/namespaces/specific.js"));
        
        final AlertsResourcesLoaderZip alertsLoader = new AlertsResourcesLoaderZip();
        final List<ZipEntry> sorted = alertsLoader.sortsJavaScriptResources(entries);
        
        assertNotNull(sorted);
        assertEquals(5, sorted.size());
        //@formatter:off
        assertEquals("META-INF/alertings/namespace.js",             sorted.get(0).getName());
        assertEquals("META-INF/alertings/namespaces/namespace.js",  sorted.get(1).getName());
        assertEquals("META-INF/alertings/namespaces/specific.js",   sorted.get(2).getName());
        assertEquals("META-INF/alertings/application.alerts.js",    sorted.get(3).getName());
        assertEquals("META-INF/alertings/prd.service.js",   sorted.get(4).getName());
        //@formatter:on        
        
    }
}
