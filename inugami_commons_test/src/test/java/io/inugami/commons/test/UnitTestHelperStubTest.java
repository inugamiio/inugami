package io.inugami.commons.test;

import io.inugami.commons.test.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnitTestHelperStubTest {

    private static final String                    FOLDER = "test/unitTestHelperStubTest";
    @Mock
    private              UnitTestHelperStubTestDAO dao;

    @Mock
    private InvocationOnMock invocationOnMock;

    // ========================================================================
    // loadRelativeStub
    // ========================================================================
    @Test
    public void loadRelativeStub_shouldLoadMocks() {
        when(dao.getUserById(anyLong())).thenAnswer(answer -> UnitTestHelperStub.loadRelativeStub(answer,
                                                                                                  FOLDER,
                                                                                                  UserDto::getId,
                                                                                                  UserDto.class
        ));

        assertThat(dao.getUserById(1L)).isNotNull();
        assertThat(dao.getUserById(1L).getId()).isEqualTo(1L);
    }


    @Test
    public void loadRelativeStub_withCache_shouldLoadMocks() {
        final List<UserDto> cache = new ArrayList<>();

        when(dao.getUserById(anyLong())).thenAnswer(answer -> UnitTestHelperStub.loadRelativeStub(answer,
                                                                                                  FOLDER,
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

        when(dao.search(anyString())).thenAnswer(answer -> UnitTestHelperStub.loadRelativeStub(answer,
                                                                                               FOLDER,
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


    // ========================================================================
    // error
    // ========================================================================
    @Test
    public void loadIntegrationTestStub_withoutFolder_shouldThrow() {
        final List<UserDto> cache = new ArrayList<>();

        UnitTestHelper.assertThrows("stub folder is required", () -> {
            UnitTestHelperStub.loadRelativeStub(invocationOnMock,
                                                null,
                                                UserDto::getId,
                                                UserDto.class);
        });

        UnitTestHelper.assertThrows("stub folder is required", () -> {
            UnitTestHelperStub.loadRelativeStub(invocationOnMock,
                                                null,
                                                UserDto::getId,
                                                UserDto.class,
                                                cache);
        });

        UnitTestHelper.assertThrows("stub folder is required", () -> {
            UnitTestHelperStub.loadIntegrationTestStub(invocationOnMock,
                                                       null,
                                                       UserDto::getId,
                                                       UserDto.class);
        });

        UnitTestHelper.assertThrows("stub folder is required", () -> {
            UnitTestHelperStub.loadIntegrationTestStub(invocationOnMock,
                                                       null,
                                                       UserDto::getId,
                                                       UserDto.class,
                                                       cache);
        });
    }


    @Test
    public void loadIntegrationTestStub_withoutIdExtractor_shouldThrow() {
        final List<UserDto> cache        = new ArrayList<>();
        final String        errorMessage = "id extractor is required";

        UnitTestHelper.assertThrows(errorMessage, () -> {
            UnitTestHelperStub.loadRelativeStub(invocationOnMock,
                                                FOLDER,
                                                null,
                                                UserDto.class);
        });

        UnitTestHelper.assertThrows(errorMessage, () -> {
            UnitTestHelperStub.loadRelativeStub(invocationOnMock,
                                                FOLDER,
                                                null,
                                                UserDto.class,
                                                cache);
        });

        UnitTestHelper.assertThrows(errorMessage, () -> {
            UnitTestHelperStub.loadIntegrationTestStub(invocationOnMock,
                                                       FOLDER,
                                                       null,
                                                       UserDto.class);
        });

        UnitTestHelper.assertThrows(errorMessage, () -> {
            UnitTestHelperStub.loadIntegrationTestStub(invocationOnMock,
                                                       FOLDER,
                                                       null,
                                                       UserDto.class,
                                                       cache);
        });
    }


    @Test
    public void loadIntegrationTestStub_withoutClass_shouldThrow() {
        final List<UserDto> cache        = new ArrayList<>();
        final String        errorMessage = "object class is required";

        UnitTestHelper.assertThrows(errorMessage, () -> {
            UnitTestHelperStub.loadRelativeStub(invocationOnMock,
                                                FOLDER,
                                                UserDto::getId,
                                                null);
        });

        UnitTestHelper.assertThrows(errorMessage, () -> {
            UnitTestHelperStub.loadRelativeStub(invocationOnMock,
                                                FOLDER,
                                                UserDto::getId,
                                                null,
                                                cache);
        });

        UnitTestHelper.assertThrows(errorMessage, () -> {
            UnitTestHelperStub.loadIntegrationTestStub(invocationOnMock,
                                                       FOLDER,
                                                       UserDto::getId,
                                                       null);
        });

        UnitTestHelper.assertThrows(errorMessage, () -> {
            UnitTestHelperStub.loadIntegrationTestStub(invocationOnMock,
                                                       FOLDER,
                                                       UserDto::getId,
                                                       null,
                                                       cache);
        });
    }
}