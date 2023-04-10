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
import io.inugami.api.exceptions.ErrorCode;
import io.inugami.commons.test.api.LineMatcher;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mockito.invocation.InvocationOnMock;

import java.io.File;
import java.util.List;
import java.util.function.Function;

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
        UnitTestHelperException.assertThrows(errorCode, handler);
    }

    public static void throwException(final Exception error) {
        UnitTestHelperException.throwException(error);
    }
}