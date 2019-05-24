package org.inugami.core.alertings.senders.opsgenie;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.inugami.api.alertings.AlerteLevels;
import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.exceptions.RenderingException;
import org.inugami.api.loggers.Loggers;
import org.inugami.core.alertings.senders.SenderRenderer;
import org.inugami.core.alertings.senders.opsgenie.sender.model.OpsgenieModel;
import org.inugami.api.exceptions.Asserts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        result.setMessage(alert.getMessage());
        result.setAlias(alert.getAlerteName());
        result.setDescription(alert.getSubLabel());

        AlerteLevels level = alert.getLevelType();

        String resultPriority;
        switch(level){
            case FATAL:     resultPriority          =   "P1";
                                                    break;
            case ERROR:     resultPriority          =   "P2";
                                                    break;
            case WARN:      resultPriority           =   "P3";
                                                    break;
            case INFO:      resultPriority           =   "P4";
                                                    break;
            case TRACE:     resultPriority          =   "P5";
                                                    break;
            case DEBUG:     resultPriority          =   "P5";
                break;
            case UNDEFINE:  resultPriority          =   "P5";
                                                    break;
            default:        resultPriority          =   "P5";

        }
        result.setPriority(resultPriority);

        ArrayList<String> tags = new ArrayList<String>();
        for(Tag tag : alert.getTags()){
            tags.add(tag.getName());
        }
        result.setTags(tags);

        //tags a aller chercjer avec extract ??

        //details a mettre le reste

        //entity je met qq chose ?
        //responders je fait quoi ??
        //visible to ???
    }

    /*
    =========================================================================
    BUILDER
    =========================================================================
    */

    private Map<String, Object> extractValues(final Object data, final String... keys) {
        final Map<String, Object> buffer = new HashMap<>();

        if (data != null) {
            //@formatter:off
            if (data instanceof Map<?, ?>) {
                final Map<?, ?> values = (Map<?, ?>) data;
                for(final String key:keys){
                    Optional.ofNullable(values.get(key)).ifPresent(item -> buffer.put(key,item));
                }

            }
            //@formatter:on
        }

        return buffer;
    }
}