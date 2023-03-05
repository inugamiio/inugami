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
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.tools.ConsoleColors;
import io.inugami.commons.marshaling.JsonMarshaller;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnitTestHelper {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Pattern UID_PATTERN = Pattern.compile("(?:[^_-]+[_-]){0,1}(?<uid>[0-9]+)");
    public static final  String  UTF_8       = "UTF-8";
    public static final  String  NULL        = "null";


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
        } catch (final IOException e) {
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
        } else {
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

            } catch (final Exception error) {
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
        if (value == null) {
            return NULL;
        }
        try {
            result = JsonMarshaller.getInstance()
                                   .getIndentedObjectMapper()
                                   .writeValueAsString(value);
        } catch (final Exception e) {
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
            result = JsonMarshaller.getInstance()
                                   .getDefaultObjectMapper()
                                   .writeValueAsString(value);
        } catch (final Exception e) {
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
        } catch (final IOException e) {
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


    public static <T> void checkMapper(final String relativePath,
                                       final TypeReference<T> refObjectType,
                                       final Function<Object, Object> mapperFunction)
            throws IOException {
        Asserts.assertNotNull(TestHelpersErrorCodes.MUST_BE_NOT_NULL, refObjectType);
        Asserts.assertNotNull(TestHelpersErrorCodes.MUST_BE_NOT_NULL, relativePath);

        final String path = buildTestFilePath(relativePath.split("/")).getAbsolutePath()
                                                                      .replaceAll(Pattern.quote("[\\][.][\\]"), "\\");
        final File   jsonFolder     = new File(path);
        final String jsonFolderPath = jsonFolder.getAbsolutePath();

        Asserts.assertTrue("File define isn't a folder:" + jsonFolderPath, jsonFolder.isDirectory());
        Asserts.assertTrue("can't read folder:" + jsonFolderPath, jsonFolder.canRead());


        final String[] fileNames = jsonFolder.list();
        Asserts.assertNotNull(fileNames, "no json found in folder :" + jsonFolderPath);

        final List<File> jsonLoaderFiles = Arrays.asList(fileNames)
                                                 .stream()
                                                 .filter(file -> file.endsWith("load.json"))
                                                 .map(fileName -> new File(String.join(File.separator, path, fileName)))
                                                 .collect(Collectors.toList());

        for (final File jsonLoaderFile : jsonLoaderFiles) {
            final String jsonLoader       = readFile(jsonLoaderFile);
            final String refJson          = readFile(buildRefJsonPath(jsonLoaderFile));
            final String mappedObjectJson = null;

            final Object initialResult = new ObjectMapper().readValue(jsonLoader, refObjectType);
            final Object mappedObject  = mapperFunction.apply(initialResult);
            try {
                Asserts.assertNotNull(TestHelpersErrorCodes.MUST_BE_NOT_NULL, mappedObject);
                assertText(mappedObject, refJson);
            } catch (final Exception error) {
                Loggers.DEBUG.error("\nref:\n---------\n{}current:\n---------\n{}\n---------",
                                    refJson,
                                    mappedObjectJson);
                throw error;
            }
        }
    }

    public static void assertJson(final Object value, final String jsonRef) {
        assertText(jsonRef, convertToJson(value));
    }

    public static void assertJson(final String jsonRef, final String json) {
        assertText(jsonRef, json);
    }

    public static void assertTextRelatif(final String value, final String path) {
        final String refJson = loadJsonReference(path);
        assertText(refJson, value);
    }

    public static void assertTextRelatif(final Object value, final String path) {
        final String refJson = loadJsonReference(path);
        assertText(value, refJson);
    }

    public static void assertText(final Object value, final String jsonRef) {
        if (jsonRef == null) {
            Asserts.assertNull("json must be null", value);
        } else {
            Asserts.assertNotNull("json mustn't be null", value);
            final String json = convertToJson(value);
            assertText(jsonRef, json);
        }


    }

    public static void assertText(final String jsonRef, final String json, LineAssertion... lineAssertions) {
        Asserts.assertNotNull("json ref mustn't be null", jsonRef);
        Asserts.assertNotNull("json mustn't be null", json);
        final String[] jsonValue = json.split("\n");
        final String[] refLines  = jsonRef.split("\n");

        try {
            if (jsonValue.length != refLines.length) {
                Loggers.DEBUG.error(renderDiff(jsonValue, refLines));
            }
            Asserts.assertTrue(
                    String.format("reference and json have not same size : %s,%s", jsonValue.length, refLines.length),
                    jsonValue.length == refLines.length);
        } catch (final Throwable e) {
            throw e;
        }


        for (int i = 0; i < refLines.length; i++) {


            final String currentJsonValue = jsonValue[i].trim();
            final String currentRefLine   = refLines[i].trim();
            final LineAssertion lineAssertion = searchLineAssertion(i, currentJsonValue, lineAssertions);

            if (lineAssertion == null) {
                if (!currentJsonValue.equals(currentRefLine)) {
                    Loggers.DEBUG.error(renderDiff(jsonValue, refLines));
                    throw new RuntimeException(
                            String.format("[%s] %s != %s", i + 1, jsonValue[i].trim(), refLines[i].trim()));
                }
            } else {
                if (lineAssertion.isSkipped(currentJsonValue)) {
                    continue;
                } else if (!lineAssertion.isMatching(currentJsonValue, currentRefLine)) {
                    Loggers.DEBUG.error(renderDiff(jsonValue, refLines));
                    throw new RuntimeException(
                            String.format("[%s] %s != %s", i + 1, jsonValue[i].trim(), refLines[i].trim()));
                }
            }


        }
    }

    private static LineAssertion searchLineAssertion(final int lineNumber,
                                                     final String line,
                                                     final LineAssertion[] lineAssertions) {
        if (lineAssertions != null) {
            for (LineAssertion lineAssertion : lineAssertions) {
                if (lineAssertion.accept(lineNumber, line)) {
                    return lineAssertion;
                }
            }
        }
        return null;
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
        } catch (final Throwable error) {
            Asserts.assertNotNull(errorClass, "error class is mandatory");
            Asserts.assertTrue("error class isn't a " + errorClass.getName(), error.getClass()
                                                                                   .isInstance(errorClass));
            Asserts.assertTrue(String.format("current : \"%s\"  ref: \"%s\"", error.getMessage(), errorMessage),
                               error.getMessage()
                                    .equals(errorMessage));
        }
    }

    public static void assertThrowsError(final ErrorCode errorCode, final ExecutableFunction handler) {
        try {
            handler.execute();
            throw new UncheckedException(DefaultErrorCode.buildUndefineError(), "method must throw exception");
        } catch (final Throwable error) {
            Asserts.assertTrue("error class isn't a  ExceptionWithErrorCode", error instanceof ExceptionWithErrorCode);

            final String currentErrorCode = ((ExceptionWithErrorCode) error).getErrorCode()
                                                                            .getErrorCode();

            Asserts.assertTrue(String.format("current : \"%s\"  ref: \"%s\"", currentErrorCode, errorCode.getErrorCode()),
                               currentErrorCode.equals(errorCode.getErrorCode()));
        }
    }

    private static String renderDiff(final String[] jsonValue, final String[] refLines) {
        final JsonBuilder writer = new JsonBuilder();
        writer.line();
        writer.write("Current:").line().write(ConsoleColors.createLine("-", 80)).line();
        writer.write(String.join("\n", jsonValue)).line();
        writer.write(ConsoleColors.createLine("-", 80));

        writer.line().line();
        final int    currentNbLine    = jsonValue.length;
        final int    refNbLines       = refLines.length;
        final int    nbLines          = jsonValue.length > refLines.length ? jsonValue.length : refLines.length;
        final int    lineColumn       = String.valueOf(nbLines).length() + 2;
        final int    maxCurrentColumn = computeMaxColumnSize(jsonValue);
        final int    maxRefColumn     = computeMaxColumnSize(refLines);
        final String lineDeco         = " |";
        final String diffMiddleOk     = "     ";
        final String diffMiddleKO     = " <-> ";
        final int    fullSize         = lineColumn + maxCurrentColumn + diffMiddleOk.length() + maxRefColumn;

        writer.write(ConsoleColors.createLine("=", fullSize)).line();
        writer.write(ConsoleColors.createLine(" ", lineColumn)).write(lineDeco);
        writer.write(writeCenter("Current", maxCurrentColumn));
        writer.write(diffMiddleOk);
        writer.write(writeCenter("Reference", maxRefColumn));
        writer.line();
        writer.write(ConsoleColors.createLine("=", fullSize)).line();

        for (int i = 0; i < nbLines; i++) {
            final String  currentLine = i >= currentNbLine ? "" : jsonValue[i];
            final String  refLine     = i >= refNbLines ? "" : refLines[i];
            final boolean diff        = !currentLine.trim().equals(refLine.trim());
            if (diff) {
                writer.write(ConsoleColors.RED);
            }
            writer.write(i).write(ConsoleColors.createLine(" ", lineColumn - String.valueOf(i).length()));
            writer.write(lineDeco);
            writer.write(currentLine).write(ConsoleColors.createLine(" ", maxCurrentColumn - currentLine.length()));
            writer.write(diff ? diffMiddleKO : diffMiddleOk);
            writer.write(refLine);
            if (diff) {
                writer.write(ConsoleColors.RESET);
            }
            writer.line();
        }

        return writer.toString();
    }


    private static int computeMaxColumnSize(final String[] lines) {
        int result = 20;
        if (lines != null) {
            for (final String line : lines) {
                if (line.length() > result) {
                    result = line.length();
                }
            }
        }
        return result;
    }

    private static String writeCenter(final String title, final int columnSize) {
        final StringBuilder result = new StringBuilder();
        if (columnSize < title.length()) {
            result.append(title.substring(0, columnSize));
        } else {
            final int titleOffset = title.length() / 2;

            result.append(ConsoleColors.createLine(" ", titleOffset));
            result.append(title);
            result.append(ConsoleColors.createLine(" ", columnSize - (titleOffset + title.length())));
        }
        return result.toString();
    }
}