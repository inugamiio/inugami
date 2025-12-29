package ${package}.infrastructure.database.listener;

import ${package}.infrastructure.database.entity.UserEntity;
import io.inugami.commons.test.api.LocalDateTimeLineMatcher;
import io.inugami.framework.api.monitoring.MdcService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;

class AuditingEntityListenerTest {

    @AfterEach
    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
    }

    @Test
    void setUpdatedOn_nominal() {
        MdcService.getInstance().principal("user-1");
        final var listener = new AuditingEntityListener();
        final var entity   = UserEntity.builder().build();
        listener.setUpdatedOn(entity);

        assertText(entity,
                   """
                           {
                             "audit" : {
                               "lastModifiedBy" : "user-1",
                               "lastModifiedDate" : "2025-12-28T22:14:55.812417489"
                             }
                           }
                           """,
                   LocalDateTimeLineMatcher.of(3));
    }
}