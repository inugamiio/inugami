package io.inugami.api.ctx;

import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.spi.NamedSpi;

public interface DynamicEventProcessor extends NamedSpi {
    void process(GenericEvent event, ProviderFutureResult providerResult, final String channelName);
}
