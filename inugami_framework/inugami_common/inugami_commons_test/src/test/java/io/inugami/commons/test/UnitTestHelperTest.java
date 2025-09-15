package io.inugami.commons.test;

import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.dto.UserDto;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.inugami.commons.test.TestUtils.USER_DTO_TYPE;
import static io.inugami.commons.test.TestUtils.buildRelativePath;
import static io.inugami.commons.test.UnitTestHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UnitTestHelperTest {

    @Mock
    private UnitTestHelperStubTestDAO dao;

    // =========================================================================
    // LOAD / READ FILE
    // =========================================================================
    @Test
    void readFileRelative_nominal() {
        final String value = UnitTestHelper.readFileRelative("test/UnitTestHelperFile/readFileRelative.txt");
        assertThat(value).isEqualTo("hello");
    }

    @Test
    void readFileRelative_withNullValue_shouldThrow() {
        assertThrows(RuntimeException.class,
                     "can't read file from null relative path!",
                     () -> UnitTestHelper.readFileRelative(null));
    }


    @Test
    void readFile_nominal() {
        final File file = UnitTestHelper.buildTestFilePath("test", "UnitTestHelperFile", "readFileRelative.txt");
        assertThat(UnitTestHelper.readFile(file)).isEqualTo("hello");
    }

    @Test
    void readFile_withNullValue_shouldReturnNull() {
        final File file = null;
        assertThat(UnitTestHelper.readFile(file)).isNull();
        final String path = null;
        assertThat(UnitTestHelper.readFile(path)).isNull();

    }

    // =========================================================================
    // LOAD Object Stub by uid
    // =========================================================================

    // =========================================================================
    // BUILD PATH
    // =========================================================================
    @Test
    void buildTestFilePath_nominal() {
        assertThat(buildRelativePath(UnitTestHelper.buildTestFilePath("data", "mock")))
                .isEqualTo("./src/test/resources/data/mock");
    }

    @Test
    void buildIntegrationTestFilePath_nominal() {
        assertThat(buildRelativePath(UnitTestHelper.buildIntegrationTestFilePath("data", "mock")))
                .isEqualTo("./src/test_it/resources/data/mock");
    }

    @Test
    void buildPath_nominal() {
        assertThat(buildRelativePath(UnitTestHelper.buildPath("data", "mock")))
                .isEqualTo("./data/mock");
    }

    // =========================================================================
    // Stub
    // =========================================================================

    @Test
    void loadRelativeStub_shouldLoadMocks() {
        when(dao.getUserById(anyLong())).thenAnswer(answer -> UnitTestHelper.loadRelativeStub(answer,
                                                                                              "test/unitTestHelperStubTest",
                                                                                              UserDto::getId,
                                                                                              UserDto.class
        ));

        assertThat(dao.getUserById(1L)).isNotNull();
        assertThat(dao.getUserById(1L).getId()).isEqualTo(1L);
    }

    @Test
    void loadRelativeStub_withCache_shouldLoadMocks() {
        final List<UserDto> cache = new ArrayList<>();

        when(dao.getUserById(anyLong())).thenAnswer(answer -> UnitTestHelper.loadRelativeStub(answer,
                                                                                              "test/unitTestHelperStubTest",
                                                                                              UserDto::getId,
                                                                                              UserDto.class,
                                                                                              cache
        ));


        assertThat(dao.getUserById(1L)).isNotNull();
    }


    @Test
    void loadRelativeStub_withIdResolver_shouldLoadMocks() {
        final List<UserDto> cache = new ArrayList<>();

        when(dao.search(anyString())).thenAnswer(answer -> UnitTestHelper.loadRelativeStub(answer,
                                                                                           "test/unitTestHelperStubTest",
                                                                                           UserDto::getId,
                                                                                           args -> {
                                                                                               if ("John".equalsIgnoreCase(String.valueOf(args[0]))) {
                                                                                                   return 1;
                                                                                               } else {
                                                                                                   return 2;
                                                                                               }
                                                                                           },
                                                                                           UserDto.class,
                                                                                           cache
        ));


        assertThat(dao.search("John").getFirstName()).isEqualTo("John");
        assertThat(dao.search("Albertine").getFirstName()).isEqualTo("Albertine");
    }

    // =========================================================================
    // JSON
    // =========================================================================
    @Test
    void loadJson_withTypeReference_shouldLoad() {
        final UserDto user = UnitTestHelper.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        UnitTestHelper.assertTextRelative(user, "test/dto/user_1_ref.json");
    }

    @Test
    void loadJson_withClass_shouldLoad() {
        final UserDto user = UnitTestHelper.loadJson("test/dto/user.1.json", UserDto.class);
        UnitTestHelper.assertTextRelative(user, "test/dto/user_1_ref.json");
    }


    @Test
    void convertFromJson_nominal() {
        final UserDto ref  = UnitTestHelper.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        final String  json = UnitTestHelper.convertToJson(ref);

        UnitTestHelper.assertTextRelative(UnitTestHelper.convertFromJson(json.getBytes(StandardCharsets.UTF_8), UserDto.class), "test/dto/user_1_ref.json");
        UnitTestHelper.assertTextRelative(UnitTestHelper.convertFromJson(json.getBytes(StandardCharsets.UTF_8), USER_DTO_TYPE), "test/dto/user_1_ref.json");

        UnitTestHelper.assertTextRelative(UnitTestHelper.convertFromJson(json, UserDto.class), "test/dto/user_1_ref.json");
        UnitTestHelper.assertTextRelative(UnitTestHelper.convertFromJson(json, USER_DTO_TYPE), "test/dto/user_1_ref.json");
    }


    @Test
    void convertToJsonWithoutIndent_nominal() {
        final UserDto ref = UnitTestHelper.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        assertThat(UnitTestHelper.convertToJsonWithoutIndent(ref)).isEqualTo("{\"creationDate\":\"2023-04-07T14:04:00\",\"firstName\":\"John\",\"lastName\":\"Smith\"}");
    }

    // =========================================================================
    // TEXT
    // =========================================================================

    @Test
    void assertText_withLocalDateTime_shouldSerializeAsString() {
        final LocalDateTime date = LocalDateTime.of(2023, 4, 2, 20, 27, 0);
        assertTextRelative(date, "assertText_withLocalDateTime_shouldSerializeAsString.json");
    }

    @Test
    void AssertTextException_withDiff_shouldThrow() {
        final UserDto ref = UnitTestHelperJson.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        ref.setLastName("foobar");
        assertThrows(() -> UnitTestHelper.assertTextRelative(UnitTestHelperJson.convertToJson(ref), "test/dto/user_1_refForceConvertToJson.json"));
    }

    // =========================================================================
    // EXCEPTIONS
    // =========================================================================
    @Test
    void assertThrowsError_withException_shouldMatch() {

        assertThrows(NullPointerException.class, "some error", () -> {
            throw new NullPointerException("some error");
        });
    }


    @Test
    void throwException_default_method() {
        //------------------------------------------
        assertThrows(() -> {
            throw new UncheckedException();
        });
        //------------------------------------------

        try {
            assertThrows(() -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

    }


    @Test
    void throwException_withErrorMessage() {
        //------------------------------------------

        final String msg = null;
        assertThrows(msg, () -> {
            throw new UncheckedException();
        });

        assertThrows("this method should throws", () -> {
            throw new UncheckedException("this method should throws");
        });

        //------------------------------------------

        try {
            assertThrows("this method should throws", () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            assertThrows("this method should throws", () -> {
                throw new UncheckedException("some error");
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("current : \"some error\"  ref: \"this method should throws\"");
        }
    }


    @Test
    void throwException_withClass() {
        //------------------------------------------

        final String msg = null;
        assertThrows(UncheckedException.class, () -> {
            throw new UncheckedException();
        });
        //------------------------------------------

        try {
            assertThrows(UncheckedException.class, () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            assertThrows(UncheckedException.class, () -> {
                throw new NullPointerException("some error");
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("error class isn't a io.inugami.framework.interfaces.exceptions.UncheckedException");
        }
    }


    @Test
    void throwException_withClassAndMessage() {
        //------------------------------------------

        final String msg = null;
        assertThrows(UncheckedException.class, "some error", () -> {
            throw new UncheckedException("some error");
        });
        //------------------------------------------

        try {
            assertThrows(UncheckedException.class, "some error", () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            assertThrows(UncheckedException.class, "some error", () -> {
                throw new NullPointerException("some error");
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("error class isn't a io.inugami.framework.interfaces.exceptions.UncheckedException");
        }

        try {
            assertThrows(UncheckedException.class, "some error", () -> {
                throw new UncheckedException("sorry");
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("current : \"sorry\"  ref: \"some error\"");
        }
    }


    @Test
    void throwException_withErrorCode() {
        //------------------------------------------
        final ErrorCode undefinedError = DefaultErrorCode.buildUndefineError();
        final String    msg            = null;
        assertThrows(undefinedError, () -> {
            throw new UncheckedException(undefinedError);
        });
        //------------------------------------------

        try {
            assertThrows(undefinedError, () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            assertThrows(undefinedError, () -> {
                throw new UncheckedException(DefaultErrorCode.buildUndefineErrorCode().errorCode("ERR").build());
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("current : \"ERR\"  ref: \"err-undefine\"");
        }

    }

    @Test
    void throwException_nominal() {
        assertThrows(NullPointerException.class, "sorry", () -> {
            UnitTestHelper.throwException(new NullPointerException("sorry"));
        });
    }


    @Test
    void assertUtilityClassLombok_nominal() {
        UnitTestHelper.assertUtilityClassLombok(UnitTestHelperUtilityClassTest.SimpleUtilityClass.class);
    }

    @Test
    void assertUtilityClassLombok_withMethodNonStatic() {
        assertThrows("hello isn't static method!", () -> UnitTestHelper.assertUtilityClassLombok(UnitTestHelperUtilityClassTest.BadUtilityClass.class));
    }

    @Test
    void assertUtilityClassLombok_withoutConstructorPrivate() {
        assertThrows(UncheckedException.class, () -> UnitTestHelper.assertUtilityClassLombok(UnitTestHelperUtilityClassTest.WithoutConstructorUtilityClass.class));
    }


    // =========================================================================
    // DATA
    // =========================================================================

    @Test
    void getRandomUid_nominal() {
        Assertions.assertThat(UnitTestHelper.getRandomUid()).isNotEqualTo(UnitTestHelper.getRandomUid());
        for (int i = 10; i >= 0; i--) {
            log.info("{}", UnitTestHelper.getRandomUid());
        }
    }

    @Test
    void getRandomWord_nominal() {
        Assertions.assertThat(UnitTestHelper.getRandomWord()).isNotNull();
    }

    @Test
    void getRandomCategory_nominal() {
        Assertions.assertThat(UnitTestHelper.getRandomCategory()).isNotNull();
    }

    @Test
    void getRandomLabel_nominal() {
        Assertions.assertThat(UnitTestHelper.getRandomLabel()).isNotNull();
    }

    @Test
    void getRandomPhrase_nominal() {
        Assertions.assertThat(UnitTestHelper.getRandomPhrase()).isNotNull();
    }

    @Test
    void getRandomPhrase_withMinMax() {
        final String[] value = UnitTestHelper.getRandomPhrase(2, 5, true).split(" ");
        Assertions.assertThat(value).isNotNull();
        Assertions.assertThat(value.length).isGreaterThanOrEqualTo(2).isLessThanOrEqualTo(5);
    }

    @Test
    void getRandomSection_nominal() {
        Assertions.assertThat(UnitTestHelper.getRandomSection()).isNotNull();
    }

    @Test
    void getRandomSection_withMinMax() {
        Assertions.assertThat(UnitTestHelper.getRandomSection(2, 5, 5)).isNotNull();
    }

    @Test
    void getRandomDouble_nominal() {
        final double value = UnitTestHelper.getRandomDouble(2, 5);
        Assertions.assertThat(value).isGreaterThanOrEqualTo(2).isLessThanOrEqualTo(5);
    }

    @Test
    void getRandomBetween_nominal() {
        final double value = UnitTestHelper.getRandomBetween(2, 5);
        Assertions.assertThat(value).isGreaterThanOrEqualTo(2).isLessThanOrEqualTo(5);
    }


    // =========================================================================
    // ASSERTS ENUM
    // =========================================================================
    @Test
    void assertEnum_nominal() {
        assertEnum(Levels.class, "{\n" +
                                 "  \"ADMIN\" : {\n" +
                                 "    \"label\" : \"admin\",\n" +
                                 "    \"level\" : 10\n" +
                                 "  },\n" +
                                 "  \"GUEST\" : {\n" +
                                 "    \"label\" : \"guest\",\n" +
                                 "    \"level\" : 0\n" +
                                 "  }\n" +
                                 "}",
                   SkipLineMatcher.of(7));
    }

    @Test
    void assertEnumRelative_nominal() {
        assertEnumRelative(Levels.class, "test/UnitTestHelperTest/assertEnumRelative_nominal.json", SkipLineMatcher.of(7));
    }

    @RequiredArgsConstructor
    public enum Levels {
        ADMIN("admin", 10),
        GUEST("guest", 0);

        private final String label;
        private final int    level;
    }
}