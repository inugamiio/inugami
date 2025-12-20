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
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Builder
@Service
public class SchedulerProducer implements ApplicationListener<ApplicationEvent> {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private final ScheduledThreadPoolExecutor executor;
    private final IEngineService              engineService;
    private final Clock                       clock;

    //==================================================================================================================
    // LIGECYCLE
    //==================================================================================================================
    @Override
    public void onApplicationEvent(final ApplicationEvent event) {
        if (event instanceof ApplicationStartedEvent) {
            executor.scheduleAtFixedRate(this::run, computeDelay(), 1000, TimeUnit.MILLISECONDS);
        }
    }

    //==================================================================================================================
    // PRODUCER
    //==================================================================================================================
    void run() {
        engineService.run();
    }


    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    protected long computeDelay() {
        final Timestamp now = Timestamp.from(LocalDateTime.now(clock).toInstant(ZoneOffset.UTC));
        final Timestamp future = Timestamp.from(LocalDateTime.now(clock)
                                                             .withNano(0)
                                                             .plusSeconds(1L)
                                                             .toInstant(ZoneOffset.UTC));
        return future.getTime() - now.getTime();
    }


}
