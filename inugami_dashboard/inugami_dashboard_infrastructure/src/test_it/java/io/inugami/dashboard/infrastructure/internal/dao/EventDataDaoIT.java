package io.inugami.dashboard.infrastructure.internal.dao;

import io.inugami.dashboard.infrastructure.spring.SpringBootIntegrationTest;
import io.inugami.framework.interfaces.models.engine.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.dashboard.infrastructure.utils.DataUtils.*;

class EventDataDaoIT extends SpringBootIntegrationTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Autowired
    private EventDataDao eventDataDao;

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @Test
    void updateEventsData_nominal() {
        eventDataDao.updateEventsData(List.of());
        eventDataDao.updateEventsData(List.of(buildEventDoneDTO().toBuilder()
                                                                 .data(buildEnginePluginEventResultDTO().toBuilder()
                                                                                                        .status(Status.ERROR)
                                                                                                        .build())
                                                                 .build(),
                                              buildEventDoneDTO().toBuilder()
                                                                 .data(buildEnginePluginEventResultDTO().toBuilder()
                                                                                                        .status(null)
                                                                                                        .build())
                                                                 .build(),
                                              buildEventDoneDTO()));
        assertText(eventDataDao.findPluginDataByGav(buildGav()),
                   """
                           {
                             "event-name" : {
                               "data" : {
                                 "alerts" : [ ],
                                 "data" : [ 15, 52 ]
                               },
                               "message" : "success",
                               "name" : "event",
                               "status" : "SUCCESS"
                             }
                           }
                           """);
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================


}