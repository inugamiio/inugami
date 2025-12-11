package io.inugami.framework.api.tools;

import org.junit.jupiter.api.Test;

import static io.inugami.framework.api.tools.PortGenerator.MAX_PORT;
import static io.inugami.framework.api.tools.PortGenerator.MIN_PORT;
import static org.assertj.core.api.Assertions.assertThat;

class PortGeneratorTest {

    @Test
    void generateFor_nominal() {
        final String service = "springbootIntegrationTest";
        final int    port    = PortGenerator.generateFor(service);
        assertThat(port).isGreaterThanOrEqualTo(MIN_PORT);
        assertThat(port).isLessThanOrEqualTo(MAX_PORT);

        assertThat(PortGenerator.generateFor(service)).isEqualTo(port);
    }

}