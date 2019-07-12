package org.inugami.core.context;

import org.inugami.api.ctx.DynamicEventProcessor;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.providers.task.ProviderFutureResult;

public class CoreDynamicEventProcessor implements DynamicEventProcessor {
    
    @Override
    public void process(final GenericEvent event, final ProviderFutureResult providerResult, final String channelName) {
        Context.getInstance().notifyDynamicEventResult(event, providerResult, channelName);
    }
    
}
