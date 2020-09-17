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
package io.inugami.core.alertings.senders.slack;

import static io.inugami.api.tools.ConfigHandlerTools.ENABLE;
import static io.inugami.api.tools.ConfigHandlerTools.grabConfig;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.alertings.AlertsSender;
import io.inugami.api.alertings.AlertsSenderException;
import io.inugami.api.exceptions.RenderingException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.core.alertings.senders.slack.sender.AbstractSlackModel;
import io.inugami.core.alertings.senders.slack.sender.SlackSender;
import io.inugami.core.context.ApplicationContext;
import io.inugami.core.services.senders.Sender;
import io.inugami.core.services.senders.SenderException;

/**
 * SlackAlertSender
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
@Named
public class SlackAlertSender implements AlertsSender, Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long          serialVersionUID = 710547928932371902L;
    
    @Inject
    private ApplicationContext         context;
    
    @Inject
    @SlackSender
    private Sender<AbstractSlackModel> sender;
    
    @Default
    @Inject
    private SlackAlertRenderer         defaultRenderer;
    
    @Inject
    private List<SlackAlertRenderer>   renderers;
    
    private String                     defaultChannel;
    
    private boolean                    enable           = true;
    
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public SlackAlertSender() {
    }
    
    protected SlackAlertSender(final ApplicationContext context, final Sender<AbstractSlackModel> sender,
                               final List<SlackAlertRenderer> renderers, final SlackAlertRenderer defaultRenderer) {
        this.context = context;
        this.sender = sender;
        this.renderers = renderers;
        this.defaultRenderer = defaultRenderer;
    }
    
    @Inject
    public void init() {
        final ConfigHandler<String, String> config = context.getGlobalConfiguration().optionnal();
        enable = Boolean.parseBoolean(grabConfig(SlackAlertSender.class, ENABLE, config));
        defaultChannel = grabConfig(SlackAlertSender.class, "default.channel", config);
        
        if ((defaultChannel == null) || defaultChannel.trim().isEmpty()) {
            Loggers.ALERTING.error(SlackAlertSender.class.getSimpleName() + " no default channel define!");
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
        
        AbstractSlackModel data = null;
        try {
            data = buildNewAlert(alert);
        }
        catch (final RenderingException e1) {
            Loggers.DEBUG.error(e1.getMessage(), e1);
            throw new AlertsSenderException(e1.getMessage(), e1);
        }
        
        for (final String channel : channels) {
            try {
                final String reelChannel = resolveChannel(channel);
                sender.send(data.clone(reelChannel));
            }
            catch (final SenderException e) {
                if (Loggers.DEBUG.isDebugEnabled()) {
                    Loggers.DEBUG.error(e.getMessage(), e);
                }
                Loggers.ALERTING.error("[{}] error on sending message to channel {} : {}",
                                       SlackAlertSender.class.getSimpleName(), channel, e.getMessage());
            }
        }
    }
    
    private String resolveChannel(final String channel) {
        return "@all".equals(channel) || !channel.startsWith("#") ? defaultChannel : channel;
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
    protected AbstractSlackModel buildNewAlert(final AlertingResult alert) throws RenderingException {
        final SlackAlertRenderer renderer = resolveRenderer(alert);
        return renderer.rendering(alert);
    }
    
    // =========================================================================
    // RESOLVER
    // =========================================================================
    private SlackAlertRenderer resolveRenderer(final AlertingResult alert) {
        SlackAlertRenderer result = null;
        for (final SlackAlertRenderer renderer : renderers) {
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
