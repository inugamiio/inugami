package io.inugami.api.marshalling;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultModuleRegisterSpiTest {

    @Test
    void extractModules_nominal() {
        assertThat(new DefaultModuleRegisterSpi().extractModules()).isEmpty();
    }
}