package io.inugami.api.configurtation;

import org.junit.jupiter.api.Test;

import static io.inugami.api.tools.unit.test.UnitTestHelper.assertUtilityClassLombok;
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