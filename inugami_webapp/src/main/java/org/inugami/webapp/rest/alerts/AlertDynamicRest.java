package org.inugami.webapp.rest.alerts;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import org.inugami.api.dao.DaoException;
import org.inugami.api.loggers.Loggers;
import org.inugami.commons.security.ItemProcessor;
import org.inugami.commons.security.SecurityTools;
import org.inugami.core.alertings.dynamic.entities.ActivationTime;
import org.inugami.core.alertings.dynamic.entities.AlertDataTransfomer;
import org.inugami.core.alertings.dynamic.entities.DynamicAlertEntity;
import org.inugami.core.alertings.dynamic.entities.DynamicLevel;
import org.inugami.core.alertings.dynamic.entities.ProviderSource;
import org.inugami.core.alertings.dynamic.entities.Tag;
import org.inugami.core.alertings.dynamic.entities.TimeSlot;
import org.inugami.core.cdi.services.dao.AbstractCrudRest;
import org.inugami.core.cdi.services.dao.PostCrudHandler;

@Path("alert/dynamic")
public class AlertDynamicRest extends AbstractCrudRest<DynamicAlertEntity, String>
        implements PostCrudHandler<DynamicAlertEntity> {
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    @Override
    protected Class<? extends DynamicAlertEntity> initType() {
        return DynamicAlertEntity.class;
    }
    
    @Override
    protected String parseUid(final String uid) throws DaoException {
        return uid;
    }
    
    @Override
    protected DynamicAlertEntity secureXssEntity(final DynamicAlertEntity entity) {
        //@formatter:off
        SecurityTools.secureJavaScriptAndHtml(entity::getAlerteName, entity::setAlerteName);
        SecurityTools.secureJavaScriptAndHtml(entity::getLevel,      entity::setLevel);
        SecurityTools.secureJavaScriptAndHtml(entity::getLabel,      entity::setLabel);
        SecurityTools.secureJavaScriptAndHtml(entity::getSubLabel,   entity::setSubLabel);
        SecurityTools.secureJavaScriptAndHtml(entity::getChannel,    entity::setChannel);
        SecurityTools.secureJavaScriptAndHtml(entity::getData,       entity::setData);

        SecurityTools.secureJavaScriptAndHtml(entity.getTags()  , new ItemProcessor<>(Tag::getName, (item, content) -> item.setName(content)));
        SecurityTools.secureJavaScriptAndHtml(entity.getLevels(), new ItemProcessor<>(DynamicLevel::getName, (item, content) -> item.setName(content)));
        
        if(entity.getSource()!=null) {
            final ProviderSource source = entity.getSource();
            SecurityTools.secureJavaScriptAndHtml(source::getProvider,        source::setProvider);
            SecurityTools.secureJavaScriptAndHtml(source::getCronExpression,  source::setCronExpression);
            SecurityTools.secureJavaScriptAndHtml(source::getFrom,            source::setFrom);
            SecurityTools.secureJavaScriptAndHtml(source::getTo,              source::setTo);
        }
  
        if(entity.getTransformer()!=null) {
            final AlertDataTransfomer transfo = entity.getTransformer();
            SecurityTools.secureJavaScriptAndHtml(transfo::getName,        transfo::setName);
        }
        
        if(entity.getActivations() !=null) {
            for(final ActivationTime activationTime : entity.getActivations()) {
                if(activationTime.getDays()!=null) {
                    final List<String> days = activationTime.getDays()
                                                      .stream()
                                                      .map(SecurityTools::escapeJavaScriptAndHtml)
                                                      .collect(Collectors.toList());
                    
                    activationTime.setDays(days);
                }
                
                SecurityTools.secureJavaScriptAndHtml(activationTime.getHours(),
                                                      new ItemProcessor<>(TimeSlot::getFrom, (item, content) -> item.setFrom(content)),
                                                      new ItemProcessor<>(TimeSlot::getTo, (item, content) -> item.setTo(content)));
            }
        }
        //@formatter:on
        return entity;
    }
    
    // =========================================================================
    // HANDLER
    // =========================================================================
    @Override
    protected PostCrudHandler<DynamicAlertEntity> initHandler() {
        return this;
    }
    
    @Override
    public void onFindAll(final List<DynamicAlertEntity> result) {
        Loggers.REST.info("find {} entities", result == null ? 0 : result.size());
    }
    
    @Override
    public void onFind(final List<DynamicAlertEntity> result) {
        Loggers.REST.info("find {} entities", result == null ? 0 : result.size());
    }
    
    @Override
    public void onSave(final List<DynamicAlertEntity> listEntity) {
        Loggers.REST.info("save {} entities", listEntity == null ? 0 : listEntity.size());
    }
    
}
