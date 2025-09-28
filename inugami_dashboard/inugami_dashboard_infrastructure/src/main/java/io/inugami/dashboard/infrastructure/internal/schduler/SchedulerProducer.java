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

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Builder
public class SchedulerProducer {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private final ScheduledThreadPoolExecutor executor;
    private final IEngineService              engineService;

    //==================================================================================================================
    // LIGECYCLE
    //==================================================================================================================
    public SchedulerProducer init() {
        executor.schedule(this::run, 1000, TimeUnit.MILLISECONDS);
        return this;
    }


    //==================================================================================================================
    // PRODUCER
    //==================================================================================================================
    void run() {
        engineService.run();
    }

}
