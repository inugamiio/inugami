package org.inugami.api.ctx;

import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.spi.NamedSpi;

public interface DynamicEventProcessor extends NamedSpi {
    void process(GenericEvent event, ProviderFutureResult providerResult, final String channelName);
}
