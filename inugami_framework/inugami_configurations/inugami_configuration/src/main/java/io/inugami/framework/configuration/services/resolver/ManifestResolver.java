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
package io.inugami.framework.configuration.services.resolver;

import io.inugami.framework.commons.files.FilesUtils;
import io.inugami.framework.configuration.exceptions.FatalConfigurationException;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.services.mapping.ManifestGavMapper;
import io.inugami.framework.configuration.services.mapping.ManifestGavMapperForce;
import io.inugami.framework.configuration.services.mapping.ManifestMapper;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.FatalException;
import io.inugami.framework.interfaces.mapping.Mapper;
import io.inugami.framework.interfaces.models.Tuple;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

/**
 * ManifestResolver
 *
 * @author patrick_guillerm
 * @since 27 déc. 2017
 */
@Slf4j
class ManifestResolver {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Mapper<Gav, Manifest> mapperGav = new ManifestGavMapper();

    private static final Mapper<Gav, Manifest> mapperGavForce = new ManifestGavMapperForce();

    private static final Mapper<ManifestInfo, Manifest> mapper = new ManifestMapper();

    private static final Map<Gav, ManifestInfo> MANIFESTS = loadAllManifests();

    // =========================================================================
    // INITIALIZE
    // =========================================================================
    private static Map<Gav, ManifestInfo> loadAllManifests() {
        final Map<Gav, ManifestInfo> result      = new HashMap<>();
        final ClassLoader            classLoader = ConfigurationResolver.class.getClassLoader();

        final Enumeration<URL> urls;
        try {
            urls = classLoader.getResources("META-INF/MANIFEST.MF");
        } catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        }

        while (urls.hasMoreElements()) {
            final URL                      url  = urls.nextElement();
            final Tuple<Gav, ManifestInfo> item = processLoadingManifest(url);
            if (item != null) {
                result.put(item.getKey(), item.getValue());
            }
        }

        return result;
    }

    private static Tuple<Gav, ManifestInfo> processLoadingManifest(final URL url) {
        Tuple<Gav, ManifestInfo> result   = null;
        final Manifest           manifest = loadManifest(url);
        Gav                      gav      = null;
        if (manifest != null) {
            gav = mapperGav.mapping(manifest);
        }

        if (gav != null) {
            final ManifestInfo manifestInfo = new ManifestInfo(mapper.mapping(manifest), url);

            result = new Tuple<>(gav, manifestInfo);
        }
        return result;
    }

    protected static Manifest loadManifest(final URL url) {
        InputStream stream = null;
        try {
            stream = url.openStream();
        } catch (final IOException e) {
            log.error(e.getMessage());
        }

        Manifest manifest = null;
        try {
            manifest = new Manifest(stream);
        } catch (final IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (final IOException e) {
                log.error(e.getMessage());
            }
        }

        return manifest;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public ManifestInfo resolvePluginManifest(final PluginConfiguration config) {
        Asserts.assertNotNull("plugin configuration mustn't be null!", config);
        ManifestInfo result = MANIFESTS.get(config.getGav());
        if (result != null) {
            final File file = buildPathFromURI(config.getConfigFile());
            if ((file != null) && file.exists()) {
                result = loadManifestFromFileSystem(file);

            }
        }
        return result;
    }

    private ManifestInfo loadManifestFromFileSystem(final File file) {
        ManifestInfo result       = null;
        final File   manifestFile = FilesUtils.buildFile(file.getParentFile(), "MANIFEST.MF");

        if (manifestFile.exists()) {
            URL url = null;
            try {
                url = manifestFile.toURI().toURL();
            } catch (final MalformedURLException e) {
                throw new FatalConfigurationException(e.getMessage());
            }

            final Manifest manifest = loadManifest(url);
            Gav            gav      = null;
            if (manifest != null) {
                gav = mapperGavForce.mapping(manifest);
            }

            if (gav != null) {
                result = new ManifestInfo(mapper.mapping(manifest), url);
            }
        } else {
            log.error("Can't read manifest : {}", manifestFile);
        }

        return result;
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private File buildPathFromURI(final String path) {
        File result = null;
        try {
            result = new File(new URI(path).toURL().getFile());
        } catch (final Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
