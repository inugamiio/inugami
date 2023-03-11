package io.inugami.commons.test.helpers;

import io.inugami.api.exceptions.Asserts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YamlHelper {

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
        return loadYaml(path, CommonsHelper.UTF_8);
    }

    public static Map<String, Object> loadYaml(final String path, final Charset charset) {
        return loadYaml(Asserts.assertFileReadable(path), CommonsHelper.UTF_8);
    }

    public static Map<String, Object> loadYaml(final File file) {
        return loadYaml(file, CommonsHelper.UTF_8);
    }

    public static Map<String, Object> loadYaml(final File file, final Charset charset) {
        Asserts.assertNotNull("file mustn't be null", file);
        final String        yamlContent = FileReaderHelper.readFile(file, charset == null ? CommonsHelper.UTF_8 : charset);
        Map<String, Object> result      = new Yaml().load(yamlContent);
        return result;
    }

    public static <T> T loadYaml(final String path, final Charset charset, final Class<? extends T> objectClass) {
        Asserts.assertNotEmpty("file " + path + " mustn't be empty", path);
        return loadYaml(new File(path), charset, objectClass);
    }

    public static <T> T loadYaml(File file, Charset charset, Class<? extends T> objectClass) {
        Asserts.assertFileReadable(file);
        final String yamlContent = FileReaderHelper.readFile(file, charset == null ? CommonsHelper.UTF_8 : charset);
        T            result      = new Yaml().loadAs(yamlContent, objectClass);
        return result;
    }
}
