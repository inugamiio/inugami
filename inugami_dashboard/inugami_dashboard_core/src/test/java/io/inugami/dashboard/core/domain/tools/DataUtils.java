/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.dashboard.core.domain.tools;

import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginResultDTO;
import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.interfaces.concurrent.FutureData;
import io.inugami.framework.interfaces.concurrent.FutureDataModel;
import io.inugami.framework.interfaces.concurrent.ImmediateFutureData;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.event.TargetConfig;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.models.number.DataPoint;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @since 2025-12-18
 */
@UtilityClass
public class DataUtils {
    public static final String PROCESSOR_NAME = "processor_name";
    public static final String PROVIDER       = "provider";


    public static SimpleEvent buildSimpleEvent() {
        return SimpleEvent.builder()
                          .name("event-name")
                          .fromFirstTime("-10min")
                          .from("-5min")
                          .processors(List.of(ProcessorModel.builder()
                                                            .name(PROCESSOR_NAME)
                                                            .build()))
                          .query("query")
                          .scheduler("0 0/5 * * * ?")
                          .mapper("mapper")
                          .provider(PROVIDER)
                          .build();
    }

    public static Event buildEvent() {
        return Event.builder()
                    .name("event-name")
                    .fromFirstTime("-10min")
                    .from("-5min")
                    .processors(List.of(ProcessorModel.builder()
                                                      .name(PROCESSOR_NAME)
                                                      .build()))

                    .targets(List.of(TargetConfig.builder()
                                                 .build()))
                    .scheduler("0 0/5 * * * ?")
                    .mapper("mapper")
                    .provider(PROVIDER)
                    .build();
    }

    public static Plugin buildPlugin() {
        return Plugin.builder()
                     .config(PluginConfiguration.builder()
                                                .enable(true)
                                                .build())
                     .gav(buildGav())
                     .events(EventConfig.builder()
                                        .gav(buildGav())
                                        .name("events")
                                        .enable(true)
                                        .events(buildEvent())
                                        .simpleEvents(buildSimpleEvent())
                                        .build())
                     .build();
    }

    public static Gav buildGav() {
        return Gav.builder()
                  .groupId("io.inugami.plugin")
                  .artifactId("inu-test")
                  .version("4.3.0")
                  .qualifier("jar")
                  .build();
    }

    public static FutureData<ProviderFutureResult> buildProviderFutureResult(final LocalDateTime date) {
        return FutureDataModel.<ProviderFutureResult>builder()
                              .future(ImmediateFutureData.<ProviderFutureResult>builder()
                                                         .data(ProviderFutureResult.builder()
                                                                                   .channel("SSE_inugami")
                                                                                   .data(DataPoint.builder()
                                                                                                  .timestamp(1766064662604L)
                                                                                                  .value(15.5)
                                                                                                  .build())
                                                                                   .build())
                                                         .build())
                              .build();
    }

    public static EnginePluginResultDTO buildEnginePluginResultDTO() {
        return EnginePluginResultDTO.builder()
                                    .gav(buildGav())
                                    .status(Status.SUCCESS)
                                    .message("success")
                                    .events(List.of(buildEnginePluginEventResultDTO()))
                                    .build();
    }

    private static EnginePluginEventResultDTO buildEnginePluginEventResultDTO() {
        return EnginePluginEventResultDTO.builder()
                                         .name("event")
                                         .status(Status.SUCCESS)
                                         .message("success")
                                         .data(ProviderFutureResult.builder()
                                                                   .data(List.of(15, 52))
                                                                   .build())
                                         .build();
    }
}
