package io.inugami.core.context;

import io.inugami.api.ctx.DynamicEventProcessor;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.providers.task.ProviderFutureResult;

public class CoreDynamicEventProcessor implements DynamicEventProcessor {
    
    @Override
    public void process(final GenericEvent event, final ProviderFutureResult providerResult, final String channelName) {
        Context.getInstance().notifyDynamicEventResult(event, providerResult, channelName);
    }
    
}
