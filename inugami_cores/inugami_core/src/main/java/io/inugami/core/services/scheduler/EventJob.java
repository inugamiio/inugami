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
import io.inugami.core.context.Context;
import org.quartz.*;

/**
 * SchedulerJob
 *
 * @author patrick_guillerm
 * @since 17 janv. 2017
 */
@SuppressWarnings({"java:S2629"})
public class EventJob implements InterruptableJob {

    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        final JobDataMap jobData           = context.getJobDetail().getJobDataMap();
        final String     cronExpression    = (String) jobData.get(SchedulerService.CTX_CRON_EXPRESSION);
        final String     rawCronExpression = (String) jobData.get(SchedulerService.CTX_RAW_CRON_EXPRESSION);

        Loggers.PLUGINS.info("Process scheduler : {}  ({})", SchedulerNameHelper.buildName(rawCronExpression),
                             cronExpression);
        Context.getInstance().processEventsForce(rawCronExpression);
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        // nothing to do
    }
}
