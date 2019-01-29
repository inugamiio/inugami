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
package org.inugami.core.alertings.senders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.alertings.AlertsSender;
import org.inugami.api.functionnals.ConsumerWithException;
import org.inugami.api.loggers.Loggers;
import org.inugami.commons.threads.RunAndCloseService;
import org.inugami.core.context.system.SystemInfosManager;

/**
 * AlertSenderService
 * 
 * @author patrickguillerm
 * @since 20 janv. 2018
 */
@Named
@ApplicationScoped
public class AlertSenderService implements Serializable {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long  serialVersionUID = 8209590133838683500L;
    
    @Inject
    private List<AlertsSender> senders;
    
    // =========================================================================
    // METHODS SEND
    // =========================================================================
    public synchronized void sendNewAlert(final AlertingResult alert, final List<String> channels) {
        processOnSender("sendNewAlert", (sender) -> sender.sendNewAlert(alert, channels));
    }
    
    public synchronized void send(final AlertingResult alert, final List<String> channels) {
        processOnSender("send", (sender) -> sender.send(alert, channels));
    }
    
    public synchronized void sendDisable(final List<String> uids, final List<String> channels) {
        processOnSender("sendDisable", (sender) -> sender.delete(uids, channels));
    }
    
    // =========================================================================
    // PROCESS ON SENDERS
    // =========================================================================
    private synchronized void processOnSender(final String serviceName,
                                              final ConsumerWithException<AlertsSender> handler) {
        
        final List<Callable<Void>> senderTasks = buildTasks(handler);
        //@formatter:off
        new RunAndCloseService<>(AlertSenderService.class.getName(),
                                 SystemInfosManager.TIMEOUT,
                                 senderTasks.size(),
                                 senderTasks,
                                 this::onError
                                 ).run();
        //@formatter:on
        
    }
    
    private List<Callable<Void>> buildTasks(final ConsumerWithException<AlertsSender> handler) {
        final List<Callable<Void>> result = new ArrayList<>();
        
        for (final AlertsSender sender : senders) {
            result.add(() -> {
                handler.process(sender);
                return null;
            });
        }
        return result;
    }
    
    private Void onError(final Exception error, final Callable<Void> task) {
        Loggers.DEBUG.error(error.getMessage(), error);
        Loggers.ALERTS_SENDER.error(error.getMessage());
        return null;
    }
}
