package io.inugami.commons.test.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.loggers.Loggers;
import io.inugami.commons.test.LineAssertion;
import io.inugami.commons.test.TestHelpersErrorCodes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class  MapperHelper {
    public static <T> void checkMapper(final String relativePath,
                                       final TypeReference<T> refObjectType,
                                       final Function<Object, Object> mapperFunction)
            throws IOException {
        Asserts.assertNotNull(TestHelpersErrorCodes.MUST_BE_NOT_NULL, refObjectType);
        Asserts.assertNotNull(TestHelpersErrorCodes.MUST_BE_NOT_NULL, relativePath);

        final String path = PathHelper.buildTestFilePath(relativePath.split("/")).getAbsolutePath()
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
            final String jsonLoader       = FileReaderHelper.readFile(jsonLoaderFile);
            final String refJson          = FileReaderHelper.readFile(PathHelper.buildRefJsonPath(jsonLoaderFile));
            final String mappedObjectJson = null;

            final Object initialResult = new ObjectMapper().readValue(jsonLoader, refObjectType);
            final Object mappedObject  = mapperFunction.apply(initialResult);
            try {
                Asserts.assertNotNull(TestHelpersErrorCodes.MUST_BE_NOT_NULL, mappedObject);
                AssertionHelper.assertText(refJson, mappedObject,  new LineAssertion[]{});
            } catch (final Exception error) {
                Loggers.DEBUG.error("\nref:\n---------\n{}current:\n---------\n{}\n---------",
                                    refJson,
                                    mappedObjectJson);
                throw error;
            }
        }
    }
}
