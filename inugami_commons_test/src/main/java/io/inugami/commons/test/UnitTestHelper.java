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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import flexjson.JSONSerializer;
import io.inugami.api.exceptions.*;
import io.inugami.api.loggers.Loggers;
import org.apache.commons.io.FileUtils;
import org.mockito.invocation.InvocationOnMock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UnitTestHelper {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Pattern UID_PATTERN = Pattern.compile("(?:[^_-]+[_-]){0,1}(?<uid>[0-9]+)");
    public static final  String  UTF_8       = "UTF-8";

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private UnitTestHelper() {
    }

    // =========================================================================
    // LOAD / READ FILE
    // =========================================================================
    public static String loadJsonReference(final String relativePath) {
        if (relativePath == null) {
            throw new RuntimeException("can't read file from null relative path!");
        }

        final File path = buildTestFilePath(relativePath.split("/"));
        return path.exists() ? readFile(path) : null;
    }

    public static String readFile(final File file) {
        String result = null;
        try {
            result = FileUtils.readFileToString(file, UTF_8);
        }
        catch (final IOException e) {
            throwException(e);
        }
        return result;
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
        final List<T> result        = new ArrayList<>();
        final Object  rawListValues = invocationOnMock.getArguments()[0];
        if (rawListValues != null && rawListValues instanceof Iterable) {
            final Iterable<E> values = (Iterable<E>) rawListValues;
            for (final E element : values) {
                T elementResult = null;
                if (element != null) {
                    final U uidObject = uidExtractor.apply(element);
                    elementResult = loadStub(basePath, objectType, refObjectType, cleanArgument(uidObject));
                }

                if (elementResult != null) {
                    result.add(elementResult);
                }
            }
        }

        return result.isEmpty() ? null : result;
    }


    public static <T> T loadObjectStrubByUid(final InvocationOnMock invocationOnMock,
                                             final String basePath,
                                             final String objectType,
                                             final TypeReference<T> refObjectType) {
        return loadObjectStrubByUid(invocationOnMock,
                                    (c) -> cleanArgument(c.getArguments()[0]),
                                    basePath,
                                    objectType,
                                    refObjectType);
    }

    private static Object cleanArgument(final Object argument) {
        Object result = null;
        if (argument != null && argument instanceof String) {
            final Matcher matcher = UID_PATTERN.matcher((String) argument);
            if (matcher.matches()) {
                result = matcher.group("uid");
            }
        }
        else {
            result = argument;
        }
        return result;
    }

    public static <T> T loadObjectStrubByUid(final InvocationOnMock invocationOnMock,
                                             final Function<InvocationOnMock, Object> uidExtractor,
                                             final String basePath,
                                             final String objectType,
                                             final TypeReference<T> refObjectType) {
        T            result    = null;
        final Object uidObject = uidExtractor.apply(invocationOnMock);
        result = loadStub(basePath, objectType, refObjectType, uidObject);

        return result;
    }

    private static <T> T loadStub(final String basePath, final String objectType, final TypeReference<T> refObjectType,
                                  final Object uidObject) {
        T result = null;
        if (uidObject != null) {
            final StringBuilder path = new StringBuilder(basePath).append(File.separator)
                                                                  .append(objectType)
                                                                  .append(".")
                                                                  .append(uidObject)
                                                                  .append(".json");
            result = UnitTestHelper.loadJson(path.toString(), refObjectType);
        }
        return result;
    }

    public static <T> T loadJsonFromResponse(final byte[] data, final TypeReference<T> refObjectType) {
        T result = null;
        if (data != null && refObjectType != null) {
            try {
                final String json = new String(data, UTF_8);
                result = convertFromJson(json, refObjectType);

            }
            catch (final Exception error) {
                throw new UncheckedException(error.getMessage(), error);
            }

        }
        return result;
    }

    // =========================================================================
    // OBJECT CONVERT
    // =========================================================================
    public static String convertToJson(final Object value) {
        String result = null;
        try {
            result = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
                                       .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
                                       .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                       .writeValueAsString(value);
        }
        catch (final Exception e) {
            throwException(e);
        }
        return result;
    }

    public static String forceConvertToJson(final Object value) {
        return new JSONSerializer().prettyPrint(true)
                                   .exclude("*.class")
                                   .deepSerialize(value);
    }

    public static String convertToJsonWithoutIndent(final Object value) {
        String result = null;
        try {
            result = new ObjectMapper().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
                                       .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                       .writeValueAsString(value);
        }
        catch (final Exception e) {
            throwException(e);
        }
        return result;
    }


    public static <T> T loadJson(final String path, final TypeReference<T> refObjectType) {
        Asserts.notNull(path, "can't load Json Object from null path");
        final String json = loadJsonReference(path);
        return convertFromJson(json, refObjectType);
    }

    public static <T> T convertFromJson(final String json, final TypeReference<T> refObjectType) {
        try {
            return json == null ? null : new ObjectMapper().readValue(json, refObjectType);
        }
        catch (final IOException e) {
            throw new UncheckedException(DefaultErrorCode.buildUndefineError(), e, e.getMessage());
        }
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    public static File buildTestFilePath(final String... filePathParts) {
        final List<String> parts = new ArrayList<>(Arrays.asList("src", "test", "resources"));
        Arrays.asList(filePathParts)
              .forEach(parts::add);
        return buildPath(parts.toArray(new String[]{}));
    }

    public static File buildPath(final String... parts) {
        final File basePath = new File(".");

        final String[] allPathParts = new String[parts.length + 1];
        allPathParts[0] = basePath.getAbsolutePath();
        System.arraycopy(parts, 0, allPathParts, 1, parts.length);

        return new File(String.join(File.separator, allPathParts));
    }

    // =========================================================================
    // EXCEPTIONS
    // =========================================================================
    private static void throwException(final Exception error) {
        throw new RuntimeException(error.getMessage(), error);
    }

    public static void assertJson(final Object value, final String jsonRef) {
        if (jsonRef == null) {
            Asserts.isNull(value, TestHelpersErrorCodes.MUST_BE_NULL);
        }
        else {
            Asserts.notNull(value, TestHelpersErrorCodes.MUST_BE_NOT_NULL);
            final String json = convertToJson(value);
            assertJson(jsonRef, json);
        }


    }

    public static void assertJson(final String jsonRef, final String json) {
        Asserts.notNull(jsonRef, TestHelpersErrorCodes.MUST_BE_NOT_NULL);
        Asserts.notNull(json, TestHelpersErrorCodes.MUST_BE_NOT_NULL);
        final String[] jsonValue = json.split("\n");
        final String[] refLines  = jsonRef.split("\n");

        try {
            Asserts.equalsObj(TestHelpersErrorCodes.MUST_BE_EQUALS, jsonValue.length, refLines.length);
        }
        catch (final Throwable e) {
            Loggers.DEBUG.error("\nactual :\n{}\nreference:\n{}\n----------", json, jsonRef);
            throw e;
        }


        for (int i = 0; i < refLines.length; i++) {
            try {
                Asserts.equalsObj(TestHelpersErrorCodes.MUST_BE_EQUALS, jsonValue[i].trim(), refLines[i].trim());
            }
            catch (final Throwable e) {
                Loggers.DEBUG.error("\nactual :\n{}\nreference:\n{}----------", json, jsonRef);
                throw e;
            }

        }
    }

    public static <T> void checkMapper(final String relativePath, final TypeReference<T> refObjectType,
                                       final Function<Object, Object> mapperFunction)
            throws IOException {
        Asserts.notNull(TestHelpersErrorCodes.MUST_BE_NOT_NULL, refObjectType);
        Asserts.notNull(TestHelpersErrorCodes.MUST_BE_NOT_NULL, relativePath);

        final String path = buildTestFilePath(relativePath.split("/")).getAbsolutePath()
                                                                      .replaceAll(Pattern.quote("[\\][.][\\]"), "\\");
        final File   jsonFolder     = new File(path);
        final String jsonFolderPath = jsonFolder.getAbsolutePath();

        Asserts.isTrue("File define isn't a folder:" + jsonFolderPath, jsonFolder.isDirectory());
        Asserts.isTrue("can't read folder:" + jsonFolderPath, jsonFolder.canRead());


        final String[] fileNames = jsonFolder.list();
        Asserts.notNull(fileNames, "no json found in folder :" + jsonFolderPath);

        final List<File> jsonLoaderFiles = Arrays.asList(fileNames)
                                                 .stream()
                                                 .filter(file -> file.endsWith("load.json"))
                                                 .map(fileName -> new File(String.join(File.separator, path, fileName)))
                                                 .collect(Collectors.toList());

        for (final File jsonLoaderFile : jsonLoaderFiles) {
            final String jsonLoader       = readFile(jsonLoaderFile);
            final String refJson          = readFile(buildRefJsonPath(jsonLoaderFile));
            String       mappedObjectJson = null;

            final Object initialResult = new ObjectMapper().readValue(jsonLoader, refObjectType);
            final Object mappedObject  = mapperFunction.apply(initialResult);
            try {
                Asserts.notNull(TestHelpersErrorCodes.MUST_BE_NOT_NULL, mappedObject);
                mappedObjectJson = convertToJson(mappedObject);
                Asserts.equalsObj(TestHelpersErrorCodes.MUST_BE_EQUALS, refJson, mappedObjectJson);
            }
            catch (final Exception error) {
                Loggers.DEBUG.error("\nref:\n---------\n{}current:\n---------\n{}\n---------",
                                    refJson,
                                    mappedObjectJson);
                throw error;
            }
        }
    }

    private static File buildRefJsonPath(final File jsonLoaderFile) {
        final String fullPath    = jsonLoaderFile.getAbsolutePath();
        final String usecasePath = fullPath.substring(0, fullPath.length() - ".load.json".length()) + ".json";
        return new File(usecasePath);
    }

    public static void assertThrowsError(final Class<? extends Exception> errorClass, final String errorMessage,
                                         final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new UncheckedException("method must throw exception", DefaultErrorCode.buildUndefineError());
        }
        catch (final Throwable error) {
            Asserts.notNull(errorClass, "error class is mandatory");
            Asserts.isTrue("error class isn't a " + errorClass.getName(), error.getClass()
                                                                               .isInstance(errorClass));
            Asserts.isTrue(String.format("current : \"%s\"  ref: \"%s\"", error.getMessage(), errorMessage),
                           error.getMessage()
                                .equals(errorMessage));
        }
    }

    public static void assertThrowsError(final ErrorCode errorCode, final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new UncheckedException(DefaultErrorCode.buildUndefineError(), "method must throw exception");
        }
        catch (final Throwable error) {
            Asserts.isTrue("error class isn't a  ExceptionWithErrorCode", error instanceof ExceptionWithErrorCode);

            final String currentErrorCode = ((ExceptionWithErrorCode) error).getErrorCode()
                                                                            .getErrorCode();

            Asserts.isTrue(String.format("current : \"%s\"  ref: \"%s\"", currentErrorCode, errorCode.getErrorCode()),
                           currentErrorCode.equals(errorCode.getErrorCode()));
        }
    }


}