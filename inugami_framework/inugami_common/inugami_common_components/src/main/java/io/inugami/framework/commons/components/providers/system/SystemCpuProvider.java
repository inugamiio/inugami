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
package io.inugami.framework.commons.components.providers.system;

import io.inugami.framework.commons.threads.runner.MultiThreadedProviderRunner;
import io.inugami.framework.interfaces.concurrent.FutureData;
import io.inugami.framework.interfaces.concurrent.FutureDataModel;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.exceptions.services.ProviderException;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.graphite.TimeValue;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.models.number.FloatNumber;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.providers.ProviderRunner;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import io.inugami.framework.interfaces.task.ProviderTask;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Slf4j
public class SystemCpuProvider implements Provider {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String         SYSTEM     = "system";
    public static final String         SYSTEM_CPU = "system.cpu";
    private             ProviderRunner providerRunner;


    //==================================================================================================================
    // INIT
    //==================================================================================================================
    @Override
    public String getType() {
        return SYSTEM;
    }

    @Override
    public void postConstruct(final ConfigHandler<String, String> configuration, final ManifestInfo manifest) {
        log.debug("initialize SystemCpuProvider");
        providerRunner = MultiThreadedProviderRunner.builder()
                                                    .componentName(SYSTEM_CPU)
                                                    .config(configuration)
                                                    .build();
    }

    //==================================================================================================================
    // Provider
    //==================================================================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        final var task = SystemCpuProviderTask.builder()
                                              .build();
        final Future<ProviderFutureResult> future = providerRunner.run(getName(), task);
        return FutureDataModel.<ProviderFutureResult>builder()
                              .event(event)
                              .future(future)
                              .task(task)
                              .build();
    }

    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        return ProviderFutureResult.builder()
                                   .data(Optional.ofNullable(data).orElse(List.of())
                                                 .stream()
                                                 .map(ProviderFutureResult::getData)
                                                 .filter(Objects::nonNull)
                                                 .flatMap(List::stream)
                                                 .toList())
                                   .build();
    }


    //==================================================================================================================
    // VALIDATE
    //==================================================================================================================
    @Setter
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class SystemCpuProviderTask implements ProviderTask {
        private final Gav         pluginGav;
        private final SimpleEvent event;


        @Override
        public ProviderFutureResult callProvider() {
            return ProviderFutureResult.builder()
                                       .data(TimeValue.builder()
                                                      .path(SYSTEM_CPU)
                                                      .value(FloatNumber.of(10.0))
                                                      .time(System.currentTimeMillis())
                                                      .build())
                                       .build();
        }
    }

}
