package io.inugami.api.tools.unit.test;

import io.inugami.api.exceptions.Asserts;
import io.inugami.interfaces.exceptions.MessagesFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mockito.invocation.InvocationOnMock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperStub {

    private static final Map<String, List<?>> CACHE             = new ConcurrentHashMap<>();
    private static final String               STUB_FILE_PATTERN = "[.-_][0-9]+[.](json|yaml)";

    // ========================================================================
    // API
    // ========================================================================
    static <T, ID> T loadIntegrationTestStub(final InvocationOnMock answer,
                                             final String folder,
                                             final Function<T, ID> getId,
                                             final Class<? extends T> userDtoClass) {
        Asserts.assertNotNull("stub folder is required", folder);
        return loadStub(answer, UnitTestHelperPath.buildIntegrationTestFilePath(folder), getId, null, userDtoClass, null);
    }

    static <T, ID> T loadIntegrationTestStub(final InvocationOnMock answer,
                                             final String folder,
                                             final Function<T, ID> getId,
                                             final Function<Object[], ID> idResolver,
                                             final Class<? extends T> userDtoClass) {
        Asserts.assertNotNull("stub folder is required", folder);
        return loadStub(answer, UnitTestHelperPath.buildIntegrationTestFilePath(folder), getId, idResolver, userDtoClass, null);
    }

    static <T, ID> T loadIntegrationTestStub(final InvocationOnMock answer,
                                             final String folder,
                                             final Function<T, ID> getId,
                                             final Class<? extends T> userDtoClass,
                                             final List<T> cache) {
        Asserts.assertNotNull("stub folder is required", folder);
        return loadStub(answer, UnitTestHelperPath.buildIntegrationTestFilePath(folder), getId, null, userDtoClass, cache);
    }

    static <T, ID> T loadIntegrationTestStub(final InvocationOnMock answer,
                                             final String folder,
                                             final Function<T, ID> getId,
                                             final Function<Object[], ID> idResolver,
                                             final Class<? extends T> userDtoClass,
                                             final List<T> cache) {
        Asserts.assertNotNull("stub folder is required", folder);
        return loadStub(answer, UnitTestHelperPath.buildIntegrationTestFilePath(folder), getId, idResolver, userDtoClass, cache);
    }


    static <T, ID> T loadRelativeStub(final InvocationOnMock answer,
                                      final String folder,
                                      final Function<T, ID> getId,
                                      final Class<? extends T> userDtoClass) {
        Asserts.assertNotNull("stub folder is required", folder);
        return loadStub(answer, UnitTestHelperPath.buildTestFilePath(folder), getId, null, userDtoClass, null);
    }

    static <T, ID> T loadRelativeStub(final InvocationOnMock answer,
                                      final String folder,
                                      final Function<T, ID> getId,
                                      final Function<Object[], ID> idResolver,
                                      final Class<? extends T> userDtoClass) {
        Asserts.assertNotNull("stub folder is required", folder);
        return loadStub(answer, UnitTestHelperPath.buildTestFilePath(folder), getId, idResolver, userDtoClass, null);
    }

    static <T, ID> T loadRelativeStub(final InvocationOnMock answer,
                                      final String folder,
                                      final Function<T, ID> getId,
                                      final Class<? extends T> userDtoClass,
                                      final List<T> cache) {
        Asserts.assertNotNull("stub folder is required", folder);
        return loadStub(answer, UnitTestHelperPath.buildTestFilePath(folder), getId, null, userDtoClass, cache);
    }

    static <T, ID> T loadRelativeStub(final InvocationOnMock answer,
                                      final String folder,
                                      final Function<T, ID> getId,
                                      final Function<Object[], ID> idResolver,
                                      final Class<? extends T> userDtoClass,
                                      final List<T> cache) {
        Asserts.assertNotNull("stub folder is required", folder);
        return loadStub(answer, UnitTestHelperPath.buildTestFilePath(folder), getId, idResolver, userDtoClass, cache);
    }

    // ========================================================================
    // LOAD STUB
    // ========================================================================
    private static <T, ID> T loadStub(final InvocationOnMock answer,
                                      final File folder,
                                      final Function<T, ID> getId,
                                      final Function<Object[], ID> idResolver,
                                      final Class<? extends T> objectClass,
                                      final List<T> cache) {

        Asserts.assertNotNull("stub folder is required", folder);
        Asserts.assertNotNull("id extractor is required", getId);
        Asserts.assertNotNull("object class is required", objectClass);

        if (answer == null || answer.getArguments() == null) {
            return null;
        }
        final File fullPath = UnitTestHelperPath.resolveFullPath(folder);

        List<T> values = null;
        if (cache == null) {
            final String cacheKey = fullPath.getAbsolutePath() + "_" + objectClass;
            values = (List<T>) CACHE.get(cacheKey);
            if (values == null) {
                values = loadStub(fullPath, objectClass);
                CACHE.put(cacheKey, values);
            }
        } else {
            if (cache.isEmpty()) {
                values = loadStub(fullPath, objectClass);
                cache.addAll(values);
            } else {
                values = cache;
            }
        }

        Asserts.assertNotEmpty(() -> MessagesFormatter.format("no stub for class : {0} in folder : {1}", objectClass.getSimpleName(), fullPath), values);

        if (answer.getArguments() == null) {
            return null;
        }

        final ID currentId = resolveId(answer.getArguments(), idResolver);
        if (currentId == null) {
            return null;
        }

        for (final Object value : values) {
            final T  stub = (T) value;
            final ID id   = getId.apply(stub);
            if (String.valueOf(currentId).equals(String.valueOf(id))) {
                return stub;
            }
        }
        return null;
    }

    private static <ID> ID resolveId(final Object[] arguments, final Function<Object[], ID> idResolver) {
        if (idResolver == null) {
            return (ID) arguments[0];
        } else {
            return idResolver.apply(arguments);
        }
    }

    private static <T> List<T> loadStub(final File fullPath,
                                        final Class<? extends T> userDtoClass) {

        Asserts.assertFolderExists(fullPath);
        final List<T> result      = new ArrayList<>();
        final Pattern filePattern = Pattern.compile(cleanObjectName(userDtoClass.getSimpleName()) + STUB_FILE_PATTERN);

        for (final String filename : fullPath.list()) {
            if (isStubFile(filename, filePattern)) {
                result.add(loadStubObject(fullPath, filename, userDtoClass));
            }
        }

        return result;
    }

    private static String cleanObjectName(final String simpleName) {
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }

    private static boolean isStubFile(final String filename, final Pattern filePattern) {
        return filePattern.matcher(filename).matches();
    }

    private static <T> T loadStubObject(final File fullPath,
                                        final String filename,
                                        final Class<? extends T> userDtoClass) {

        T          result   = null;
        final File filePath = new File(fullPath.getAbsolutePath() + File.separator + filename);
        if (filename.endsWith("json")) {
            result = loadJson(filePath, userDtoClass);
        } else if (filename.endsWith("yaml")) {
            result = loadYaml(filePath, userDtoClass);
        }
        Asserts.assertNotNull(() -> "unable to load stub " + filePath, result);
        return result;
    }

    private static <T> T loadJson(final File filePath, final Class<? extends T> userDtoClass) {
        final String content = UnitTestHelperFile.readFile(filePath);
        return UnitTestHelperJson.convertFromJson(content, userDtoClass);
    }

    private static <T> T loadYaml(final File filePath, final Class<? extends T> userDtoClass) {
        final String content = UnitTestHelperFile.readFile(filePath);
        return UnitTestHelperYaml.convertFromYaml(content, userDtoClass);
    }


}
