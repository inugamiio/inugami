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
package io.inugami.framework.commons.spring.configuration.external;

import io.inugami.framework.api.tools.RunSafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.io.FileReader;
import java.util.*;

import static io.inugami.framework.commons.spring.configuration.external.ExternalPropertiesError.EXTERNAL_PROPERTIES_FOLDER_NOT_EXISTS;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertFolderExists;

/**
 * @since 2026-02-07
 */
@Slf4j
public class ExternalPropertiesLoader implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String EXTERNAL_CONFIGURATION = "external-configuration";
    public static final String PROPERTIES             = ".properties";

    // =================================================================================================================
    // INIT
    // =================================================================================================================
    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        final String externalConfigFolder = getProperty(EXTERNAL_CONFIGURATION);
        if (externalConfigFolder == null) {
            return;
        }

        final var        folder         = verifyFolder(externalConfigFolder);
        final List<File> properties     = searchProperties(folder);
        final var        propertySource = applicationContext.getEnvironment().getPropertySources();


        for (final File file : properties) {
            log.debug("loading {} properties", file.getAbsolutePath());
            final Map<String, String> config       = loadConfigurationProperties(file);
            final Map<String, Object> configBucket = new LinkedHashMap<>();
            for (final var entry : config.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                final var value = entry.getValue().trim();
                if (!value.isEmpty()) {
                    configBucket.put(entry.getKey(), value);
                }
            }
            if (configBucket.isEmpty()) {
                continue;
            }
            log.debug("loaded {} properties", file.getAbsolutePath());
            propertySource.addLast(new MapPropertySource(file.getName(), configBucket));
        }
    }

    // =================================================================================================================
    // LOADER
    // =================================================================================================================
    private static Map<String, String> loadConfigurationProperties(final File propertyFile) {
        Map<String, String> result = new LinkedHashMap<>();

        final var  properties = new Properties();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(propertyFile);
            properties.load(fileReader);
            for (var entry : properties.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                final var value = String.valueOf(entry.getValue()).trim();
                if (value.isEmpty()) {
                    continue;
                }
                result.put(String.valueOf(entry.getKey()), value);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(fileReader);
        }

        return result;
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    protected String getProperty(final String key) {
        return Optional.ofNullable(System.getProperty(key))
                       .orElse(System.getenv().get(key));
    }

    protected File verifyFolder(final String externalConfigFolder) {
        final var result = RunSafeUtils.runSafe(()-> new File(externalConfigFolder).getCanonicalFile());
        assertFolderExists(EXTERNAL_PROPERTIES_FOLDER_NOT_EXISTS, result);
        return result;
    }

    protected List<File> searchProperties(final File folder) {
        return Arrays.asList(folder.listFiles())
                     .stream()
                     .filter(file -> file.getName().endsWith(PROPERTIES) )
                     .toList();
    }


    private static void close(final FileReader fileReader) {
        if (fileReader == null) {
            return;
        }
        try {
            fileReader.close();
        } catch (Throwable e) {
        }
    }

}
