package ${package}.infrastructure.database.mapper;

import ${package}.infrastructure.database.entity.AuditEntity;
import ${package}.infrastructure.database.entity.AuditProjection;
import ${package}.infrastructure.database.entity.AuditableEntity;
import ${package}.infrastructure.database.entity.UserEntity;
import io.inugami.commons.test.UnitTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditEntityMapperUtilsTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String SYSTEM  = "system";
    public static final String USER_1  = "user-1";
    public static final String NOMINAL = """
            {
              "createdBy" : "system",
              "createdDate" : "2023-05-30T12:00:00",
              "lastModifiedBy" : "user-1",
              "lastModifiedDate" : "2023-06-01T12:00:00",
              "version" : 2
            }
            """;

    @Mock
    private AuditProjection auditProjection;

    //==================================================================================================================
    // TEST
    //==================================================================================================================
    @Test
    void convertToAuditDTO_withoutValue() {
        final AuditProjection projection = null;
        final AuditableEntity entity     = null;

        assertThat(AuditEntityMapperUtils.convertToAuditDTO(projection)).isNull();
        assertThat(AuditEntityMapperUtils.convertToAuditDTO(entity)).isNull();
    }

    @Test
    void convertToAuditDTO_entity_nominal() {
        assertText(AuditEntityMapperUtils.convertToAuditDTO(UserEntity.builder()
                                                                      .version(2L)
                                                                      .audit(AuditEntity.builder()
                                                                                        .createdBy(SYSTEM)
                                                                                        .createdDate(UnitTestData.DATE_TIME.minusDays(2))
                                                                                        .lastModifiedBy(USER_1)
                                                                                        .lastModifiedDate(UnitTestData.DATE_TIME)
                                                                                        .build())
                                                                      .build()),
                   NOMINAL);
    }

    @Test
    void convertToAuditDTO_projection_nominal() {
        when(auditProjection.getVersion()).thenReturn(2L);
        when(auditProjection.getCreatedBy()).thenReturn(SYSTEM);
        when(auditProjection.getCreatedDate()).thenReturn(UnitTestData.DATE_TIME.minusDays(2));
        when(auditProjection.getLastModifiedBy()).thenReturn(USER_1);
        when(auditProjection.getLastModifiedDate()).thenReturn(UnitTestData.DATE_TIME);
        assertText(AuditEntityMapperUtils.convertToAuditDTO(auditProjection),
                   NOMINAL);
    }
}