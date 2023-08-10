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
package io.inugami.configuration.services.resolver;

import io.inugami.api.mapping.Mapper;
import io.inugami.api.models.Gav;
import io.inugami.api.models.plugins.ManifestInfo;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.configuration.services.mapping.ManifestGavMapper;
import io.inugami.configuration.services.mapping.ManifestMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.jar.Manifest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * ManifestResolverTest
 *
 * @author patrick_guillerm
 * @since 27 déc. 2017
 */
class ManifestResolverTest implements TestUnitResources {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testProcessLoadingManifest() throws Exception {
        final File resourcesPath = initResourcesPath();
        final File manifestFile  = FilesUtils.buildFile(resourcesPath, "MANIFEST.MF.txt");

        final Manifest manifest = ManifestResolver.loadManifest(manifestFile.toURI().toURL());
        assertNotNull(manifest);
        final Mapper<Gav, Manifest>          mapper     = new ManifestGavMapper();
        final Mapper<ManifestInfo, Manifest> mapperInfo = new ManifestMapper();

        assertNotNull(mapper.mapping(manifest));
        final ManifestInfo info = mapperInfo.mapping(manifest);
        assertNotNull(info.getWorkspace());
    }
}
