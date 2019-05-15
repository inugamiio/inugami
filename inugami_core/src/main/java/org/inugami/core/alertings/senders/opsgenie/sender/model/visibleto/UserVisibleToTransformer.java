package org.inugami.core.alertings.senders.opsgenie.sender.model.visibleto;

import flexjson.transformer.Transformer;
import org.inugami.configuration.services.mapping.transformers.AbstractTransformerHelper;

public class UserVisibleToTransformer extends AbstractTransformerHelper<UserVisibleTo> implements Transformer {

    @Override
    public void process(UserVisibleTo value)  {
        fieldString("type",() -> value.getType().toString());

        if(value.getId() == null){
            field("username",()->value.getUsername());
        }else{
            fieldString("id",()-> value.getId());
        }
    }
}
