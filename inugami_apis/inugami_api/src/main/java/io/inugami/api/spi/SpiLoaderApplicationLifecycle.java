package io.inugami.api.spi;

import io.inugami.api.listeners.ApplicationLifecycleSPI;

public class SpiLoaderApplicationLifecycle implements ApplicationLifecycleSPI {


    @Override
    public void onApplicationContextInitialized(final Object event) {
        SpiLoaderServiceSPI loader = SpiLoader.getInstance().loadSpiServiceByPriority(SpiLoaderServiceSPI.class, new JavaSpiLoaderServiceSPI());
        SpiLoader.getInstance().reloadLoaderService(loader);
    }
}
