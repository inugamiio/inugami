package io.inugami.api.test;

import io.inugami.api.spi.SpiPriority;

@SpiPriority(10)
public class MySpiServiceFallback implements MySpiService {
    @Override
    public void process() {

    }
}
