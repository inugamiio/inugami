package io.inugami.api.ctx;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BootstrapContextTest {

    @Test
    void nominal() {
        BootstrapContext<String> bootstrapContext = new BootstrapContext<>() {
        };

        bootstrapContext.bootrap("hello");
        bootstrapContext.shutdown("hello");
        assertThat(bootstrapContext).isNotNull();
    }


}