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
package io.inugami.core.services.sse;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import io.inugami.api.models.tools.RunnableContext;
import io.inugami.core.services.scheduler.SchedulerServiceFatalException;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * SseReconnect
 * 
 * @author patrick_guillerm
 * @since 3 avr. 2018
 */
public class SseReconnect implements RunnableContext {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private boolean             started  = false;
    
    private static final String CRON_EXP = "0 1-59/10 * * * ?";
    
    private Scheduler           scheduler;
    
    private final Trigger       trigger;
    
    private final JobDetail     job;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SseReconnect() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        }
        catch (final SchedulerException e) {
            throw new SchedulerServiceFatalException(e.getMessage(), e);
        }
        trigger = newTrigger().withIdentity("scheduler", "sseReconnect").withSchedule(cronSchedule(CRON_EXP)).build();
        
        job = newJob(SseReconnectJob.class).withIdentity("scheduler", "sseReconnect").build();
        
        start();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public boolean isStarted() {
        return started;
    }
    
    @Override
    public final void start() {
        if (!started) {
            try {
                scheduler.start();
                scheduler.scheduleJob(job, trigger);
                started = true;
            }
            catch (final SchedulerException e) {
                throw new SchedulerServiceFatalException(e.getMessage(), e);
            }
        }
        
    }
    
    @Override
    public void shutdown() {
        if (started) {
            try {
                scheduler.shutdown(true);
                started = false;
            }
            catch (final SchedulerException e) {
                throw new SchedulerServiceFatalException(e.getMessage(), e);
            }
        }
    }
}
