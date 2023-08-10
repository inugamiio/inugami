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
package io.inugami.core.services.scheduler;

import io.inugami.api.loggers.Loggers;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * SchedulerService
 *
 * @author patrick_guillerm
 * @since 17 janv. 2017
 */
@SuppressWarnings({"java:S2629"})
public class SchedulerService {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String CTX_RAW_CRON_EXPRESSION = "rawCronExpression";

    public static final String CTX_CRON_EXPRESSION = "cronExpression";

    private final String rawCronExpression;

    private final Scheduler scheduler;

    private final Trigger trigger;

    private final JobDetail job;

    private boolean started;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public SchedulerService(final String cronExpression, final String rawCronExpression) {
        this.rawCronExpression = rawCronExpression;

        Loggers.INIT.info("Scheduler : {}", cronExpression);
        final String key = "scheduler" + rawCronExpression;

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (final SchedulerException e) {
            throw new SchedulerServiceFatalException(e.getMessage(), e);
        }

        //@formatter:off
        trigger = newTrigger()
                        .withIdentity(key+"_trigger",key)
                        .withSchedule(cronSchedule(cronExpression))
                        .build();
        //@formatter:on
        final JobDataMap jobData = new JobDataMap();
        jobData.put(CTX_RAW_CRON_EXPRESSION, rawCronExpression);
        jobData.put(CTX_CRON_EXPRESSION, cronExpression);

        job = newJob(EventJob.class).withIdentity(key + "scheduler", key).setJobData(jobData).build();

    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public synchronized void start() {
        if (!started) {
            try {
                Loggers.INIT.info("Scheduler starting : {}", SchedulerNameHelper.buildName(rawCronExpression));
                scheduler.start();
                scheduler.scheduleJob(job, trigger);
                started = true;
            } catch (final SchedulerException e) {
                throw new SchedulerServiceFatalException(e.getMessage(), e);
            }
        }
    }

    public synchronized void shutdown() {
        if (started) {
            try {
                scheduler.shutdown(true);
                started = false;
            } catch (final SchedulerException e) {
                throw new SchedulerServiceFatalException(e.getMessage(), e);
            }
        }
    }

}
