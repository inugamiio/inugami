package io.inugami.commons.test;

import io.inugami.commons.test.dto.UserDto;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static io.inugami.commons.test.TestUtils.USER_DTO_TYPE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UnitTestHelperJsonTest {


    // =========================================================================
    // LOAD
    // =========================================================================
    @Test
    void loadJson_withTypeReference_shouldLoad() {
        final UserDto user = UnitTestHelperJson.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        UnitTestHelper.assertTextRelative(user, "test/dto/user_1_ref.json");
    }

    @Test
    void loadJson_withClass_shouldLoad() {
        final UserDto user = UnitTestHelperJson.loadJson("test/dto/user.1.json", UserDto.class);
        UnitTestHelper.assertTextRelative(user, "test/dto/user_1_ref.json");
    }


    @Test
    void convertFromJson_nominal() {
        final UserDto ref  = UnitTestHelperJson.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        final String  json = UnitTestHelperJson.convertToJson(ref);

        UnitTestHelper.assertTextRelative(UnitTestHelperJson.convertFromJson(json.getBytes(StandardCharsets.UTF_8), UserDto.class), "test/dto/user_1_ref.json");
        UnitTestHelper.assertTextRelative(UnitTestHelperJson.convertFromJson(json.getBytes(StandardCharsets.UTF_8), USER_DTO_TYPE), "test/dto/user_1_ref.json");

        UnitTestHelper.assertTextRelative(UnitTestHelperJson.convertFromJson(json, UserDto.class), "test/dto/user_1_ref.json");
        UnitTestHelper.assertTextRelative(UnitTestHelperJson.convertFromJson(json, USER_DTO_TYPE), "test/dto/user_1_ref.json");
    }


    @Test
    void convertToJsonWithoutIndent_nominal() {
        final UserDto ref = UnitTestHelperJson.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        assertThat(UnitTestHelperJson.convertToJsonWithoutIndent(ref)).isEqualTo("{\"creationDate\":\"2023-04-07T14:04:00\",\"firstName\":\"John\",\"lastName\":\"Smith\"}");
    }
}