package io.inugami.api.test;

import io.inugami.api.spi.SpiLoader;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"java:S2699"})
class SpiTest {

    @Test
    void test() {
        SpiLoader.getInstance().loadSpiServicesByPriority(MySpiService.class);
    }
}
