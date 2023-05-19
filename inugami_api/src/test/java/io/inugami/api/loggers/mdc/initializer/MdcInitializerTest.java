package io.inugami.api.loggers.mdc.initializer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"java:S2699"})
public class MdcInitializerTest {

    @Disabled
    @Test
    public void initialize_nominal() {
        MdcInitializer.initialize();
    }
}