package io.inugami.framework.api.test;

import io.inugami.framework.interfaces.spi.SpiPriority;

@SpiPriority(10)
public class MySpiServiceFallback implements MySpiService {
    @Override
    public void process() {

    }
}
