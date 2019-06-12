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
package org.inugami.webapp.rest.alerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.inugami.api.alertings.AlertsSender;
import org.inugami.api.dao.Dao;
import org.inugami.api.dao.DaoEntityNotFoundException;
import org.inugami.api.dao.DaoException;
import org.inugami.api.tools.AnnotationTools;
import org.inugami.commons.security.SecurityTools;
import org.inugami.core.alertings.AlertEntity;
import org.inugami.core.alertings.AlertServices;
import org.inugami.core.cdi.services.dao.AbstractCrudRest;
import org.inugami.core.cdi.services.dao.CrudSecurityHandler;
import org.inugami.core.cdi.services.dao.PostCrudHandler;
import org.inugami.core.cdi.services.dao.PostCrudHandlerBuilder;
import org.inugami.core.security.commons.roles.Admin;
import org.inugami.core.security.commons.roles.crud.CrudSecurityHandlerAdminOnly;
import org.inugami.core.services.sse.SseService;
import org.inugami.webapp.rest.alerts.models.ProviderRestModel;
import org.picketlink.Identity;

/**
 * AlertsRest
 * 
 * @author patrick_guillerm
 * @since 9 janv. 2018
 */
@Path("alert")
public class AlertRest extends AbstractCrudRest<AlertEntity, String> {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @Inject
    private Dao                dao;
    
    @Inject
    private Identity           identity;
    
    @Inject
    private AlertServices      alertServices;
    
    @Inject
    private List<AlertsSender> senders;
    
    // =========================================================================
    // AbstractCrudRest
    // =========================================================================
    @Override
    protected Class<? extends AlertEntity> initType() {
        return AlertEntity.class;
    }
    
    @Override
    protected String parseUid(final String uid) throws DaoException {
        return uid;
    }
    
    @Override
    protected CrudSecurityHandler<AlertEntity> initSecurityHandler() {
        return new CrudSecurityHandlerAdminOnly<AlertEntity>();
    }
    
    @Override
    protected PostCrudHandler<AlertEntity> initHandler() {
        //@formatter:off
        return new PostCrudHandlerBuilder<AlertEntity>()
                    .addOnSave(this::onSave)
                    .addOnDelete(this::onDelete)
                    .addOnDeleteItem(this::onDeleteItem)
                    .addOnRegister(this::onRegister)
                    .build();
        //@formatter:on
    }
    
    // =========================================================================
    // HANDLER
    // =========================================================================
    private void onSave(final List<AlertEntity> entities) {
        SseService.sendAlertsUpdate();
    }
    
    private void onRegister(final List<AlertEntity> entities) {
        onSave(entities);
    }
    
    private void onDelete(final List<String> uids) {
        alertServices.processDisableAlerts(uids, "@all");
    }
    
    private void onDeleteItem(final String uid) {
        final List<String> uids = new ArrayList<>();
        uids.add(uid);
        onDelete(uids);
    }
    
    @Override
    protected AlertEntity secureXssEntity(final AlertEntity entity) {
        //@formatter:off
        SecurityTools.secureJavaScriptAndHtml(entity::getAlerteName, entity::setAlerteName);
        SecurityTools.secureJavaScriptAndHtml(entity::getLevel,      entity::setLevel);
        SecurityTools.secureJavaScriptAndHtml(entity::getLabel,      entity::setLabel);
        SecurityTools.secureJavaScriptAndHtml(entity::getSubLabel,   entity::setSubLabel);
        SecurityTools.secureJavaScriptAndHtml(entity::getChannel,    entity::setChannel);
        //@formatter:off
        
        return entity;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Admin
    @PUT
    @Path("enable/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enable(@PathParam("uid")
    final String uid) throws DaoEntityNotFoundException, DaoException {
        
        final AlertEntity alert = getDao().getByUid(initType(), uid);
        alert.setEnable(true);
        getDao().merge(alert, initType());
        
        return Response.ok().build();
    }
    
    @Admin
    @PUT
    @Path("disable/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response disable(@PathParam("uid")
    final String uid) throws DaoEntityNotFoundException, DaoException {
        
        final AlertEntity alert = getDao().getByUid(initType(), uid);
        alert.setEnable(false);
        getDao().merge(alert, initType());
        
        return Response.ok().build();
    }
    
    
    @Admin
    @GET
    @Path("providers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProviderRestModel> getProviders() {
        //@formatter:off
        return Optional.ofNullable(senders)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(this::mapSenderToProviderRestModel)
                       .collect(Collectors.toList());
        //@formatter:on        
    }
    
    private ProviderRestModel mapSenderToProviderRestModel(final AlertsSender sender) {
        return new ProviderRestModel(AnnotationTools.resolveNamed(sender), sender.enable(), sender.getConfiguration());
    }
}
