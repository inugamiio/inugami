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
package org.inugami.core.alertings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.inugami.api.alertings.AlertingProvider;
import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.dao.Dao;
import org.inugami.api.dao.DaoEntityNotFoundException;
import org.inugami.api.dao.DaoException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.mapping.Mapper;
import org.inugami.api.models.data.basic.Json;
import org.inugami.core.alertings.dynamic.entities.DynamicAlertEntity;
import org.inugami.core.alertings.dynamic.services.DynamicAlertsService;
import org.inugami.core.cdi.scheduler.SystemEvent;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.services.sse.SseService;

/**
 * AlertServices
 * 
 * @author patrick_guillerm
 * @since 19 janv. 2018
 */
@Named
@ApplicationScoped
public class AlertServices implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long                             serialVersionUID = -758552222816727158L;
    
    @Inject
    private Dao                                           dao;
    
    @Inject
    private ApplicationContext                            context;
    
    @Inject
    private DynamicAlertsService                          dynamicAlertsService;
    
    private List<AlertingProvider>                        alertsProviders;
    
    private transient Mapper<AlertingResult, AlertEntity> transformEntityToResult;
    
    // =========================================================================
    // POST CONSTRUCT
    // =========================================================================
    @PostConstruct
    public void initialize() {
        alertsProviders = context.getAlertingProviders();
        transformEntityToResult = new TransformAlertEntityToResult();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    //@formatter:off
    public void manageAlertsSaved(@Observes  final SystemEvent event) {
        //@formatter:on
        try {
            processManageAlertsSaved();
        }
        catch (final DaoException e) {
            Loggers.ALERTING.error(e.getMessage());
            Loggers.DEBUG.error(e.getMessage(), e);
        }
    }
    
    // =========================================================================
    // private
    // =========================================================================
    private void processManageAlertsSaved() throws DaoException {
        final int nbAlerts = dao.count(AlertEntity.class);
        final Double nbStep = Math.ceil(new Double(nbAlerts) / 100.0);
        
        for (int page = 0; page < nbStep; page++) {
            final List<AlertEntity> entities = dao.find(AlertEntity.class, page, 100, "alerteName", null, null);
            processAlerts(entities);
        }
    }
    
    private void processAlerts(final List<AlertEntity> entities) {
        try {
            final List<AlertEntity> alertEntities = new ArrayList<>(entities);
            final List<AlertEntity> dynamicAlerts = extractAlertEntities(alertEntities);
            
            deleteExpireEntities(alertEntities);
            sendAlerts(alertEntities);
            processDynamicAlerts(dynamicAlerts);
        }
        catch (final DaoException e) {
            Loggers.ALERTING.error(e.getMessage());
            Loggers.DEBUG.error(e.getMessage(), e);
        }
        
    }
    
    // =========================================================================
    // DELETES
    // =========================================================================
    private void deleteExpireEntities(final List<AlertEntity> entities) throws DaoEntityNotFoundException,
                                                                        DaoException {
        final List<AlertEntity> entititesToDelete = new ArrayList<>();
        final List<AlertEntity> entititesToDisable = new ArrayList<>();
        
        final long now = System.currentTimeMillis();
        for (final AlertEntity entity : entities) {
            if (isNotScheduledAlert(entity)) {
                if (entity.getTtl() < now) {
                    entititesToDelete.add(entity);
                }
            }
            if (!entity.isEnable()) {
                entititesToDisable.add(entity);
            }
        }
        if (!entititesToDisable.isEmpty()) {
            disableAlerts(entititesToDisable);
        }
        if (!entititesToDelete.isEmpty()) {
            disableAlerts(entititesToDelete);
            dao.delete(entititesToDelete, AlertEntity.class);
            SseService.sendAdminEvent("alerts_update", new Json(null));
        }
    }
    
    private void disableAlerts(final List<AlertEntity> entititesToDelete) {
        final Map<String, List<String>> data = reduceAlertByChannel(entititesToDelete);
        for (final Map.Entry<String, List<String>> entry : data.entrySet()) {
            processDisableAlerts(entry.getValue(), entry.getKey());
        }
    }
    
    public void processDisableAlerts(final List<String> uids, final String chanel) {
        for (final AlertingProvider provider : alertsProviders) {
            provider.processDisableAlerts(uids, chanel);
        }
    }
    
    private Map<String, List<String>> reduceAlertByChannel(final List<AlertEntity> entities) {
        final Map<String, List<String>> result = new HashMap<>();
        
        for (final AlertEntity entity : entities) {
            //@formatter:off
            final String[] channels = entity.getChannel() == null ? new String[] { "@all" }
                                                                  : entity.getChannel().split(" ");
            //@formatter:on
            
            for (final String channel : channels) {
                if (!result.containsKey(channel)) {
                    result.put(channel, new ArrayList<>());
                }
                result.get(channel).add(entity.getAlerteName());
            }
        }
        return result;
    }
    
    // =========================================================================
    // SEND ALERTS
    // =========================================================================
    private void sendAlerts(final List<AlertEntity> entities) {
        if (context.getApplicationConfiguration().isAlertingEnable()) {
            final long now = System.currentTimeMillis();
            //@formatter:off
            final List<AlertingResult> alerts = entities.stream()
                                                        .filter(AlertEntity::isEnable)
                                                        .filter(entity->entity.getTtl()>=now)
                                                        .map(transformEntityToResult::mapping)
                                                        .collect(Collectors.toList());
            //@formatter:off
            for (final AlertingProvider provider : alertsProviders) {
                provider.processSavedAlerts(alerts);
            }
        }
    }

    
    // =========================================================================
    // DYNAMIC ALERTS
    // =========================================================================
    private List<AlertEntity> extractAlertEntities(final List<AlertEntity> entities) {
        final List<AlertEntity>  result = new ArrayList<>();
        if(entities != null) {
            for(final AlertEntity entity : entities) {
                if(entity instanceof DynamicAlertEntity) {
                    result.add(entity);
                }
            }
            entities.removeAll(result);
        }
        return result;
    }
    
    
    private void processDynamicAlerts(final List<AlertEntity> dynamicAlerts) {
        if(!dynamicAlerts.isEmpty()) {
            //@formatter:off
            final List<DynamicAlertEntity> entities = dynamicAlerts.stream()
                                                                   .map((item)->(DynamicAlertEntity)item)
                                                                   .collect(Collectors.toList());
            //@formatter:on
            
            dynamicAlertsService.process(entities);
        }
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private boolean isNotScheduledAlert(final AlertEntity entity) {
        return true;
    }
    
}
