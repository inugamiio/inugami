package io.inugami.core.alertings.senders.opsgenie;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.RenderingException;
import io.inugami.api.loggers.Loggers;
import io.inugami.core.alertings.senders.SenderRenderer;
import io.inugami.core.alertings.senders.opsgenie.sender.model.OpsgenieModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;


@Named
@ApplicationScoped
public class OpsgenieAlertRenderer implements SenderRenderer<OpsgenieModel> {

    @Override
    public boolean accept(final AlertingResult alert) {
        return true;
    }

    @Override
    public OpsgenieModel rendering(final AlertingResult alert) throws RenderingException {
        Asserts.assertNotNull(alert);
        Loggers.ALERTING.debug("process alerte for opsgenie : {}", alert.convertToJson());
        final OpsgenieModel result = new OpsgenieModel();
        result.setMessage(alert.getAlerteName());
        result.setDescription(alert.getSubLabel());
        result.setAlias(alert.getMessage());

        return result;
    }

}