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
package org.inugami.core.alertings.senders.teams;

import static org.inugami.api.tools.ConfigHandlerTools.ENABLE;
import static org.inugami.api.tools.ConfigHandlerTools.grabConfig;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.alertings.AlertsSender;
import org.inugami.api.alertings.AlertsSenderException;
import org.inugami.api.exceptions.RenderingException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.core.alertings.senders.teams.sender.TeamsSender;
import org.inugami.core.alertings.senders.teams.sender.models.TeamsModel;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.services.senders.Sender;
import org.inugami.core.services.senders.SenderException;

@Named
public class TeamsAlertSender implements AlertsSender, Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long        serialVersionUID = -3898826328579344252L;
    
    @Inject
    private ApplicationContext       context;
    
    @Inject
    @TeamsSender
    private Sender<TeamsModel>       sender;
    
    @Default
    @Inject
    private TeamsAlertRenderer       defaultRenderer;
    
    @Inject
    private List<TeamsAlertRenderer> renderers;
    
    private String                   defaultChannel;
    
    private boolean                  enable           = true;
    
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    
    public TeamsAlertSender() {
    }
    
    protected TeamsAlertSender(final ApplicationContext context, final Sender<TeamsModel> sender,
                               final List<TeamsAlertRenderer> renderers, final TeamsAlertRenderer defaultRenderer) {
        this.context = context;
        this.sender = sender;
        this.renderers = renderers;
        this.defaultRenderer = defaultRenderer;
    }
    
    @Inject
    public void init() {
        final ConfigHandler<String, String> config = context.getGlobalConfiguration().optionnal();
        enable = Boolean.parseBoolean(grabConfig(TeamsAlertSender.class, ENABLE, config));
        defaultChannel = grabConfig(TeamsAlertSender.class, "default.channel", config);
        
        if ((defaultChannel == null) || defaultChannel.trim().isEmpty()) {
            Loggers.ALERTING.error(TeamsAlertSender.class.getSimpleName() + " no default channel define!");
            enable = false;
        }
        
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void sendNewAlert(final AlertingResult alert, final List<String> channels) throws AlertsSenderException {
        
        if (alert == null) {
            return;
        }
        
        TeamsModel data = null;
        try {
            data = buildNewAlert(alert);
        }
        catch (final RenderingException e1) {
            Loggers.DEBUG.error(e1.getMessage(), e1);
            throw new AlertsSenderException(e1.getMessage(), e1);
        }
        
        try {
            sender.send(data);
        }
        catch (final SenderException e) {
            Loggers.ALERTING.error("[{}] error on sending message to channel {} : {}",
                                   TeamsAlertSender.class.getSimpleName(), e.getMessage());
        }
        
    }
    
    @Override
    public void send(final AlertingResult alert, final List<String> channels) throws AlertsSenderException {
        
    }
    
    @Override
    public void delete(final List<String> uids, final List<String> channels) throws AlertsSenderException {
        if (uids == null) {
            return;
        }
        
    }
    
    @Override
    public boolean enable() {
        return enable;
    }
    
    @Override
    public Map<String, String> getConfiguration() {
        return sender.getConfiguration();
    }
    
    // =========================================================================
    // BUILD
    // =========================================================================
    protected TeamsModel buildNewAlert(final AlertingResult alert) throws RenderingException {
        final TeamsAlertRenderer renderer = resolveRenderer(alert);
        return renderer.rendering(alert);
        
    }
    
    // =========================================================================
    // RESOLVER
    // =========================================================================
    private TeamsAlertRenderer resolveRenderer(final AlertingResult alert) {
        TeamsAlertRenderer result = null;
        for (final TeamsAlertRenderer renderer : renderers) {
            if ((renderer != defaultRenderer) && renderer.accept(alert)) {
                result = renderer;
                break;
            }
        }
        if (result == null) {
            result = defaultRenderer;
        }
        
        return result;
    }
    
}
