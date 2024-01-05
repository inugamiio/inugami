package io.inugami.commons.test;

import io.inugami.commons.test.api.RegexLineMatcher;
import io.inugami.commons.test.api.RegexLineReplacer;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.dto.UserDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.inugami.commons.test.TestUtils.USER_DTO_TYPE;

class UnitTestHelperTextTest {


    @Test
    void forceConvertToJson_nominal() {
        final UserDto ref = UnitTestHelperJson.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        ref.setLastName("foobar");
        UnitTestHelper.assertThrows(() -> UnitTestHelperText.assertTextRelative(UnitTestHelperJson.forceConvertToJson(ref), "test/dto/user_1_refForceConvertToJson.json"));


        UnitTestHelper.assertThrows(UnitTestHelperText.AssertTextException.class, "reference and json have not same size : 17,16",
                                    () -> UnitTestHelperText.assertTextRelative(UnitTestHelperJson.forceConvertToJson(ref), "test/dto/user_1_refForceConvertToJson.json"));
    }


    @Test
    void assertTextRelative_withoutSameSize_shouldThrow() {
        final UserDto ref = UnitTestHelperJson.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        UnitTestHelper.assertThrows("reference and json have not same size : 5,17",
                                    () -> UnitTestHelperText.assertTextRelative(ref, "test/dto/user_1_invalid_size.json"));
    }

    @Test
    void assertTextRelative_withNullValue_shouldThrow() {
        final UserDto ref = UnitTestHelperJson.loadJson("test/dto/user.1.json", USER_DTO_TYPE);

        UnitTestHelper.assertThrows("json mustn't be null",
                                    () -> UnitTestHelperText.assertTextRelative(null, "test/dto/user_1_invalid_size.json"));

        UnitTestHelper.assertThrows("can't read file from null relative path!",
                                    () -> UnitTestHelperText.assertTextRelative(ref, null));
    }


    // =========================================================================
    // SKIP
    // =========================================================================
    @Test
    void assertTextRelative_withSkipLine_shouldSkip() {

        final UserDto user = UserDto.builder()
                                    .lastName("Smith")
                                    .firstName("John")
                                    .creationDate(LocalDateTime.now())
                                    .build();

        String data = "{\n" +
                      "xxx\n" +
                      "  \"firstName\" : \"John\",\n" +
                      "  \"lastName\" : \"Smith\"\n" +
                      "}";
        UnitTestHelperText.assertText(user,
                                      data,
                                      RegexLineReplacer.of(".*creationDate.*", "xxx"));

        UnitTestHelperText.assertTextRelative(user, "test/dto/user.1.valid.json", SkipLineMatcher.of(1));
        UnitTestHelperText.assertTextRelative(user, "test/dto/user.1.valid.json", UnitTestHelperText.buildSkipLines(1));


    }

    @Test
    void assertTextRelative_withSPattern_shouldSkip() {
        final UserDto user = UserDto.builder()
                                    .lastName("Smith")
                                    .firstName("John")
                                    .creationDate(LocalDateTime.now())
                                    .build();

        UnitTestHelperText.assertTextRelative(user, "test/dto/user.1.valid.json",
                                              RegexLineMatcher.of(".*[:].*[0-9]{4}-[0-9]{2}-[0-9]{2}.*", 1));

        UnitTestHelperText.assertTextRelative(user, "test/dto/user.1.valid.json",
                                              RegexLineMatcher.of(".*creationDate.*", ".*[:].*[0-9]{4}-[0-9]{2}-[0-9]{2}.*"));

    }
}