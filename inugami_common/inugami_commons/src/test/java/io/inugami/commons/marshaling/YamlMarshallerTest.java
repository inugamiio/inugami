package io.inugami.commons.marshaling;

import com.fasterxml.jackson.databind.JsonNode;
import io.inugami.commons.files.FilesUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings({"java:S5838"})
class YamlMarshallerTest {
    // =========================================================================
    // TEST
    // =========================================================================
    @Test
    void convertFromYaml_nominal() throws IOException {
        final String  content = loadContent("commons/marshaling/yamlMarshallerTest/convertFromYaml.yaml");
        final UserDTO user    = YamlMarshaller.getInstance().convertFromYaml(content, UserDTO.class);
        assertThat(user).isNotNull();
        assertThat(user.getPreferences()).isNotNull();
        assertThat(user.getPreferences().size()).isEqualTo(1);
    }

    @Test
    void testConvertFromYaml_nominal() throws IOException {
        final String   content = loadContent("commons/marshaling/yamlMarshallerTest/convertFromYaml.yaml");
        final JsonNode node    = YamlMarshaller.getInstance().convertFromYaml(content);
        assertThat(node).isNotNull();
    }

    @Test
    void convertToYaml_nominal() throws IOException {
        final String  content = loadContent("commons/marshaling/yamlMarshallerTest/convertFromYaml.yaml");
        final UserDTO user    = YamlMarshaller.getInstance().convertFromYaml(content, UserDTO.class);
        final String  yaml    = YamlMarshaller.getInstance().convertToYaml(user);

        assertThat(yaml).isEqualTo("---\n" +
                                   "creationDate: \"2023-04-08T08:17:00\"\n" +
                                   "firstName: \"John\"\n" +
                                   "id: 1\n" +
                                   "lastName: \"Smith\"\n" +
                                   "preferences:\n" +
                                   "- description: \"trail\"\n" +
                                   "  type: \"motocycle\"\n" +
                                   "");
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private String loadContent(final String path) throws IOException {
        return FilesUtils.readContent(FilesUtils.buildFile(new File("."), "src", "test", "resources", path));
    }

}