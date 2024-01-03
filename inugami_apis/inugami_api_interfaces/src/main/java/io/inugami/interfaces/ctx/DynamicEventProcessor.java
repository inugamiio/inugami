package io.inugami.interfaces.ctx;

import io.inugami.interfaces.models.event.GenericEvent;
import io.inugami.interfaces.spi.NamedSpi;
import io.inugami.interfaces.task.ProviderFutureResult;

public interface DynamicEventProcessor extends NamedSpi {
    void process(GenericEvent event, ProviderFutureResult providerResult, final String channelName);
}
