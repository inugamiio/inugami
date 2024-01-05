package io.inugami.framework.api.test;

import io.inugami.framework.interfaces.spi.SpiLoader;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"java:S2699"})
class SpiTest {

    @Test
    void test() {
        SpiLoader.getInstance().loadSpiServicesByPriority(MySpiService.class);
    }
}
