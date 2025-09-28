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
package io.inugami.dashboard.infrastructure.internal.schduler;

import io.inugami.dashboard.api.domain.engine.IEngineService;
import io.inugami.framework.commons.threads.MonitoredThreadFactory;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
public class SchedulerConfiguration {
    private ScheduledThreadPoolExecutor engineScheduledThreadPoolExecutor;


    @Bean
    public ScheduledThreadPoolExecutor engineScheduledThreadPoolExecutor() {
        this.engineScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(5, new MonitoredThreadFactory("engine_scheduled_threadPool", false));
        return this.engineScheduledThreadPoolExecutor;

    }

    @Bean
    public SchedulerProducer schedulerProducer(final IEngineService engineService,
                                               final ScheduledThreadPoolExecutor engineScheduledThreadPoolExecutor) {

        return SchedulerProducer.builder()
                                .engineService(engineService)
                                .executor(engineScheduledThreadPoolExecutor)
                                .build()
                                .init();
    }

    @PreDestroy
    public void shutdown() {
        if (engineScheduledThreadPoolExecutor != null) {
            engineScheduledThreadPoolExecutor.shutdown();
        }
    }
}
