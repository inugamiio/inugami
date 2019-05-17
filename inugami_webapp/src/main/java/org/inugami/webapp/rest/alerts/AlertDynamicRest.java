package org.inugami.webapp.rest.alerts;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import org.inugami.api.dao.DaoException;
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

@Path("alert/dynamic")
public class AlertDynamicRest extends AbstractCrudRest<DynamicAlertEntity, String> {
    
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
        SecurityTools.secureJavaScriptAndHtml(entity::getUrl,        entity::setUrl);
        SecurityTools.secureJavaScriptAndHtml(entity::getChannel,    entity::setChannel);
        SecurityTools.secureJavaScriptAndHtml(entity::getData,       entity::setData);
        SecurityTools.secureJavaScriptAndHtml(entity::getScript,     entity::setScript);

        SecurityTools.secureJavaScriptAndHtml(entity.getTags()  , new ItemProcessor<>(Tag::getName, (item, content) -> item.setName(content)));
        SecurityTools.secureJavaScriptAndHtml(entity.getLevels(), new ItemProcessor<>(DynamicLevel::getName, (item, content) -> item.setName(content)));
        
        if(entity.getSource()!=null) {
            final ProviderSource source = entity.getSource();
            SecurityTools.secureJavaScriptAndHtml(source::getProvider,        source::setProvider);
            SecurityTools.secureJavaScriptAndHtml(source::getCronExpression,  source::setCronExpression);
            SecurityTools.secureJavaScriptAndHtml(source::getFrom,            source::setFrom);
            SecurityTools.secureJavaScriptAndHtml(source::getTo,              source::setTo);
            SecurityTools.secureJavaScriptAndHtml(source::getQuery,           source::setQuery); 
        }
  
        if(entity.getTransformer()!=null) {
            final AlertDataTransfomer transfo = entity.getTransformer();
            SecurityTools.secureJavaScriptAndHtml(transfo::getName,        transfo::setName);
            SecurityTools.secureJavaScriptAndHtml(transfo::getScript,  transfo::setScript);
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
    
}
