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
package io.inugami.commons.test.mock;

import io.inugami.commons.test.mock.utils.MockGeneratorOpenApiUtils;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.framework.interfaces.tools.ListUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static io.inugami.commons.test.mock.MockGeneratorError.*;
import static io.inugami.commons.test.mock.utils.MockGeneratorUtils.*;
import static io.inugami.framework.api.tools.RunSafeUtils.runSafe;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotNull;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertTrue;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

/**
 * MockGenerator allows you to generate REST endpoint mocks and OpenAPI documentation.
 * <p>
 * It's required to define <strong>mock.generator.path</strong> in your Maven pom.xml or in Java execution parameters.
 * This path should be the root path to your REST endpoints Maven module.
 * The path can be defined relatively, like <strong>../inugami_dashboard_interfaces_api/</strong>
 * <p>
 * This utility class should be used into your rest controller unit test to generate mocks and OpenApi documentation.
 */
@Slf4j
@UtilityClass
public class MockGenerator {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String MOCK_GENERATOR_PATH = "mock.generator.path";
    public static final String EMPTY               = "";

    // =================================================================================================================
    // GENERATE MOCK
    // =================================================================================================================
    public static File generate(@NonNull final MockContext mockContext) {
        final String mockPath = System.getProperty(MOCK_GENERATOR_PATH);
        if (mockPath == null || mockPath.isEmpty()) {
            return null;
        }
        final File mockFolder = getFolder(mockPath);
        if (mockFolder == null) {
            log.warn("mock.generator.path isn't defined or path doesn't exists");
            return null;
        }

        final String fileName = buildFileName(mockContext.getErrorCode());
        final File   filePath = buildMockFilePath(mockFolder, mockContext.getFolder(), fileName);
        if (filePath == null) {
            log.error("can build mock file path");
            return null;
        }

        final String json = runSafe(() -> JsonMarshaller.getInstance()
                                                        .getIndentedObjectMapper()
                                                        .writeValueAsString(cleanContext(mockContext)));

        if (json == null) {
            return null;
        }
        return runSafe(() -> {
            log.info("write mock file : {}", filePath);
            FileUtils.write(filePath, json, StandardCharsets.UTF_8);
            return filePath;
        }, log);
    }

    private static MockContext cleanContext(final @NotNull MockContext mockContext) {
        final var builder = mockContext.toBuilder();
        builder.folder(null);

        final var requestHeaders  = Optional.ofNullable(mockContext.getRequestHeaders()).orElse(new LinkedHashMap<>());
        final var responseHeaders = Optional.ofNullable(mockContext.getResponseHeaders()).orElse(new LinkedHashMap<>());
        builder.requestHeaders(requestHeaders);
        builder.responseHeaders(responseHeaders);

        applyIfNotNull(requestHeaders.get(Headers.X_DEVICE_IDENTIFIER), v -> responseHeaders.put(Headers.X_DEVICE_IDENTIFIER, v));
        applyIfNotNull(requestHeaders.get(Headers.X_CORRELATION_ID), v -> responseHeaders.put(Headers.X_CORRELATION_ID, v));

        return builder.build();
    }

    // =================================================================================================================
    // READ MOCKS
    // =================================================================================================================
    public static MockContext readMock(@NonNull final File file) {
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        return runSafe(() -> JsonMarshaller.getInstance().getIndentedObjectMapper().readValue(file, MockContext.class),
                       log);
    }

    @SuppressWarnings({"java:S3824"})
    public static @NonNull Map<String, List<MockContext>> readMocks(@NonNull final Collection<File> files) {
        final Map<String, List<MockContext>> result = new LinkedHashMap<>();

        for (File file : files) {
            final String methodName = resolveLastParentFolder(file);
            if (methodName == null) {
                continue;
            }
            List<MockContext> bucket = result.get(methodName);
            if (bucket == null) {
                bucket = new ArrayList<>();
                result.put(methodName, bucket);
            }
            applyIfNotNull(readMock(file), bucket::add);
        }

        return result;
    }


    // =================================================================================================================
    // GENERATE OPEN API DOCUMENTATION
    // =================================================================================================================
    public static File generateOpenApiDocumentation(@NonNull final MockOpenApiContext context) {
        final String mockPath = System.getProperty(MOCK_GENERATOR_PATH);
        if (mockPath == null || mockPath.isEmpty()) {
            return null;
        }
        final File mockFolder = getFolder(mockPath);

        assertNotNull(CONTEXT_REQUIRED, context);
        assertNotNull(CONTEXT_REQUIRED, context.getRestClientClass());
        if (context.getFolders() == null || context.getFolders().isEmpty()) {
            log.warn("can't build OpenApi documentation without mock folder");
        }

        assertNotNull(ENDPOINT_MAVEN_MODULE_PATH_REQUIRED, mockFolder);
        assertTrue(ENDPOINT_MAVEN_MODULE_PATH_REQUIRED, mockFolder.exists());
        assertTrue(ENDPOINT_MAVEN_MODULE_NOT_READABLE, mockFolder.canRead());
        assertTrue(ENDPOINT_MAVEN_MODULE_NOT_WRITABLE, mockFolder.canWrite());


        final Map<String, List<MockContext>> mocks = readMocks(buildMockPaths(mockFolder, context.getFolders()));
        final StringBuilder packagePath = new StringBuilder(mockFolder.getPath())
                .append(File.separator)
                .append("src")
                .append(File.separator)
                .append("main")
                .append(File.separator)
                .append("java");
        final String rawPackagePath = resolvePackagePath(context.getRestClientClass());
        if (!rawPackagePath.startsWith(File.separator)) {
            packagePath.append(File.separator);
        }
        packagePath.append(rawPackagePath);
        final var packageFolder = new File(packagePath.toString());
        createFolderIfNotExists(packageFolder);

        try {
            final var result = MockGeneratorOpenApiUtils.renderOpenApiDocumentation(packageFolder, mocks, context);
            log.info("generated OpenApi documentation : {}", result.getAbsoluteFile());
            return result;
        } catch (IOException error) {
            log.error("can't generate OpenApi documentation for {}", context.getRestClientClass().getName(), error);
            return null;
        }
    }


    private static List<File> buildMockPaths(@NonNull final File mockFolder,
                                             @NonNull final List<String> folders) {
        final Set<File>   files          = new LinkedHashSet<>();
        final Set<String> currentFolders = new LinkedHashSet<>(folders);
        for (String currentFolder : currentFolders) {
            final File folder = buildMockFileFolder(mockFolder, currentFolder);
            if (folder == null || folder.listFiles() == null) {
                continue;
            }
            files.addAll(ListUtils.toList(folder.listFiles()));
        }

        return new ArrayList<>(files);
    }


}
