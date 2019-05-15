package org.inugami.core.alertings.senders.opsgenie.sender.model.visibleto;

import flexjson.transformer.Transformer;
import org.inugami.configuration.services.mapping.transformers.AbstractTransformerHelper;

public class TeamVisibleToTransformer extends AbstractTransformerHelper<TeamVisibleTo> implements Transformer {
    @Override
    public void process(TeamVisibleTo value) {
        fieldString("type",() -> value.getType().toString());

        if(value.getId() == null){
            field("name",()->value.getName());
        }else{
            fieldString("id",()-> value.getId());
        }
    }
}
