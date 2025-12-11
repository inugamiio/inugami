package io.inugami.framework.api.marshalling;

import com.fasterxml.jackson.core.type.TypeReference;
import io.inugami.framework.api.tools.unit.test.UnitTestData;
import io.inugami.framework.api.tools.unit.test.dto.UserDataDTO;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class YamlMarshallerTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final TypeReference<UserDataDTO> TYPE         = new TypeReference<UserDataDTO>() {
    };
    public static final  String                     YAML_NOMINAL = """
            ---
            birthday: "1988-04-12"
            canton: "VD"
            city: "Cheseaux-sur-Lausanne"
            deviceIdentifier: "401f0498-c43f-43ad-a3f4-2888838332ad"
            email: "emilie.lalonde@mock.org"
            firstName: "Émilie"
            id: 1
            lastName: "Lalonde"
            nationality: "CH"
            old: 35
            phoneNumber: "0615031522"
            sex: "FEMALE"
            socialId: "7564971247732"
            streetName: "du Château"
            streetNumber: "10"
            streetType: "Chem."
            zipCode: "1033"
            """;

    public static final String NOMINAL = """
            {
              "birthday" : "1988-04-12",
              "canton" : "VD",
              "city" : "Cheseaux-sur-Lausanne",
              "deviceIdentifier" : "401f0498-c43f-43ad-a3f4-2888838332ad",
              "email" : "emilie.lalonde@mock.org",
              "firstName" : "Émilie",
              "id" : 1,
              "lastName" : "Lalonde",
              "nationality" : "CH",
              "old" : 35,
              "phoneNumber" : "0615031522",
              "sex" : "FEMALE",
              "socialId" : "7564971247732",
              "streetName" : "du Château",
              "streetNumber" : "10",
              "streetType" : "Chem.",
              "zipCode" : "1033"
            }
            """;

    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void convertToYaml_nominal() {
        assertText(YamlMarshaller.getInstance().convertToYaml(UnitTestData.USER_1),
                   YAML_NOMINAL);
    }


    @Test
    void convertFromYaml_nominal() {
        assertThat(YamlMarshaller.getInstance().convertFromYaml(null, UserDataDTO.class)).isNull();

        final UserDataDTO user = YamlMarshaller.getInstance().convertFromYaml(YAML_NOMINAL, UserDataDTO.class);
        assertText(user,
                   NOMINAL);


        assertText(YamlMarshaller.getInstance().convertFromYaml(YAML_NOMINAL, TYPE),
                   NOMINAL);


        assertText(YamlMarshaller.getInstance().convertFromYaml(YAML_NOMINAL),
                   NOMINAL);
    }
    // =================================================================================================================
    // TOOLS
    // =================================================================================================================

}