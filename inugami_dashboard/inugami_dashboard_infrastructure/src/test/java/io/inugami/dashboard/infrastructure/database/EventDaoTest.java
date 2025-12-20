package io.inugami.dashboard.infrastructure.database;

import io.inugami.commons.test.UnitTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EventDaoTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @InjectMocks
    private EventDao eventDao;

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Test
    void getEvent_nominal() {
        assertThat(eventDao.getEvent(UnitTestData.UID)).isNull();
    }
}