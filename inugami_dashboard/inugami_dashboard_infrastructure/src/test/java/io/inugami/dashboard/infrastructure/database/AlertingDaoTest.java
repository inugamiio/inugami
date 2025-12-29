package io.inugami.dashboard.infrastructure.database;

import io.inugami.commons.test.api.UuidLineMatcher;
import io.inugami.dashboard.api.domain.alerting.dto.AlertingFilters;
import io.inugami.dashboard.api.domain.alerting.dto.AlertingSearchRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.inugami.commons.test.UnitTestData.OTHER;
import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.dashboard.infrastructure.utils.DataUtils.buildAlertingModel;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AlertingDaoTest {
    public static final String NOMINAL_LIST = """
            [ {
              "condition" : ">=5",
              "description" : "lorem ipsum",
              "function" : "simpleFunction",
              "level" : "ERROR",
              "name" : "simple-alert",
              "provider" : "provider",
              "uid" : "9efcb2f2-e884-49cc-ba2e-46cc92940ca7"
            } ]
            """;
    public static final String NOMINAL      = """
            {
              "condition" : ">=5",
              "description" : "lorem ipsum",
              "function" : "simpleFunction",
              "level" : "ERROR",
              "name" : "simple-alert",
              "provider" : "provider",
              "uid" : "06aef48d-ab89-4f8e-b132-f5461a12fcfd"
            }
            """;
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Test
    void crud_nominal() {
        final var dao = dao();
        assertThat(dao.create(null)).isEmpty();

        //--- CREATE ----------------------------------------------------------
        final var alert = dao.create(List.of(buildAlertingModel())).stream().findFirst().orElse(null);
        assertText(alert, NOMINAL, UuidLineMatcher.of(7));

        //--- READ ------------------------------------------------------------
        assertText(dao.search(AlertingSearchRequestDTO.builder().build(), AlertingFilters.FILTERS),
                   """
                           {
                             "data" : [ {
                               "condition" : ">=5",
                               "description" : "lorem ipsum",
                               "function" : "simpleFunction",
                               "level" : "ERROR",
                               "name" : "simple-alert",
                               "provider" : "provider",
                               "uid" : "c3ccb74b-a602-4bcc-9890-42cd2c6baf2a"
                             } ],
                             "nbFoundItems" : 1,
                             "next" : false,
                             "page" : 0,
                             "pageSize" : 0,
                             "previous" : false,
                             "totalPages" : 1
                           }
                           """,
                   UuidLineMatcher.of(8));

        assertThat(dao.getById(null, false)).isNull();
        assertText(dao.getById(alert.getUid(), false),
                   NOMINAL,
                   UuidLineMatcher.of(7));

        assertText(dao.getByIds(List.of(alert.getUid())), NOMINAL_LIST, UuidLineMatcher.of(7));
        assertThat(dao.getByIds(null)).isEmpty();


        assertThat(dao.contains(List.of(alert.getUid()))).isTrue();
        assertThat(dao.contains(null)).isFalse();
        assertThat(dao.contains(List.of())).isFalse();
        assertThat(dao.contains(List.of(OTHER))).isFalse();


        //--- UPDATE ----------------------------------------------------------
        assertText(dao.update(List.of(alert.toBuilder()
                                           .description("simple description")
                                           .build())),
                   """
                           [ {
                             "condition" : ">=5",
                             "description" : "simple description",
                             "function" : "simpleFunction",
                             "level" : "ERROR",
                             "name" : "simple-alert",
                             "provider" : "provider",
                             "uid" : "fe5e3f36-f9f2-4d56-b5da-542bab69e3b7"
                           } ]
                           """, UuidLineMatcher.of(7));

        assertText(dao.getById(alert.getUid(), false),
                   """
                           {
                             "condition" : ">=5",
                             "description" : "simple description",
                             "function" : "simpleFunction",
                             "level" : "ERROR",
                             "name" : "simple-alert",
                             "provider" : "provider",
                             "uid" : "6c996f17-e255-47e2-b488-7e4f25dea88c"
                           }
                           """, UuidLineMatcher.of(7));

        //--- DELETE ----------------------------------------------------------
        dao.delete(List.of(alert.getUid()));
        assertThat(dao.contains(List.of(alert.getUid()))).isFalse();
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    AlertingDao dao() {
        return AlertingDao.builder().build();
    }

}