package io.inugami.api.test;

import io.inugami.interfaces.spi.SpiPriority;

@SpiPriority(5)
public class MySpiServiceDefault  implements MySpiService {
    @Override
    public void process() {

    }
}
