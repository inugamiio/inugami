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
package io.inugami.framework.commons.testing.files;

import io.inugami.framework.commons.files.FilesUtils;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FilesUtilsTest {
    @TempDir
    Path tempDir;

    // =========================================================================
    // TESTS ASSERTIONS ET INFOS
    // =========================================================================
    @Test
    void should_validate_file_assertions() throws IOException {
        File folder = tempDir.resolve("subfolder").toFile();
        folder.mkdir();
        File file = new File(folder, "test.txt");
        file.createNewFile();

        FilesUtils.assertFileExists(file);
        FilesUtils.assertIsFile(file);
        FilesUtils.assertIsFolder(folder);
        FilesUtils.assertCanRead(file);
        FilesUtils.assertCanWrite(file);

        assertThat(FilesUtils.getCanonicalPath(file)).contains("test.txt");
    }

    @Test
    void should_get_content_type_and_length() throws IOException {
        File cssFile = tempDir.resolve("style.css").toFile();
        FilesUtils.write(".body { color: red; }", cssFile);

        assertThat(FilesUtils.getContentType(cssFile)).isEqualTo("text/css");
        assertThat(FilesUtils.getContentLength(cssFile)).isGreaterThan(0);
    }

    // =========================================================================
    // TESTS LECTURE / ECRITURE
    // =========================================================================
    @Test
    void should_read_line_by_line() throws IOException {
        File file = tempDir.resolve("lines.txt").toFile();
        FilesUtils.write("L1\nL2\nL3", file);

        List<String> lines = new ArrayList<>();
        FilesUtils.readLineByLine(file, lines::add);

        assertThat(lines).containsExactly("L1", "L2", "L3");
    }

    @Test
    void should_handle_binary_serialization() {
        File binFile = tempDir.resolve("data.bin").toFile();
        ArrayList<String> data = new ArrayList<>(List.of("one", "two"));

        FilesUtils.writeToBinary(binFile, data);
        List<String> result = FilesUtils.readFromBinary(binFile, new ArrayList<>());

        assertThat(result).containsExactly("one", "two");
    }

    // =========================================================================
    // TESTS CLASSPATH & PROPERTIES
    // =========================================================================
    @Test
    void should_read_from_classloader() {
        assertThatThrownBy(() -> FilesUtils.readFromClassLoader("non_existent.file"))
                .isInstanceOf(TechnicalException.class);
    }

    @Test
    void should_parse_properties() {
        String props = "key1=value1\nkey2=value2";
        Map<String, String> map = FilesUtils.readProperties(props);

        assertThat(map).hasSize(2).containsEntry("key1", "value1");
    }

    // =========================================================================
    // TESTS SCAN ET LISTE
    // =========================================================================
    @Test
    void should_list_and_scan_files() throws IOException {
        File root = tempDir.toFile();
        FilesUtils.write("c", new File(root, "test.log"));
        File sub = new File(root, "sub");
        sub.mkdir();
        FilesUtils.write("c", new File(sub, "other.log"));

        List<File> logs = FilesUtils.scanFilesystem(root, (dir, name) -> name.endsWith(".log"));
        assertThat(logs).hasSize(2);

        List<File> simpleList = FilesUtils.list(root);
        assertThat(simpleList).extracting(File::getName).contains("test.log", "sub");
    }

    // =========================================================================
    // TESTS PATH ET TOOLS
    // =========================================================================
    @Test
    void should_build_paths() {
        File base = new File("/tmp");
        File build = FilesUtils.buildFile(base, "a", "b.txt");

        assertThat(build.getPath()).contains("a").contains("b.txt");
    }

    @Test
    void should_detect_absolute_and_windows_paths() {
        if (FilesUtils.isWindows()) {
            assertThat(FilesUtils.isAbsoluteFile("C:\\Windows")).isTrue();
        } else {
            assertThat(FilesUtils.isAbsoluteFile("/etc/hosts")).isTrue();
        }
        assertThat(FilesUtils.isAbsoluteFile("relative/path")).isFalse();
    }

}