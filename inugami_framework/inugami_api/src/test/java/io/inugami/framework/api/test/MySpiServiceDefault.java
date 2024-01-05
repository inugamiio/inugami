package io.inugami.framework.api.test;

import io.inugami.framework.interfaces.spi.SpiPriority;

@SpiPriority(5)
public class MySpiServiceDefault  implements MySpiService {
    @Override
    public void process() {

    }
}
