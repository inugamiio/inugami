package io.inugami.commons.test;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.UncheckedException;
import io.inugami.commons.test.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
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
import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UnitTestHelperTest {

    @Mock
    private UnitTestHelperStubTestDAO dao;

    // =========================================================================
    // LOAD / READ FILE
    // =========================================================================
    @Test
    public void readFileRelative_nominal() {
        final String value = UnitTestHelper.readFileRelative("test/UnitTestHelperFile/readFileRelative.txt");
        assertThat(value).isEqualTo("hello");
    }

    @Test
    public void readFileRelative_withNullValue_shouldThrow() {
        UnitTestHelper.assertThrows(RuntimeException.class,
                                    "can't read file from null relative path!",
                                    () -> UnitTestHelper.readFileRelative(null));
    }


    @Test
    public void readFile_nominal() {
        final File file = UnitTestHelper.buildTestFilePath("test", "UnitTestHelperFile", "readFileRelative.txt");
        assertThat(UnitTestHelper.readFile(file)).isEqualTo("hello");
    }

    @Test
    public void readFile_withNullValue_shouldReturnNull() {
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
    public void buildTestFilePath_nominal() {
        assertThat(buildRelativePath(UnitTestHelper.buildTestFilePath("data", "mock")))
                .isEqualTo("./src/test/resources/data/mock");
    }

    @Test
    public void buildIntegrationTestFilePath_nominal() {
        assertThat(buildRelativePath(UnitTestHelper.buildIntegrationTestFilePath("data", "mock")))
                .isEqualTo("./src/test_it/resources/data/mock");
    }

    @Test
    public void buildPath_nominal() {
        assertThat(buildRelativePath(UnitTestHelper.buildPath("data", "mock")))
                .isEqualTo("./data/mock");
    }

    // =========================================================================
    // Stub
    // =========================================================================

    @Test
    public void loadRelativeStub_shouldLoadMocks() {
        when(dao.getUserById(anyLong())).thenAnswer(answer -> UnitTestHelper.loadRelativeStub(answer,
                                                                                              "test/unitTestHelperStubTest",
                                                                                              UserDto::getId,
                                                                                              UserDto.class
        ));

        assertThat(dao.getUserById(1L)).isNotNull();
        assertThat(dao.getUserById(1L).getId()).isEqualTo(1L);
    }

    @Test
    public void loadRelativeStub_withCache_shouldLoadMocks() {
        final List<UserDto> cache = new ArrayList<>();

        when(dao.getUserById(anyLong())).thenAnswer(answer -> UnitTestHelper.loadRelativeStub(answer,
                                                                                              "test/unitTestHelperStubTest",
                                                                                              UserDto::getId,
                                                                                              UserDto.class,
                                                                                              cache
        ));


        assertThat(dao.getUserById(1L)).isNotNull();
        assertThat(cache.size()).isEqualTo(2);
        assertThat(dao.getUserById(1L).getId()).isEqualTo(1L);
        assertTextRelative(cache, "test/unitTestHelperStubTest/cache.ref.json");
    }


    @Test
    public void loadRelativeStub_withIdResolver_shouldLoadMocks() {
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
    public void loadJson_withTypeReference_shouldLoad() {
        final UserDto user = UnitTestHelper.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        UnitTestHelper.assertTextRelative(user, "test/dto/user_1_ref.json");
    }

    @Test
    public void loadJson_withClass_shouldLoad() {
        final UserDto user = UnitTestHelper.loadJson("test/dto/user.1.json", UserDto.class);
        UnitTestHelper.assertTextRelative(user, "test/dto/user_1_ref.json");
    }


    @Test
    public void convertFromJson_nominal() {
        final UserDto ref  = UnitTestHelper.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        final String  json = UnitTestHelper.convertToJson(ref);

        UnitTestHelper.assertTextRelative(UnitTestHelper.convertFromJson(json.getBytes(StandardCharsets.UTF_8), UserDto.class), "test/dto/user_1_ref.json");
        UnitTestHelper.assertTextRelative(UnitTestHelper.convertFromJson(json.getBytes(StandardCharsets.UTF_8), USER_DTO_TYPE), "test/dto/user_1_ref.json");

        UnitTestHelper.assertTextRelative(UnitTestHelper.convertFromJson(json, UserDto.class), "test/dto/user_1_ref.json");
        UnitTestHelper.assertTextRelative(UnitTestHelper.convertFromJson(json, USER_DTO_TYPE), "test/dto/user_1_ref.json");
    }

    @Test
    public void forceConvertToJson_nominal() {
        final UserDto ref = UnitTestHelperJson.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        UnitTestHelper.assertTextRelative(UnitTestHelper.forceConvertToJson(ref), "test/dto/user_1_refForceConvertToJson.valid.json");
    }

    @Test
    public void convertToJsonWithoutIndent_nominal() {
        final UserDto ref = UnitTestHelper.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        assertThat(UnitTestHelper.convertToJsonWithoutIndent(ref)).isEqualTo("{\"creationDate\":\"2023-04-07T14:04:00\",\"firstName\":\"John\",\"lastName\":\"Smith\"}");
    }

    // =========================================================================
    // TEXT
    // =========================================================================

    @Test
    public void assertText_withLocalDateTime_shouldSerializeAsString() {
        final LocalDateTime date = LocalDateTime.of(2023, 4, 2, 20, 27, 0);
        assertTextRelative(date, "assertText_withLocalDateTime_shouldSerializeAsString.json");
    }

    @Test
    public void AssertTextException_withDiff_shouldThrow() {
        final UserDto ref = UnitTestHelperJson.loadJson("test/dto/user.1.json", USER_DTO_TYPE);
        ref.setLastName("foobar");
        UnitTestHelper.assertThrows(() -> UnitTestHelper.assertTextRelative(UnitTestHelperJson.forceConvertToJson(ref), "test/dto/user_1_refForceConvertToJson.json"));


        UnitTestHelper.assertThrows(UnitTestHelperText.AssertTextException.class, "reference and json have not same size : 17,16",
                                    () -> UnitTestHelper.assertTextRelative(UnitTestHelper.forceConvertToJson(ref), "test/dto/user_1_refForceConvertToJson.json"));
    }

    // =========================================================================
    // EXCEPTIONS
    // =========================================================================
    @Test
    public void assertThrowsError_withException_shouldMatch() {

        UnitTestHelper.assertThrows(NullPointerException.class, "some error", () -> {
            throw new NullPointerException("some error");
        });
    }


    @Test
    public void throwException_default_method() {
        //------------------------------------------
        UnitTestHelper.assertThrows(() -> {
            throw new UncheckedException();
        });
        //------------------------------------------

        try {
            UnitTestHelper.assertThrows(() -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

    }


    @Test
    public void throwException_withErrorMessage() {
        //------------------------------------------

        final String msg = null;
        UnitTestHelper.assertThrows(msg, () -> {
            throw new UncheckedException();
        });

        UnitTestHelper.assertThrows("this method should throws", () -> {
            throw new UncheckedException("this method should throws");
        });

        //------------------------------------------

        try {
            UnitTestHelper.assertThrows("this method should throws", () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            UnitTestHelper.assertThrows("this method should throws", () -> {
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
    public void throwException_withClass() {
        //------------------------------------------

        final String msg = null;
        UnitTestHelper.assertThrows(UncheckedException.class, () -> {
            throw new UncheckedException();
        });
        //------------------------------------------

        try {
            UnitTestHelper.assertThrows(UncheckedException.class, () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            UnitTestHelper.assertThrows(UncheckedException.class, () -> {
                throw new NullPointerException("some error");
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("error class isn't a io.inugami.api.exceptions.UncheckedException");
        }
    }


    @Test
    public void throwException_withClassAndMessage() {
        //------------------------------------------

        final String msg = null;
        UnitTestHelper.assertThrows(UncheckedException.class, "some error", () -> {
            throw new UncheckedException("some error");
        });
        //------------------------------------------

        try {
            UnitTestHelper.assertThrows(UncheckedException.class, "some error", () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            UnitTestHelper.assertThrows(UncheckedException.class, "some error", () -> {
                throw new NullPointerException("some error");
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("error class isn't a io.inugami.api.exceptions.UncheckedException");
        }

        try {
            UnitTestHelper.assertThrows(UncheckedException.class, "some error", () -> {
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
    public void throwException_withErrorCode() {
        //------------------------------------------
        final ErrorCode undefinedError = DefaultErrorCode.buildUndefineError();
        final String    msg            = null;
        UnitTestHelper.assertThrows(undefinedError, () -> {
            throw new UncheckedException(undefinedError);
        });
        //------------------------------------------

        try {
            UnitTestHelper.assertThrows(undefinedError, () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            UnitTestHelper.assertThrows(undefinedError, () -> {
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
    public void throwException_nominal() {
        UnitTestHelper.assertThrows(NullPointerException.class, "sorry", () -> {
            UnitTestHelper.throwException(new NullPointerException("sorry"));
        });
    }

}