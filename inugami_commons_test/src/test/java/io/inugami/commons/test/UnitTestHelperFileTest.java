package io.inugami.commons.test;

import org.junit.jupiter.api.Test;

import java.io.File;

import static io.inugami.commons.test.UnitTestHelperException.assertThrows;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UnitTestHelperFileTest {

    @Test
    public void readFileIntegration() {
        final String value = UnitTestHelperFile.readFileIntegration("test/UnitTestHelperFile/readFileIntegration.txt");
        assertThat(value).isEqualTo("hello");
    }

    @Test
    public void readFileIntegration_withError() {
        assertThrows(() -> UnitTestHelperFile.readFileIntegration("test/UnitTestHelperFile/readFileIntegration.notExists.txt"));
    }

    @Test
    public void readFileRelative_nominal() {
        final String value = UnitTestHelperFile.readFileRelative("test/UnitTestHelperFile/readFileRelative.txt");
        assertThat(value).isEqualTo("hello");
    }

    @Test
    public void readFileRelative_withNullValue_shouldThrow() {
        assertThrows(RuntimeException.class,
                     "can't read file from null relative path!",
                     () -> UnitTestHelperFile.readFileRelative(null));
    }


    @Test
    public void readFile_nominal() {
        final File file = UnitTestHelperPath.buildTestFilePath("test", "UnitTestHelperFile", "readFileRelative.txt");
        assertThat(UnitTestHelperFile.readFile(file)).isEqualTo("hello");
        assertThat(UnitTestHelperFile.readFile(file.getAbsolutePath())).isEqualTo("hello");
    }

    @Test
    public void readFile_withNullValue_shouldReturnNull() {
        final File file = null;
        assertThat(UnitTestHelperFile.readFile(file)).isNull();
        final String path = null;
        assertThat(UnitTestHelperFile.readFile(path)).isNull();

    }
}