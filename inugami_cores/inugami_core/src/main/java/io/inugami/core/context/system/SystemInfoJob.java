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
package io.inugami.core.context.system;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.JsonBuilder;
import io.inugami.core.cdi.scheduler.SchedulerSystem;
import io.inugami.core.context.Context;
import io.inugami.core.model.system.CpuUsage;
import io.inugami.core.model.system.JvmMemoryUsage;
import io.inugami.core.model.system.SimpleThreadInfo;
import io.inugami.core.model.system.ThreadsUsage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.enterprise.inject.spi.CDI;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SystemInfoJob
 *
 * @author pguillerm
 * @since 3 sept. 2017
 */
@SuppressWarnings({"java:S2153", "java:S2129"})
public class SystemInfoJob implements Job {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        fireCdiEvent();
        grabCpuUsage();
        grabMemoryUsage();
        grabThreadsUsages();
        SystemInfosManager.getInstance().addNbSockets();
        SystemInfosManager.getInstance().grabInfosDone();
    }

    // =========================================================================
    // CDI
    // =========================================================================
    private void fireCdiEvent() {
        final SchedulerSystem schedulerSystem = CDI.current().select(SchedulerSystem.class).get();
        schedulerSystem.fireSchedulerEvent();
    }

    // =========================================================================
    // PRIVATE
    // =========================================================================
    private void grabCpuUsage() {
        final OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        SystemInfosManager.getInstance().addCpuUsages(new CpuUsage(operatingSystemMXBean.getSystemLoadAverage()));
    }

    private void grabMemoryUsage() {
        final Runtime runtime = Runtime.getRuntime();

        final long init = runtime.totalMemory();
        final long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        final JvmMemoryUsage usage = new JvmMemoryUsage(init, used, runtime.maxMemory());
        SystemInfosManager.getInstance().addMemoryUsages(usage);
    }

    private void grabThreadsUsages() {
        final ThreadMXBean     threadMxBean    = ManagementFactory.getThreadMXBean();
        final List<ThreadInfo> rawThreadsInfos = Arrays.asList(threadMxBean.getThreadInfo(threadMxBean.getAllThreadIds()));

        // @formatter:off
		final List<SimpleThreadInfo> threadsInfos = rawThreadsInfos.stream()
		                                                           .filter(Objects::nonNull)
																   .map(item -> new SimpleThreadInfo(
																		   				item.getThreadName(),
																		   				item.getThreadState()
																		   	   )
																   )
																   .collect(Collectors.toList());
		// @formatter:on
        final ThreadsUsage usage = new ThreadsUsage(rawThreadsInfos.size(), threadsInfos);
        SystemInfosManager.getInstance().addThreadsUsage(usage);

        applyThreadsSecurity(threadsInfos.size());
    }


    private void applyThreadsSecurity(final int nbThreads) {
        final int max_threads = Context.getInstance().getApplicationConfiguration().getMaxThreads();

        if (nbThreads > max_threads) {
            final String msg = "too many threads running !({}/{})";
            Loggers.XLLOG.error(msg, nbThreads, max_threads);
            Loggers.SYSTEM.error(msg, nbThreads, max_threads);
            Runtime.getRuntime().exit(1);
        }

        final int percent       = Double.valueOf((Double.valueOf(nbThreads) / max_threads) * 100).intValue();
        final int minAcceptable = 50;

        if (percent > minAcceptable) {
            final JsonBuilder json = new JsonBuilder();
            json.openObject();
            json.addField("nbThreads").write(nbThreads).addSeparator();
            json.addField("maxThreads").write(max_threads);
            json.closeObject();

            final AlertingResult alert = AlertingResult.builder()
                                                       .alerteName("system.too.many.threads.running")
                                                       .created(System.currentTimeMillis())
                                                       .message("_system.too.many.threads.running")
                                                       .data(json.toJsonObject())
                                                       .build();


            String level = null;
            if (percent > 80) {
                level = "error inugami-system-threads";
            } else if (percent > 60) {
                level = "warn inugami-system-threads";
            } else if (percent > 50) {
                level = "info inugami-system-threads";
            }

            alert.setLevel(level);
            Context.getInstance().sendAlert(alert);
        }
    }

}
