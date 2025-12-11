package io.inugami.framework.interfaces.connectors;

import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectorConstantsTest {
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(ConnectorConstants.class);
    }

    @Test
    void CURRENT_APPLICATION_NAME_nominal() {
        assertThat(ConnectorConstants.CURRENT_APPLICATION_NAME).isEqualTo("inugami");
    }

    @Test
    void HEADER_APPLICATION_NAME_nominal() {
        assertThat(ConnectorConstants.HEADER_APPLICATION_NAME).isEqualTo("application-name");
    }

    @Test
    void APPLICATION_HOSTNAME_HEADER_nominal() {
        assertThat(ConnectorConstants.APPLICATION_HOSTNAME_HEADER).isEqualTo("application-host");
    }
}