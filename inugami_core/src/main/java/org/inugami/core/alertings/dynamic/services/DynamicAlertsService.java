package org.inugami.core.alertings.dynamic.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.FatalException;
import org.inugami.api.loggers.Loggers;
import org.inugami.core.alertings.dynamic.entities.DynamicAlertEntity;
import org.inugami.core.context.ApplicationContext;

@Named
public class DynamicAlertsService {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @Inject
    private ApplicationContext context;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DynamicAlertsService() {
    }
    
    protected DynamicAlertsService(final ApplicationContext context) {
        this.context = context;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public void process(final List<DynamicAlertEntity> entities) {
        Asserts.notNull(entities);
        
        final List<DynamicAlertEntity> alertsToProcess = resolveAlertsToProcess(entities, System.currentTimeMillis());
        
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
            
            if ((cronExpression != null) && cronResolver.willFire(timestamp)) {
                result.add(entity);
            }
        }
        return result;
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
}
