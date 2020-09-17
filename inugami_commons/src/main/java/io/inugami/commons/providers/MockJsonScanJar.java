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
package io.inugami.commons.providers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.files.zip.ZipScanerBuilder;
import io.inugami.commons.scan.ScanResources;

/**
 * MockJsonScanJar
 * 
 * @author patrick_guillerm
 * @since 26 janv. 2018
 */
public final class MockJsonScanJar implements ScanResources<List<String>> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final URL url;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MockJsonScanJar(final URL manifestUrl) {
        this.url = manifestUrl;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public List<String> scan() throws IOException {
        final File jarPath = FilesUtils.resolveJarFile(url);
        final List<String> result = new ArrayList<>();
        //@formatter:off
        ZipScanerBuilder.builder(jarPath)
                        .addZipScanProcessor(this::matchesJsResources,(zipEntry, zipFile)->{
                                result.add(zipEntry.getName());
                        })
                       .scan();
        //@formatter:on
        return result;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private boolean matchesJsResources(final String path) {
        return path.startsWith(MockJsonHelper.MOCK_DIRECTORY);
    }
}
