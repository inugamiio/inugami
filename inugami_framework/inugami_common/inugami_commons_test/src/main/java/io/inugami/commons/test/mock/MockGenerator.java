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

import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static io.inugami.commons.test.mock.utils.MockGeneratorUtils.*;
import static io.inugami.framework.api.tools.ReflectionUtils.runSafe;

/**
 * MockGenerator allows you to generate REST endpoint mocks and OpenAPI documentation.
 * It's required to define <strong>mock.generator.path</strong> in your Maven pom.xml or in Java execution parameters.
 * This path should point to the REST endpoints Maven module (inside your resources folder).
 */
@Slf4j
@UtilityClass
public class MockGenerator {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String MOCK_GENERATOR_PATH = "mock.generator.path";

    // =================================================================================================================
    // GENERATE MOCK
    // =================================================================================================================
    public static File generate(@NonNull final MockContext mockContext) {
        final File mockFolder = getFolder(System.getProperty(MOCK_GENERATOR_PATH));
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

        if (json != null) {
            try {
                log.info("write mock file : {}", filePath);
                FileUtils.write(filePath, json, StandardCharsets.UTF_8);
                return filePath;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private static MockContext cleanContext(final @NotNull MockContext mockContext) {
        final var builder = mockContext.toBuilder();
        builder.folder(null);

        final var requestHeaders  = Optional.ofNullable(mockContext.getRequestHeaders()).orElse(new LinkedHashMap<>());
        final var responseHeaders = Optional.ofNullable(mockContext.getResponseHeaders()).orElse(new LinkedHashMap<>());
        builder.requestHeaders(requestHeaders);
        builder.responseHeaders(responseHeaders);

        if (requestHeaders.containsKey(Headers.X_DEVICE_IDENTIFIER)) {
            responseHeaders.put(Headers.X_DEVICE_IDENTIFIER, requestHeaders.get(Headers.X_DEVICE_IDENTIFIER));
        }
        if (requestHeaders.containsKey(Headers.X_CORRELATION_ID)) {
            responseHeaders.put(Headers.X_CORRELATION_ID, requestHeaders.get(Headers.X_CORRELATION_ID));
        }
        return builder.build();
    }

    public static MockContext readMock(@NonNull final File file) {
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        return runSafe(() -> JsonMarshaller.getInstance().getIndentedObjectMapper().readValue(file, MockContext.class));
    }

    // =================================================================================================================
    // GENERATE OPEN API DOCUMENTATION
    // =================================================================================================================
    public static void generateOpenApiDocumentation(@NonNull final String folder) {
        generateOpenApiDocumentation(folder, MockOpenApiContext.builder().build());
    }

    public static void generateOpenApiDocumentation(@NonNull final String folder,
                                                    @NonNull final MockOpenApiContext context) {

    }


}
