package io.inugami.core.alertings.senders.opsgenie;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.exceptions.RenderingException;
import io.inugami.api.loggers.Loggers;
import io.inugami.core.alertings.senders.SenderRenderer;
import io.inugami.core.alertings.senders.opsgenie.sender.model.OpsgenieModel;
import io.inugami.api.exceptions.Asserts;


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