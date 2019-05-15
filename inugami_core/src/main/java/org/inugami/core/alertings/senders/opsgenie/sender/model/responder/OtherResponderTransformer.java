package org.inugami.core.alertings.senders.opsgenie.sender.model.responder;

import flexjson.transformer.Transformer;
import org.inugami.configuration.services.mapping.transformers.AbstractTransformerHelper;
import org.inugami.core.alertings.senders.opsgenie.sender.model.visibleto.TeamVisibleTo;

public class OtherResponderTransformer  extends AbstractTransformerHelper<OtherResponder> implements Transformer {

    @Override
    public void process(OtherResponder value) {
        fieldString("type",()->value.getType().toString());
        if(value.getId() == null){
            field("name",()->value.getName());
        }else{
            fieldString("id",()-> value.getId());
        }
    }
}
