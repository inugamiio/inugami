package org.inugami.core.alertings.senders.opsgenie;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.alertings.AlertsSender;
import org.inugami.api.alertings.AlertsSenderException;
import org.inugami.api.exceptions.RenderingException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.core.alertings.senders.opsgenie.sender.OpsgenieSender;
import org.inugami.core.alertings.senders.opsgenie.sender.model.OpsgenieModel;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.services.senders.Sender;
import org.inugami.core.services.senders.SenderException;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static org.inugami.api.tools.ConfigHandlerTools.ENABLE;
import static org.inugami.api.tools.ConfigHandlerTools.grabConfig;

@Named
public class OpsgenieAlertSender implements AlertsSender, Serializable {

    private static final long serialVersionUID = 6930221140869485131L;

    @Inject
    private ApplicationContext context;

    @Inject
    @OpsgenieSender
    private Sender<OpsgenieModel> sender;

    @Default
    @Inject
    private OpsgenieAlertRenderer defaultRenderer;


    private String                   defaultChannel;

    private boolean                  enable           = true;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================


    public OpsgenieAlertSender() {
    }

    protected OpsgenieAlertSender(final ApplicationContext context, final Sender<OpsgenieModel> sender, final OpsgenieAlertRenderer defaultRenderer) {
        this.context = context;
        this.sender = sender;
        this.defaultRenderer = defaultRenderer;
    }


    @Inject
    public void init() {
        final ConfigHandler<String, String> config = context.getGlobalConfiguration().optionnal();
        enable = Boolean.parseBoolean(grabConfig(OpsgenieAlertSender.class, ENABLE, config));
        defaultChannel = grabConfig(OpsgenieAlertSender.class, "default.channel", config);

        if ((defaultChannel == null) || defaultChannel.trim().isEmpty()) {
            Loggers.ALERTING.error(OpsgenieAlertSender.class.getSimpleName() + " no default channel define!");
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

        OpsgenieModel data = null;
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
                    OpsgenieAlertSender.class.getSimpleName(), e.getMessage());
        }

    }

    @Override
    public void send(final AlertingResult alert, final List<String> channels) throws AlertsSenderException {

    }

    @Override
    public void delete(List<String> uids, List<String> channels) throws AlertsSenderException {

    }

    @Override
    public Map<String, String> getConfiguration() {
        return sender.getConfiguration();
    }
    
    @Override
    public boolean enable() {
        return enable;
    }

    // =========================================================================
    // BUILD
    // =========================================================================
    protected OpsgenieModel buildNewAlert(final AlertingResult alert) throws RenderingException {
        return this.defaultRenderer.rendering(alert);

    }

}
