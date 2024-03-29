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
package io.inugami.core.alertings.senders;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.alertings.AlertsSender;
import io.inugami.api.functionnals.ConsumerWithException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.tools.AnnotationTools;
import io.inugami.commons.threads.RunAndCloseService;
import io.inugami.core.context.system.SystemInfosManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
    private static final long serialVersionUID = 8209590133838683500L;

    @Inject
    private List<AlertsSender> senders;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public AlertSenderService() {
    }

    protected AlertSenderService(final List<AlertsSender> senders) {
        this.senders = senders;
    }

    // =========================================================================
    // METHODS SEND
    // =========================================================================
    public synchronized void sendNewAlert(final AlertingResult alert, final List<String> channels) {
        processOnSender(alert, sender -> sender.sendNewAlert(alert, channels));
    }

    public synchronized void send(final AlertingResult alert, final List<String> channels) {
        processOnSender(alert, sender -> sender.send(alert, channels));
    }

    public synchronized void sendDisable(final List<String> uids, final List<String> channels) {
        processOnSender(null, sender -> sender.delete(uids, channels));
    }

    // =========================================================================
    // PROCESS ON SENDERS
    // =========================================================================
    private synchronized void processOnSender(final AlertingResult alert,
                                              final ConsumerWithException<AlertsSender> handler) {

        final List<Callable<Void>> senderTasks = buildTasks(handler, alert);
        //@formatter:off
        new RunAndCloseService<>(AlertSenderService.class.getName(),
                                 SystemInfosManager.TIMEOUT,
                                 senderTasks.size(),
                                 senderTasks,
                                 this::onError
                                 ).run();
        //@formatter:on

    }

    private List<Callable<Void>> buildTasks(final ConsumerWithException<AlertsSender> handler,
                                            final AlertingResult alert) {
        final List<Callable<Void>> result          = new ArrayList<>();
        final List<AlertsSender>   selectedSenders = resolveSenders(alert);
        for (final AlertsSender sender : selectedSenders) {
            result.add(() -> {
                handler.process(sender);
                return null;
            });
        }
        return result;
    }

    protected List<AlertsSender> resolveSenders(final AlertingResult alert) {
        final List<AlertsSender> result = new ArrayList<>();
        if ((alert == null) || (alert.getProviders() == null) || alert.getProviders().isEmpty()) {
            result.addAll(senders);
        } else {
            for (final AlertsSender sender : senders) {
                final String senderName = AnnotationTools.resolveNamed(sender);
                if (alert.getProviders().contains(senderName)) {
                    result.add(sender);
                }
            }
        }
        return result;
    }

    private Void onError(final Exception error, final Callable<Void> task) {
        Loggers.DEBUG.error(error.getMessage(), error);
        Loggers.ALERTS_SENDER.error(error.getMessage());
        return null;
    }
}
