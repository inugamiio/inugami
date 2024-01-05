package io.inugami.framework.api.configurtation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.api.tools.unit.test.UnitTestData.LOREM_IPSUM;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultConfigurationSpiTest {

    private static DefaultConfigurationSpi config;

    @BeforeAll
    static void init() {
        System.setProperty("io.inugami.defaultConfigurationSpi.test.str", LOREM_IPSUM);
        System.setProperty("io.inugami.defaultConfigurationSpi.test.boolean", "true");
        System.setProperty("io.inugami.defaultConfigurationSpi.test.int", "1");
        System.setProperty("io.inugami.defaultConfigurationSpi.test.int.err", "err");
        config = new DefaultConfigurationSpi();
    }


    @Test
    void getProperty_nominal() {
        assertThat(config.getProperty(null)).isNull();
        assertThat(config.getProperty("io.inugami.defaultConfigurationSpi.test.str")).isEqualTo(LOREM_IPSUM);
        assertThat(config.getProperty("io.inugami.defaultConfigurationSpi.test.str.other", LOREM_IPSUM)).isEqualTo(LOREM_IPSUM);
    }

    @Test
    void getBooleanProperty_nominal() {
        assertThat(config.getBooleanProperty(null, true)).isTrue();
        assertThat(config.getBooleanProperty("io.inugami.defaultConfigurationSpi.test.boolean")).isTrue();
        assertThat(config.getBooleanProperty("io.inugami.defaultConfigurationSpi.test.boolean.other", true)).isTrue();
    }

    @Test
    void getIntProperty_nominal() {
        assertThat(config.getIntProperty("io.inugami.defaultConfigurationSpi.test.int")).isOne();
        assertThat(config.getIntProperty("io.inugami.defaultConfigurationSpi.test.int.other", 1)).isOne();
        assertThat(config.getIntProperty("io.inugami.defaultConfigurationSpi.test.int.err", 1)).isOne();
    }

    @Test
    void getLongProperty_nominal() {
        assertThat(config.getLongProperty("io.inugami.defaultConfigurationSpi.test.int")).isOne();
        assertThat(config.getLongProperty("io.inugami.defaultConfigurationSpi.test.int.other", 1L)).isOne();
        assertThat(config.getLongProperty("io.inugami.defaultConfigurationSpi.test.int.err", 1)).isOne();
    }

    @Test
    void getDoubleProperty_nominal() {
        assertThat(config.getDoubleProperty("io.inugami.defaultConfigurationSpi.test.int")).isOne();
        assertThat(config.getDoubleProperty("io.inugami.defaultConfigurationSpi.test.int.other", 1.0)).isOne();
        assertThat(config.getDoubleProperty("io.inugami.defaultConfigurationSpi.test.int.err", 1.0)).isOne();
    }
}