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
package io.inugami.core.alertings.senders.email;

import static io.inugami.api.tools.ConfigHandlerTools.ENABLE;
import static io.inugami.api.tools.ConfigHandlerTools.grabConfig;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.alertings.AlertsSender;
import io.inugami.api.alertings.AlertsSenderException;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.core.alertings.senders.email.sender.EmailSenderService;
import io.inugami.core.alertings.senders.slack.SlackAlertSender;
import io.inugami.core.context.ApplicationContext;

/**
 * SseAlertingSender
 * 
 * @author patrickguillerm
 * @since 20 janv. 2018
 */
@Named
@ApplicationScoped
public class EmailAlertSender implements AlertsSender, Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long            serialVersionUID = -8163002950831242626L;
    
    @Inject
    private ApplicationContext           context;
    
    @Inject
    private transient EmailSenderService emailSender;
    
    private boolean                      enable;
    
    // =========================================================================
    // INIT
    // =========================================================================
    @PostConstruct
    public void init() {
        final ConfigHandler<String, String> config = context.getGlobalConfiguration().optionnal();
        enable = Boolean.parseBoolean(grabConfig(SlackAlertSender.class, ENABLE, config));
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void send(final AlertingResult alert, final List<String> channels) throws AlertsSenderException {
        // nothing to do
    }
    
    @Override
    public void sendNewAlert(final AlertingResult alert, final List<String> channels) throws AlertsSenderException {
        Asserts.notNull(alert, channels);
    }
    
    @Override
    public void delete(final List<String> uids, final List<String> channels) throws AlertsSenderException {
        Asserts.notNull(uids, channels);
        
    }
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Override
    public boolean enable() {
        return enable;
    }
    
}
