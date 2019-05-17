package org.inugami.core.alertings.dynamic.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.FatalException;
import org.inugami.api.loggers.Loggers;
import org.inugami.commons.threads.ExecutorFactory;
import org.inugami.core.alertings.dynamic.entities.ActivationTime;
import org.inugami.core.alertings.dynamic.entities.DynamicAlertEntity;
import org.inugami.core.context.ApplicationContext;

@ApplicationScoped
@Named
public class DynamicAlertsService implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long            serialVersionUID = -8151022786358668308L;
    
    @Inject
    private transient ApplicationContext context;
    
    private transient ExecutorService    executor;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DynamicAlertsService() {
    }
    
    protected DynamicAlertsService(final ApplicationContext context) {
        this.context = context;
        postConstruct();
    }
    
    @PostConstruct
    public void postConstruct() {
        final int maxThreads = context.getApplicationConfiguration().getAlertingDynamicMaxThreads();
        
        //@formatter:off
        executor = new ExecutorFactory(maxThreads,
                                       DynamicAlertsService.class.getSimpleName(),
                                       false,
                                       context.getGlobalConfiguration())
                           .getExecutorSerice();
        //@formatter:on
    }
    
    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public void process(final List<DynamicAlertEntity> entities) {
        Asserts.notNull(entities);
        
        final List<DynamicAlertEntity> alertsToProcess = resolveAlertsToProcess(entities, System.currentTimeMillis());
        
        if (!alertsToProcess.isEmpty()) {
            
            final List<DyncamicAlertsTask> tasks = buildTasks(alertsToProcess);
            
            processAlerting(tasks);
        }
    }
    
    // =========================================================================
    // RESOLVE ALERTS TO PROCESS
    // =========================================================================
    protected List<DynamicAlertEntity> resolveAlertsToProcess(final List<DynamicAlertEntity> entities,
                                                              final long timestamp) {
        final List<DynamicAlertEntity> result = new ArrayList<>();
        
        for (final DynamicAlertEntity entity : entities) {
            final String cronExpression = resolveCronExpression(entity);
            final CronResolver cronResolver = buildCronREsolver(cronExpression, entity.getUid());
            
            if ((cronExpression != null) && cronResolver.willFire(timestamp)
                && alertIsInTimeSlot(entity.getActivations())) {
                result.add(entity);
            }
        }
        return result;
    }
    
    private boolean alertIsInTimeSlot(final List<ActivationTime> activations) {
        Loggers.ALERTING.warn("alertIsInTimeSlot isn't implemented yet!");
        return true;
    }
    
    // =========================================================================
    // PROCESS ALERTING
    // =========================================================================
    private void processAlerting(final List<DyncamicAlertsTask> tasks) {
        tasks.forEach(executor::submit);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private String resolveCronExpression(final DynamicAlertEntity entity) {
        String result = null;
        if ((entity.getSource() != null) && (entity.getSource().getCronExpression() != null)) {
            result = context.getGlobalConfiguration().applyProperties(entity.getSource().getCronExpression());
        }
        
        return result;
    }
    
    private CronResolver buildCronREsolver(final String cronExpression, final String entityUid) {
        CronResolver result = null;
        if (cronExpression != null) {
            try {
                result = new CronResolver(cronExpression);
            }
            catch (final FatalException e) {
                final String message = String.format("unable to resolve cron expression %s from dynamic alert %s",
                                                     cronExpression, entityUid);
                Loggers.DEBUG.error(message, e);
                Loggers.ALERTS_SENDER.error(message);
            }
            
        }
        
        return result;
    }
    
    private List<DyncamicAlertsTask> buildTasks(final List<DynamicAlertEntity> alertsToProcess) {
        final List<DyncamicAlertsTask> result = new ArrayList<>();
        
        for (final DynamicAlertEntity entity : alertsToProcess) {
            result.add(new DyncamicAlertsTask(entity.cloneObject(), context));
        }
        return result;
    }
}
