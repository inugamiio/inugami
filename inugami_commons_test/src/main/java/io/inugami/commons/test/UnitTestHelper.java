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
import io.inugami.api.exceptions.*;
import io.inugami.commons.test.helpers.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mockito.invocation.InvocationOnMock;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnitTestHelper {
    // =========================================================================
    // LOAD / READ FILE
    // =========================================================================
    @Deprecated
    public static String loadJsonReference(final String relativePath) {
        return FileReaderHelper.loadRelativeFile(relativePath);
    }

    /**
     * Allows to read relative test file in <strong>src/test/resources</strong> folder
     * By default the encoding charset is UTF-8
     *
     * @param relativePath the path relative to test resources folder
     * @return file content
     */
    public static String loadRelativeFile(final String relativePath) {
        return FileReaderHelper.loadRelativeFile(relativePath);
    }

    /**
     * Allows to read relative test file in <strong>src/test/resources</strong> folder
     *
     * @param relativePath the path relative to test resources folder
     * @param charset      file encoding charset
     * @return file content
     */
    public static String loadRelativeFile(final String relativePath, final Charset charset) {
        return FileReaderHelper.loadRelativeFile(relativePath, charset);
    }

    /**
     * Allows to read  file. By default, the encoding charset is UTF-8
     *
     * @param file the file to read
     * @return file content
     */
    public static String readFile(final File file) {
        return FileReaderHelper.readFile(file);
    }

    /**
     * Allows to read  file. By default the encoding charset is UTF-8
     *
     * @param file    the file to read
     * @param charset file encoding charset
     * @return file content
     */
    public static String readFile(final File file, final Charset charset) {
        return FileReaderHelper.readFile(file, charset);
    }

    // =========================================================================
    // LOAD Object Stub by uid
    // =========================================================================
    public static <T, E, U extends Object> List<T> loadListObjectStrubByUid(final InvocationOnMock invocationOnMock,
                                                                            final String basePath,
                                                                            final String objectType,
                                                                            final TypeReference<T> refObjectType) {
        return loadListObjectStrubByUid(invocationOnMock, (value) -> value, basePath, objectType, refObjectType);
    }

    public static <T, E, U extends Object> List<T> loadListObjectStrubByUid(final InvocationOnMock invocationOnMock,
                                                                            final Function<E, U> uidExtractor,
                                                                            final String basePath,
                                                                            final String objectType,
                                                                            final TypeReference<T> refObjectType) {
        return StubHelper.loadListObjectStrubByUid(invocationOnMock, uidExtractor, basePath, objectType, refObjectType);
    }


    public static <T> T loadObjectStrubByUid(final InvocationOnMock invocationOnMock,
                                             final String basePath,
                                             final String objectType,
                                             final TypeReference<T> refObjectType) {
        return StubHelper.loadObjectStrubByUid(invocationOnMock, basePath, objectType, refObjectType);
    }


    public static <T> T loadObjectStrubByUid(final InvocationOnMock invocationOnMock,
                                             final Function<InvocationOnMock, Object> uidExtractor,
                                             final String basePath,
                                             final String objectType,
                                             final TypeReference<T> refObjectType) {
        return StubHelper.loadObjectStrubByUid(invocationOnMock, uidExtractor, basePath, objectType, refObjectType);
    }


    // =========================================================================
    // CONVERT TO JSON
    // =========================================================================


    public static String convertToJson(final Object value) {
        return JsonHelper.convertToJson(value);
    }

    public static String forceConvertToJson(final Object value) {
        return JsonHelper.forceConvertToJson(value);
    }

    public static String convertToJsonWithoutIndent(final Object value) {
        return JsonHelper.convertToJsonWithoutIndent(value);
    }


    public static <T> T loadJson(final String path, final TypeReference<T> refObjectType) {
        return JsonHelper.loadJson(path, refObjectType);
    }


    @Deprecated
    public static <T> T loadJsonFromResponse(final byte[] data, final TypeReference<T> refObjectType) {
        return JsonHelper.convertFromJson(data, refObjectType);
    }


    public static <T> T convertFromJson(final byte[] data, final TypeReference<T> refObjectType) {
        return JsonHelper.convertFromJson(data, refObjectType);
    }

    public static <T> T convertFromJson(final String json, final TypeReference<T> refObjectType) {
        return JsonHelper.convertFromJson(json, refObjectType);
    }

    // =========================================================================
    // YAML
    // =========================================================================
    public static Map<String, Object> loadRelativeYaml(final String relativePath) {
        return loadRelativeYaml(relativePath, CommonsHelper.UTF_8);
    }

    public static Map<String, Object> loadRelativeYaml(final String relativePath, final Charset charset) {
        Asserts.assertNotEmpty(relativePath);
        return loadYaml(PathHelper.buildTestFilePath(relativePath), charset);
    }

    public static <T> T loadRelativeYaml(final String relativePath, Class<? extends T> objectClass) {
        return loadRelativeYaml(relativePath, CommonsHelper.UTF_8, objectClass);
    }

    public static <T> T loadRelativeYaml(final String relativePath, final Charset charset, final Class<? extends T> objectClass) {
        Asserts.assertNotEmpty(relativePath);
        return loadYaml(PathHelper.buildTestFilePath(relativePath), charset, objectClass);
    }

    //  =======================================================================
    // LOAD YAML
    //  =======================================================================
    public static Map<String, Object> loadYaml(final String path) {
        return YamlHelper.loadYaml(path);
    }

    public static Map<String, Object> loadYaml(final String path, final Charset charset) {
        return YamlHelper.loadYaml(path, charset);
    }

    public static Map<String, Object> loadYaml(final File file) {
        return YamlHelper.loadYaml(file);
    }

    public static Map<String, Object> loadYaml(final File file, final Charset charset) {
        return YamlHelper.loadYaml(file, charset);
    }
    public static <T> T loadYaml(final String path, final Class<? extends T> objectClass) {
        return YamlHelper.loadYaml(path, CommonsHelper.UTF_8, objectClass);
    }

    public static <T> T loadYaml(final String path, final Charset charset, final Class<? extends T> objectClass) {
        return YamlHelper.loadYaml(path, charset, objectClass);
    }
    public static <T> T loadYaml(File file, Class<? extends T> objectClass) {
        return YamlHelper.loadYaml(file, CommonsHelper.UTF_8, objectClass);
    }
    public static <T> T loadYaml(File file, Charset charset, Class<? extends T> objectClass) {
        return YamlHelper.loadYaml(file, charset, objectClass);
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    public static File buildTestFilePath(final String... filePathParts) {
        return PathHelper.buildTestFilePath(filePathParts);
    }

    public static File buildPath(final String... parts) {
        return PathHelper.buildPath(parts);
    }

    // =========================================================================
    // CHECK MAPPER
    // =========================================================================


    public static <T> void checkMapper(final String relativePath, final TypeReference<T> refObjectType, final Function<Object, Object> mapperFunction) throws IOException {
        MapperHelper.checkMapper(relativePath, refObjectType, mapperFunction);
    }

    // =========================================================================
    // ASSERT JSON / TEXT
    // =========================================================================
    public static void assertJsonSkipLine(final Object value, final String jsonRef, final int... skipLines) {
        AssertionHelper.assertJson(value, jsonRef, LineAssertionHelper.buildSkipLine(skipLines));
    }

    public static void assertJson(final Object value, final String jsonRef, final LineAssertion... lineAssertions) {
        AssertionHelper.assertText(jsonRef, convertToJson(value), lineAssertions);
    }

    public static void assertJsonSkipLine(final String jsonRef, final String json, final int... skipLines) {
        AssertionHelper.assertJson(jsonRef, json, LineAssertionHelper.buildSkipLine(skipLines));
    }

    public static void assertJson(final String jsonRef, final String json, final LineAssertion... lineAssertions) {
        AssertionHelper.assertText(jsonRef, json, lineAssertions);
    }

    @Deprecated
    public static void assertTextRelatif(final String value, final String path, final LineAssertion... lineAssertions) {
        AssertionHelper.assertTextRelative(value, path, lineAssertions);
    }

    public static void assertTextRelativeSkipLine(final String value, final String path, final int... skipLines) {
        AssertionHelper.assertTextRelative(value, path, skipLines);
    }

    public static void assertTextRelative(final String value, final String path, final LineAssertion... lineAssertions) {
        AssertionHelper.assertTextRelative(value, path, lineAssertions);
    }

    @Deprecated
    public static void assertTextRelatif(final Object value, final String path, final LineAssertion... lineAssertions) {
        AssertionHelper.assertTextRelative(path, value, lineAssertions);
    }

    public static void assertTextRelativeSkipLine(final Object value, final String path, final int... skipLines) {
        AssertionHelper.assertTextRelative(path, value, skipLines);
    }

    public static void assertTextRelative(final Object value, final String path, final LineAssertion... lineAssertions) {
        AssertionHelper.assertTextRelative(path, value, lineAssertions);
    }

    public static void assertTextSkipLine(final Object value, final String jsonRef, final int... skipLines) {
        AssertionHelper.assertText(jsonRef, value, skipLines);
    }

    public static void assertText(final Object value, final String jsonRef, final LineAssertion... lineAssertions) {
        AssertionHelper.assertText(jsonRef, value, lineAssertions);
    }

    public static void assertTextSkipLine(final String jsonRef, final String json, int... skipLines) {
        AssertionHelper.assertText(jsonRef, json, skipLines);
    }

    public static void assertText(final String jsonRef, final String json, LineAssertion... lineAssertions) {
        AssertionHelper.assertText(jsonRef, json, lineAssertions);
    }


    // =========================================================================
    // ASSERT THROWS ERROR
    // =========================================================================
    public static void assertThrowsError(final Class<? extends Exception> errorClass, final String errorMessage,
                                         final ExecutableFunction handler) {
        ThrowErrorHelper.assertThrowsError(errorClass, errorMessage, handler);
    }

    public static void assertThrowsError(final ErrorCode errorCode, final ExecutableFunction handler) {
        ThrowErrorHelper.assertThrowsError(errorCode, handler);
    }
}