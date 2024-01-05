package io.inugami.framework.interfaces.ctx;

import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.spi.NamedSpi;
import io.inugami.framework.interfaces.task.ProviderFutureResult;

public interface DynamicEventProcessor extends NamedSpi {
    void process(GenericEvent event, ProviderFutureResult providerResult, final String channelName);
}
