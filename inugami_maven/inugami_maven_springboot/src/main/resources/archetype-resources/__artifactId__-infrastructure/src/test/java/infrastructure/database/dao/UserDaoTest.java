package ${package}.infrastructure.database.dao;

import ${package}.api.domain.user.dto.UserDTO;
import ${package}.infrastructure.database.mapper.DatasourceEntityMapperConfiguration;
import ${package}.infrastructure.database.repository.UserEntityRepository;
import io.inugami.commons.test.UnitTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Mock
    private UserEntityRepository userEntityRepository;


    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Test
    void contains_nominal() {
        when(userEntityRepository.findUids(List.of(UnitTestData.UID))).thenReturn(List.of( UnitTestData.UID));
        assertThat(dao().contains(List.of(UnitTestData.UID))).isTrue();
    }
    @Test
    void contains_withoutValue() {
        final var dao = dao();
        when(userEntityRepository.findUids(List.of("cf6514de-8a7e-47cd-b59a-3da8c334fe17", UnitTestData.UID))).thenReturn(List.of( UnitTestData.UID));

        assertThat(dao.contains(null)).isFalse();
        assertThat(dao.contains(List.of())).isFalse();
        assertThat(dao.contains(List.of("cf6514de-8a7e-47cd-b59a-3da8c334fe17", UnitTestData.UID))).isFalse();
    }

    //==================================================================================================================
    // UPDATE
    //==================================================================================================================
    @Test
    void update_withoutData(){
        final var dao = dao();
        assertThat(dao.update(null)).isEmpty();
        assertThat(dao.update(List.of())).isEmpty();

        verify(userEntityRepository, never()).update(any(),any(),any(),any(),any(),any());
    }

    @Test
    void update_nominal(){
        final var dao = dao();

        assertThat(dao.update(List.of(UserDTO.builder()
                                             .uid(UnitTestData.UID)
                                             .firstName(UnitTestData.FIRSTNAME)
                                             .lastName(UnitTestData.LASTNAME)
                                             .email(UnitTestData.EMAIL)
                                             .build()))).isNotEmpty();

        verify(userEntityRepository).update(any(),any(),any(),any(),any(),any());
    }


    //==================================================================================================================
    // DELETE
    //==================================================================================================================
    @Test
    void delete_nominal(){
        dao().delete(List.of(UnitTestData.UID));
        verify(userEntityRepository).deleteAllById(List.of(UnitTestData.UID));
    }
    @Test
    void delete_withoutData(){
        dao().delete(List.of());
        dao().delete(null);

        verify(userEntityRepository, never()).deleteAllById(any());
    }
    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    UserDao dao() {
        return UserDao.builder()
                      .userEntityRepository(userEntityRepository)
                      .userEntityMapper(new DatasourceEntityMapperConfiguration().userEntityMapper())
                      .build();
    }
}