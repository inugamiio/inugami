package io.inugami.api.test;

import io.inugami.api.spi.SpiPriority;

@SpiPriority(5)
public class MySpiServiceDefault  implements MySpiService {
    @Override
    public void process() {

    }
}
