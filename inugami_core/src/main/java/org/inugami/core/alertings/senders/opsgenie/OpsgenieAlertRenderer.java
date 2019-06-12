package org.inugami.core.alertings.senders.opsgenie;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.exceptions.RenderingException;
import org.inugami.api.loggers.Loggers;
import org.inugami.core.alertings.senders.SenderRenderer;
import org.inugami.core.alertings.senders.opsgenie.sender.model.OpsgenieModel;
import org.inugami.api.exceptions.Asserts;


@Named
@ApplicationScoped
public class OpsgenieAlertRenderer implements SenderRenderer<OpsgenieModel>{

    @Override
    public boolean accept(AlertingResult alert) {
        return true;
    }

    @Override
    public OpsgenieModel rendering(AlertingResult alert) throws RenderingException {
        Asserts.notNull(alert);
        Loggers.ALERTING.debug("process alerte for opsgenie : {}", alert.convertToJson());
        final OpsgenieModel result = new OpsgenieModel();
        result.setMessage(alert.getAlerteName());
        result.setDescription(alert.getSubLabel());
        result.setAlias(alert.getMessage());

        return result;
    }

}