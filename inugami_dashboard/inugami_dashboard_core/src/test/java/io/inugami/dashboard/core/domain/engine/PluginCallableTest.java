package io.inugami.dashboard.core.domain.engine;

import io.inugami.dashboard.core.domain.tools.DataUtils;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.dashboard.core.domain.tools.DataUtils.buildPlugin;

class PluginCallableTest {

    @Test
    void call_nominal() throws Exception {
        final PluginCallable task = PluginCallable.builder()
                                                  .plugin(buildPlugin())
                                                  .callable(() -> DataUtils.buildEnginePluginResultDTO())
                                                  .build();

        assertText(task.call(),
                   """
                           {
                             "events" : [ {
                               "data" : {
                                 "alerts" : [ ],
                                 "data" : [ 15, 52 ]
                               },
                               "message" : "success",
                               "name" : "event",
                               "status" : "SUCCESS"
                             } ],
                             "gav" : {
                               "artifactId" : "inu-test",
                               "groupId" : "io.inugami.plugin",
                               "hash" : "io.inugami.plugin:inu-test:4.3.0:jar",
                               "qualifier" : "jar",
                               "version" : "4.3.0"
                             },
                             "message" : "success",
                             "status" : "SUCCESS"
                           }
                           """);
    }
}