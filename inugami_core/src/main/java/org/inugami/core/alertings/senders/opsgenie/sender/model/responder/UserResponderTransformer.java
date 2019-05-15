package org.inugami.core.alertings.senders.opsgenie.sender.model.responder;

import flexjson.transformer.Transformer;
import org.inugami.configuration.services.mapping.transformers.AbstractTransformerHelper;

public class UserResponderTransformer extends AbstractTransformerHelper<UserResponder> implements Transformer {
    @Override
    public void process(UserResponder value) {
        fieldString("type",()->value.getType().toString());
        if(value.getId() == null){
            field("name",()->value.getUsername());
        }else{
            fieldString("id",()-> value.getId());
        }
    }
}
