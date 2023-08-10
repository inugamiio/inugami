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
package io.inugami.commons.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.Warning;
import io.inugami.api.functionnals.VoidFunctionWithException;
import io.inugami.commons.test.api.LineMatcher;
import io.inugami.commons.test.dto.AssertDtoContext;
import io.inugami.commons.test.dto.AssertLogContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mockito.invocation.InvocationOnMock;

import java.io.File;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"java:S6355", "java:S1123", "java:S3740", "java:S1133", "java:S119", "java:S1133"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnitTestHelper {

    // =========================================================================
    // LOAD / READ FILE
    // =========================================================================
    @Deprecated
    public static String loadJsonReference(final String relativePath) {
        return UnitTestHelperFile.readFileRelative(relativePath);
    }

    public static String readFileIntegration(final String integrationPath) {
        return UnitTestHelperFile.readFileIntegration(integrationPath);
    }

    public static String readFileRelative(final String relativePath) {
        return UnitTestHelperFile.readFileRelative(relativePath);
    }

    public static String readFile(final File file) {
        return UnitTestHelperFile.readFile(file);
    }

    public static String readFile(final String file) {
        return UnitTestHelperFile.readFile(file);
    }

    public static void fileCacheClean() {
        UnitTestHelperFile.cacheClean();
    }

    // =========================================================================
    // BUILD PATH
    // =========================================================================
    public static File buildTestFilePath(final String... filePathParts) {
        return UnitTestHelperPath.buildTestFilePath(filePathParts);
    }

    public static File buildIntegrationTestFilePath(final String... filePathParts) {
        return UnitTestHelperPath.buildIntegrationTestFilePath(filePathParts);
    }

    public static File buildPath(final String... parts) {
        return UnitTestHelperPath.buildPath(parts);
    }

    // =========================================================================
    // STUB
    // =========================================================================
    public static <T, ID> T loadIntegrationTestStub(final InvocationOnMock answer,
                                                    final String folder,
                                                    final Function<T, ID> getId,
                                                    final Class<? extends T> userDtoClass) {
        return UnitTestHelperStub.loadIntegrationTestStub(answer, folder, getId, userDtoClass);
    }

    public static <T, ID> T loadIntegrationTestStub(final InvocationOnMock answer,
                                                    final String folder,
                                                    final Function<T, ID> getId,
                                                    final Function<Object[], ID> idResolver,
                                                    final Class<? extends T> userDtoClass) {
        return UnitTestHelperStub.loadIntegrationTestStub(answer, folder, getId, idResolver, userDtoClass);
    }

    public static <T, ID> T loadIntegrationTestStub(final InvocationOnMock answer,
                                                    final String folder,
                                                    final Function<T, ID> getId,
                                                    final Class<? extends T> userDtoClass,
                                                    final List<T> cache) {
        return UnitTestHelperStub.loadIntegrationTestStub(answer, folder, getId, userDtoClass, cache);
    }

    public static <T, ID> T loadIntegrationTestStub(final InvocationOnMock answer,
                                                    final String folder,
                                                    final Function<T, ID> getId,
                                                    final Function<Object[], ID> idResolver,
                                                    final Class<? extends T> userDtoClass,
                                                    final List<T> cache) {
        return UnitTestHelperStub.loadIntegrationTestStub(answer, folder, getId, idResolver, userDtoClass, cache);
    }


    public static <T, ID> T loadRelativeStub(final InvocationOnMock answer,
                                             final String folder,
                                             final Function<T, ID> getId,
                                             final Class<? extends T> userDtoClass) {
        return UnitTestHelperStub.loadRelativeStub(answer, folder, getId, userDtoClass);
    }

    public static <T, ID> T loadRelativeStub(final InvocationOnMock answer,
                                             final String folder,
                                             final Function<T, ID> getId,
                                             final Function<Object[], ID> idResolver,
                                             final Class<? extends T> userDtoClass) {
        return UnitTestHelperStub.loadRelativeStub(answer, folder, getId, idResolver, userDtoClass);
    }

    public static <T, ID> T loadRelativeStub(final InvocationOnMock answer,
                                             final String folder,
                                             final Function<T, ID> getId,
                                             final Class<? extends T> userDtoClass,
                                             final List<T> cache) {
        return UnitTestHelperStub.loadRelativeStub(answer, folder, getId, userDtoClass, cache);
    }

    public static <T, ID> T loadRelativeStub(final InvocationOnMock answer,
                                             final String folder,
                                             final Function<T, ID> getId,
                                             final Function<Object[], ID> idResolver,
                                             final Class<? extends T> userDtoClass,
                                             final List<T> cache) {
        return UnitTestHelperStub.loadRelativeStub(answer, folder, getId, idResolver, userDtoClass, cache);
    }

    // =========================================================================
    // JSON
    // =========================================================================
    public static <T> T convertFromJson(final byte[] data, final TypeReference<T> refObjectType) {
        return UnitTestHelperJson.convertFromJson(data, refObjectType);
    }

    public static <T> T convertFromJson(final byte[] data, final Class<? extends T> objectType) {
        return UnitTestHelperJson.convertFromJson(data, objectType);
    }

    public static <T> T loadIntegrationJson(final String path, final TypeReference<T> refObjectType) {
        return UnitTestHelperJson.loadIntegrationJson(path, refObjectType);
    }

    public static <T> T loadIntegrationJson(final String path, final Class<? extends T> objectType) {
        return UnitTestHelperJson.loadIntegrationJson(path, objectType);
    }

    public static <T> T loadJson(final String relativePath, final TypeReference<T> refObjectType) {
        return UnitTestHelperJson.loadJson(relativePath, refObjectType);
    }

    public static <T> T loadJson(final String relativePath, final Class<? extends T> objectType) {
        return UnitTestHelperJson.loadJson(relativePath, objectType);
    }

    public static String forceConvertToJson(final Object value) {
        return UnitTestHelperJson.forceConvertToJson(value);
    }

    public static String convertToJson(final Object value) {
        return UnitTestHelperJson.convertToJson(value);
    }

    public static String convertToJsonWithoutIndent(final Object value) {
        return UnitTestHelperJson.convertToJsonWithoutIndent(value);
    }


    public static <T> T convertFromJson(final String json, final TypeReference<T> refObjectType) {
        return UnitTestHelperJson.convertFromJson(json, refObjectType);
    }

    public static <T> T convertFromJson(final String json, final Class<? extends T> objectType) {
        return UnitTestHelperJson.convertFromJson(json, objectType);
    }

    // =========================================================================
    // YAML
    // =========================================================================
    public static <T> T loadRelativeYaml(final String path, final Class<? extends T> objectType) {
        return UnitTestHelperYaml.loadRelativeYaml(path, objectType);
    }

    public static JsonNode loadRelativeYaml(final String path) {
        return UnitTestHelperYaml.loadRelativeYaml(path);
    }


    public static <T> T loadIntegrationYaml(final String path, final Class<? extends T> objectType) {
        return UnitTestHelperYaml.loadIntegrationYaml(path, objectType);
    }

    public static JsonNode loadIntegrationYaml(final String path) {
        return UnitTestHelperYaml.loadIntegrationYaml(path);
    }

    public static <T> T loadYaml(final String path, final Class<? extends T> objectType) {
        return UnitTestHelperYaml.loadYaml(path, objectType);
    }

    public static <T> T convertFromYaml(final String content, final TypeReference<T> userDtoClass) {
        return UnitTestHelperYaml.convertFromYaml(content, userDtoClass);
    }

    public static JsonNode loadYaml(final String path) {
        return UnitTestHelperYaml.loadYaml(path);
    }

    public static <T> T convertFromYaml(final String content, final Class<? extends T> userDtoClass) {
        return UnitTestHelperYaml.convertFromYaml(content, userDtoClass);
    }


    public static JsonNode convertFromYaml(final String content) {
        return UnitTestHelperYaml.convertFromYaml(content);
    }

    // =========================================================================
    // TEXT
    // =========================================================================
    public static void assertTextIntegration(final String value, final String path, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertTextIntegration(value, path, lineMatchers);
    }

    public static void assertTextIntegration(final Object value, final String path, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertTextIntegration(value, path, lineMatchers);
    }

    public static void assertTextRelative(final String value, final String path, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertTextRelative(value, path, lineMatchers);
    }

    public static void assertTextRelative(final Object value, final String path, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertTextRelative(value, path, lineMatchers);
    }

    public static void assertText(final Object value, final String jsonRef, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertText(value, jsonRef, lineMatchers);
    }

    public static void assertText(final String value, final String jsonRef, final LineMatcher... lineMatchers) {
        UnitTestHelperText.assertText(value, jsonRef, lineMatchers);
    }

    public static LineMatcher[] buildSkipLines(final int... skipLines) {
        return UnitTestHelperText.buildSkipLines(skipLines);
    }

    public static void assertDto(final AssertDtoContext context) {
        UnitTestHelperDto.assertDto(context);
    }


    // =========================================================================
    // assertLogs
    // =========================================================================

    /**
     * required to add logback.xml in context
     * <pre>
     * {@code
     * <?xml version="1.0" encoding="UTF-8"?>
     * <configuration>
     *
     *     <appender name="logTest" class="io.inugami.commons.test.logs.LogTestAppender">
     *     </appender>
     *
     *     <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
     *         <encoder>
     *             <pattern>[%d{dd/MM HH:mm:ss}]|%logger|%-5level|%thread|%msg\n</pattern>
     *         </encoder>
     *     </appender>
     *
     *
     *     <root level="DEBUG">
     *         <appender-ref ref="STDOUT"/>
     *         <appender-ref ref="logTest"/>
     *     </root>
     *
     * </configuration>
     * }
     * </pre>
     *
     * @param process  function to call
     * @param objClass log class to intercept
     * @param logs     logs content
     * @param matchers line matches
     */
    public static void assertLogs(final VoidFunctionWithException process,
                                  final Class<?> objClass,
                                  final String logs,
                                  final LineMatcher... matchers) {
        UnitTestHelperLogs.assertLogs(process, objClass, logs, matchers);
    }

    /**
     * required to add logback.xml in context
     * <pre>
     * {@code
     * <?xml version="1.0" encoding="UTF-8"?>
     * <configuration>
     *
     *     <appender name="logTest" class="io.inugami.commons.test.logs.LogTestAppender">
     *     </appender>
     *
     *     <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
     *         <encoder>
     *             <pattern>[%d{dd/MM HH:mm:ss}]|%logger|%-5level|%thread|%msg\n</pattern>
     *         </encoder>
     *     </appender>
     *
     *
     *     <root level="DEBUG">
     *         <appender-ref ref="STDOUT"/>
     *         <appender-ref ref="logTest"/>
     *     </root>
     *
     * </configuration>
     * }
     * </pre>
     *
     * @param process  function to call
     * @param objClass log class to intercept
     * @param logs     logs content
     * @param matchers line matches
     */
    public static void assertLogsIntegration(final VoidFunctionWithException process,
                                             final Class<?> objClass,
                                             final String logs,
                                             final LineMatcher... matchers) {
        UnitTestHelperLogs.assertLogsIntegration(process, objClass, logs, matchers);
    }


    /**
     * required to add logback.xml in context
     * <pre>
     * {@code
     * <?xml version="1.0" encoding="UTF-8"?>
     * <configuration>
     *
     *     <appender name="logTest" class="io.inugami.commons.test.logs.LogTestAppender">
     *     </appender>
     *
     *     <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
     *         <encoder>
     *             <pattern>[%d{dd/MM HH:mm:ss}]|%logger|%-5level|%thread|%msg\n</pattern>
     *         </encoder>
     *     </appender>
     *
     *
     *     <root level="DEBUG">
     *         <appender-ref ref="STDOUT"/>
     *         <appender-ref ref="logTest"/>
     *     </root>
     *
     * </configuration>
     * }
     * </pre>
     *
     * @param process  function to call
     * @param pattern  log pattern to intercept
     * @param logs     logs content
     * @param matchers line matches
     */
    public static void assertLogs(final VoidFunctionWithException process,
                                  final String pattern,
                                  final String logs,
                                  final LineMatcher... matchers) {
        UnitTestHelperLogs.assertLogs(process, pattern, logs, matchers);
    }

    /**
     * required to add logback.xml in context
     * <pre>
     * {@code
     * <?xml version="1.0" encoding="UTF-8"?>
     * <configuration>
     *
     *     <appender name="logTest" class="io.inugami.commons.test.logs.LogTestAppender">
     *     </appender>
     *
     *     <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
     *         <encoder>
     *             <pattern>[%d{dd/MM HH:mm:ss}]|%logger|%-5level|%thread|%msg\n</pattern>
     *         </encoder>
     *     </appender>
     *
     *
     *     <root level="DEBUG">
     *         <appender-ref ref="STDOUT"/>
     *         <appender-ref ref="logTest"/>
     *     </root>
     *
     * </configuration>
     * }
     * </pre>
     *
     * @param process  function to call
     * @param pattern  log pattern to intercept
     * @param logs     logs content
     * @param matchers line matches
     */
    public static void assertLogsIntegration(final VoidFunctionWithException process,
                                             final String pattern,
                                             final String logs,
                                             final LineMatcher... matchers) {
        UnitTestHelperLogs.assertLogsIntegration(process, pattern, logs, matchers);
    }


    /**
     * required to add logback.xml in context
     * <pre>
     * {@code
     * <?xml version="1.0" encoding="UTF-8"?>
     * <configuration>
     *
     *     <appender name="logTest" class="io.inugami.commons.test.logs.LogTestAppender">
     *     </appender>
     *
     *     <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
     *         <encoder>
     *             <pattern>[%d{dd/MM HH:mm:ss}]|%logger|%-5level|%thread|%msg\n</pattern>
     *         </encoder>
     *     </appender>
     *
     *
     *     <root level="DEBUG">
     *         <appender-ref ref="STDOUT"/>
     *         <appender-ref ref="logTest"/>
     *     </root>
     *
     * </configuration>
     * }
     * </pre>
     *
     * @param context
     */
    public static void assertLogs(final AssertLogContext context) {
        UnitTestHelperLogs.assertLogs(context);
    }


    // =========================================================================
    // ASSERT ENUM
    // =========================================================================
    public static void assertEnum(final Class<? extends Enum<?>> enumClass,
                                  final String reference,
                                  final LineMatcher... matchers) {
        UnitTestHelperEnum.assertEnum(enumClass, reference, matchers);
    }

    public static void assertEnumRelative(final Class<? extends Enum<?>> enumClass,
                                          final String path,
                                          final LineMatcher... matchers) {
        UnitTestHelperEnum.assertEnumRelative(enumClass, path, matchers);
    }


    // =========================================================================
    // EXCEPTIONS
    // =========================================================================
    public static void assertThrows(final ExecutableFunction handler) {
        UnitTestHelperException.assertThrows(handler);
    }

    public static void assertThrows(final String errorMessage,
                                    final ExecutableFunction handler) {
        UnitTestHelperException.assertThrows(errorMessage, handler);
    }

    public static void assertThrows(final Class<? extends Exception> errorClass,
                                    final ExecutableFunction handler) {
        UnitTestHelperException.assertThrows(errorClass, handler);
    }

    public static void assertThrows(final Class<? extends Exception> errorClass,
                                    final String errorMessage,
                                    final ExecutableFunction handler) {
        UnitTestHelperException.assertThrows(errorClass, errorMessage, handler);
    }

    public static void assertThrows(final ErrorCode errorCode, final ExecutableFunction handler) {
        UnitTestHelperException.assertThrows(errorCode, handler, null);
    }

    public static void assertThrows(final ErrorCode errorCode, final ExecutableFunction handler, final String path) {
        UnitTestHelperException.assertThrows(errorCode, handler, path);
    }

    public static void throwException(final Exception error) {
        UnitTestHelperException.throwException(error);
    }


    // =========================================================================
    // ERROR CODE
    // =========================================================================
    public static void assertErrorCode(final String path, final ErrorCode... errorCodes) {
        UnitTestHelperErrorCode.assertErrorCode(path, errorCodes);
    }

    public static void assertErrorCodeUnique(final ErrorCode... errorCodes) {
        UnitTestHelperErrorCode.assertErrorCodeUnique(errorCodes);
    }

    public static void assertWarningCode(final String path, final Warning... warnings) {
        UnitTestHelperErrorCode.assertWarningCode(path, warnings);
    }

    public static void assertWaringCodeUnique(final Warning... warnings) {
        UnitTestHelperErrorCode.assertWaringCodeUnique(warnings);
    }


    // =========================================================================
    // Utility class
    // =========================================================================
    public static void assertUtilityClass(final Class<?> utilityClass) {
        UnitTestHelperUtilityClass.assertUtilityClass(utilityClass);
    }

    public static void assertUtilityClassLombok(final Class<?> utilityClass) {
        UnitTestHelperUtilityClass.assertUtilityClassLombok(utilityClass);
    }

    // =========================================================================
    // DATA
    // =========================================================================
    public static String getRandomUid() {
        return UnitTestData.getRandomUid();
    }

    public static String getRandomWord() {
        return UnitTestData.getRandomWord();
    }

    public static String getRandomCategory() {
        return UnitTestData.getRandomCategory();
    }

    public static String getRandomLabel() {
        return UnitTestData.getRandomLabel();
    }

    public static String getRandomPhrase() {
        return UnitTestData.getRandomPhrase();
    }

    public static String getRandomPhrase(final int nbWordMin, final int nbWordMax, final boolean isLabel) {
        return UnitTestData.getRandomPhrase(nbWordMin, nbWordMax, isLabel);
    }

    public static String getRandomSection() {
        return UnitTestData.getRandomSection();
    }

    public static String getRandomSection(final int nbLine, final int nbWordMin, final int nbWordMax) {
        return UnitTestData.getRandomSection(nbLine, nbWordMin, nbWordMax);
    }

    public static double getRandomDouble(final double min, final double max) {
        return UnitTestData.getRandomDouble(min, max);
    }

    public static int getRandomBetween(final int start, final int end) {
        return UnitTestData.getRandomBetween(start, end);
    }
}