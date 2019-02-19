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
package org.inugami.monitoring.sensors.defaults.system;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

import org.inugami.api.models.data.graphite.number.LongNumber;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.monitoring.api.data.GenericMonitoringModel;
import org.inugami.monitoring.api.data.GenericMonitoringModelBuilder;
import org.inugami.monitoring.api.sensors.MonitoringSensor;
import org.inugami.monitoring.api.tools.GenericMonitoringModelTools;

/**
 * ThreadsSensor
 * 
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
public class ThreadsSensor implements MonitoringSensor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final long    interval;
    
    private final String  timeUnit;
    
    private final boolean enableThreadsDump;
    
    private final int     maxDepth;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ThreadsSensor() {
        interval = -1;
        timeUnit = null;
        enableThreadsDump = true;
        maxDepth = 25;
    }
    
    public ThreadsSensor(final long interval, final String query, final ConfigHandler<String, String> configuration) {
        super();
        this.interval = interval;
        timeUnit = configuration.grabOrDefault("timeUnit", "");
        enableThreadsDump = configuration.grabBoolean("enableThreadsDump", true);
        maxDepth = configuration.grabInt("maxDepth", 25);
    }
    
    @Override
    public MonitoringSensor buildInstance(final long interval, final String query,
                                          final ConfigHandler<String, String> configuration) {
        return new ThreadsSensor(interval, query, configuration);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Override
    public List<GenericMonitoringModel> process() {
        final List<GenericMonitoringModel> result = new ArrayList<>();
        final GenericMonitoringModelBuilder builder = GenericMonitoringModelTools.initResultBuilder();
        builder.setCounterType("system");
        builder.setService("threads");
        if ((timeUnit == null) || timeUnit.isEmpty()) {
            builder.setTimeUnit(String.format("%sms", interval));
        }
        else {
            builder.setTimeUnit(timeUnit);
        }
        
        final ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
        //@formatter:off
        final List<ThreadInfo> rawThreadsInfos = buildList(threadMxBean.getThreadInfo(threadMxBean.getAllThreadIds(),
                                                                                          enableThreadsDump ? maxDepth: 0));
        //@formatter:on
        
        final ThreadsCounter data = extractThreadsUsage(rawThreadsInfos);
        // ALL //--------------------------------------------------------------
        builder.setSubService("all");
        builder.setValue(new LongNumber(data.count()));
        if (enableThreadsDump) {
            builder.setData(buildStackTrace(rawThreadsInfos, null));
        }
        result.add(builder.build());
        
        // NEW //--------------------------------------------------------------
        builder.setSubService("newThreads");
        builder.setValue(new LongNumber(data.getNewThreads()));
        if (enableThreadsDump) {
            builder.setData(buildStackTrace(rawThreadsInfos, Thread.State.NEW));
        }
        result.add(builder.build());
        
        // RUNNABLE //---------------------------------------------------------
        builder.setSubService("runable");
        builder.setValue(new LongNumber(data.getRunnable()));
        if (enableThreadsDump) {
            builder.setData(buildStackTrace(rawThreadsInfos, Thread.State.RUNNABLE));
        }
        result.add(builder.build());
        
        // BLOCKED //----------------------------------------------------------
        builder.setSubService("blocked");
        builder.setValue(new LongNumber(data.getBlocked()));
        if (enableThreadsDump) {
            builder.setData(buildStackTrace(rawThreadsInfos, Thread.State.BLOCKED));
        }
        result.add(builder.build());
        
        // WAITING //----------------------------------------------------------
        builder.setSubService("waiting");
        builder.setValue(new LongNumber(data.getWaitting()));
        if (enableThreadsDump) {
            builder.setData(buildStackTrace(rawThreadsInfos, Thread.State.WAITING));
        }
        result.add(builder.build());
        
        // TIMED_WAITING //----------------------------------------------------
        builder.setSubService("timedWaiting");
        builder.setValue(new LongNumber(data.getTimedWaiting()));
        if (enableThreadsDump) {
            builder.setData(buildStackTrace(rawThreadsInfos, Thread.State.TIMED_WAITING));
        }
        result.add(builder.build());
        
        // TERMINATED //-------------------------------------------------------
        builder.setSubService("terminated");
        builder.setValue(new LongNumber(data.getTerminated()));
        if (enableThreadsDump) {
            builder.setData(buildStackTrace(rawThreadsInfos, Thread.State.TERMINATED));
        }
        result.add(builder.build());
        return result;
        
    }
    
    private List<ThreadInfo> buildList(final ThreadInfo[] threadInfo) {
        final List<ThreadInfo> result = new ArrayList<>();
        for (int i = threadInfo.length - 1; i >= 0; i--) {
            if (threadInfo[i] != null) {
                result.add(threadInfo[i]);
            }
        }
        return result;
    }
    
    private ThreadsCounter extractThreadsUsage(final List<ThreadInfo> rawThreadsInfos) {
        final ThreadsCounter counter = new ThreadsCounter();
        for (final ThreadInfo info : rawThreadsInfos) {
            final Thread.State state = info.getThreadState();
            
            if (state != null) {
                switch (state) {
                    case BLOCKED:
                        counter.addBlocked();
                        break;
                    case NEW:
                        counter.addNewThreads();
                        break;
                    case RUNNABLE:
                        counter.addRunnable();
                        break;
                    case TERMINATED:
                        counter.addterminated();
                        break;
                    case TIMED_WAITING:
                        counter.addTimedWaitting();
                        break;
                    case WAITING:
                        counter.addWaitting();
                        break;
                    default:
                        break;
                }
            }
            
        }
        
        return counter;
    }
    
    private String buildStackTrace(final List<ThreadInfo> rawThreadsInfos, final Thread.State state) {
        final StringBuilder result = new StringBuilder();
        
        for (final ThreadInfo info : rawThreadsInfos) {
            if ((state == null) || (state == info.getThreadState())) {
                renderStackTrace(info, result);
                result.append("\n");
            }
        }
        return result.toString();
    }
    
    private String renderStackTrace(final ThreadInfo info, final StringBuilder result) {
        result.append(info.getThreadState());
        result.append(" - ");
        result.append(info.getThreadName());
        switch (info.getThreadState()) {
            case BLOCKED:
                result.append(" block time : ").append(info.getBlockedTime()).append("ms");
                break;
            case WAITING:
                result.append(" waiting time : ").append(info.getWaitedTime()).append("ms");
                break;
            case TIMED_WAITING:
                result.append(" timed waiting time : ").append(info.getWaitedTime()).append("ms");
                break;
            default:
                break;
        }
        result.append("\n\t");
        for (final StackTraceElement stack : info.getStackTrace()) {
            result.append("\t\t\t").append(stack.toString()).append("\n");
        }
        return result.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getName() {
        return "threads";
    }
    
    @Override
    public long getInterval() {
        return interval;
    }
}
