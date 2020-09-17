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

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.DecimalFormat;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.tools.RunnableContext;
import io.inugami.core.model.system.CpuUsage;
import io.inugami.core.model.system.JvmMemoryUsage;
import io.inugami.core.model.system.SystemInformations;
import io.inugami.core.model.system.ThreadsUsage;
import io.inugami.core.services.scheduler.SchedulerServiceFatalException;
import io.inugami.core.services.sse.SseService;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * SystemInfosManager
 * 
 * @author pguillerm
 * @since 3 sept. 2017
 */
public final class SystemInfosManager implements RunnableContext {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final SystemInformations        systemInfos = new SystemInformations();
    
    private static final String             CRON_EXP    = "0/30 * * * * ?";
    
    public static final long                TIMEOUT     = 29000;
    
    private boolean                         started     = false;
    
    private Scheduler                       scheduler;
    
    private final Trigger                   trigger;
    
    private final JobDetail                 job;
    
    private final static SystemInfosManager INSTANCE    = new SystemInfosManager();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private SystemInfosManager() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        }
        catch (final SchedulerException e) {
            throw new SchedulerServiceFatalException(e.getMessage(), e);
        }
        trigger = newTrigger().withIdentity("scheduler", "system").withSchedule(cronSchedule(CRON_EXP)).build();
        
        job = newJob(SystemInfoJob.class).withIdentity("scheduler", "systemJob").build();
        
        start();
    }
    
    public static SystemInfosManager getInstance() {
        return INSTANCE;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    /* package */ void addCpuUsages(final CpuUsage usage) {
        systemInfos.addCpuUsage(usage);
    }
    
    /* package */ void addMemoryUsages(final JvmMemoryUsage usage) {
        systemInfos.addJvmMemoryUsage(usage);
    }
    
    /* package */ void addThreadsUsage(final ThreadsUsage usage) {
        systemInfos.addThreadsUsage(usage);
    }
    
    /* package */ void addNbSockets() {
        systemInfos.addNbSocketsOpen(SseService.getNbSocketsOpen());
    }
    
    /* package */ void grabInfosDone() {
        
        final CpuUsage cpu = systemInfos.getCpu();
        final JvmMemoryUsage memory = systemInfos.getMemory();
        final ThreadsUsage threads = systemInfos.getThreads();
        systemInfos.setUsers(SseService.getUserSockets());
        
        final DecimalFormat formatter = new DecimalFormat("#0.00");
        final StringBuilder msg = new StringBuilder();
        if (cpu != null) {
            msg.append("cpu : ").append(formatter.format(cpu.getUse())).append("%");
        }
        if (memory != null) {
            msg.append(", memory : ").append(formatter.format(memory.getUsedMB())).append("MB");
        }
        if (cpu != null) {
            msg.append(", sockets : ").append(systemInfos.getNbSockets());
        }
        msg.append(", threads : ").append(threads.getNbThreads());
        Loggers.SYSTEM.info(">> {}", msg.toString());
        
        sendSystemInformations(systemInfos);
        
    }
    
    private void sendSystemInformations(final SystemInformations systemInfos) {
        SseService.sendAdminEvent("system", systemInfos);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public boolean isStarted() {
        return started;
    }
    
    @Override
    public void start() {
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
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public SystemInformations getSystemInfos() {
        return systemInfos;
    }
    
}
