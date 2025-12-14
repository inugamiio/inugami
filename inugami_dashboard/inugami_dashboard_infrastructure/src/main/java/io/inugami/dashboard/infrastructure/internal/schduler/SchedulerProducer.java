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

import java.util.Calendar;
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
        final var now      = Calendar.getInstance().getTimeInMillis();
        final var calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        final long future = calendar.getTimeInMillis() + 1000;
        return future - now;
    }


}
