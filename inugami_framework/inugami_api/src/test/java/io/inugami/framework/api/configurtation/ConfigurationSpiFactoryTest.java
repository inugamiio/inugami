package io.inugami.framework.api.configurtation;

import io.inugami.framework.interfaces.configurtation.ConfigurationSpi;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertUtilityClassLombok;
import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationSpiFactoryTest {

    @Test
    void assertUtilityClass() {
        assertUtilityClassLombok(ConfigurationSpiFactory.class);
    }

    @Test
    void INSTANCE_nominal() {
        final ConfigurationSpi result = ConfigurationSpiFactory.INSTANCE;
        assertThat(result).isNotNull();
    }

}