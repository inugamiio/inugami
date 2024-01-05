package io.inugami.framework.interfaces.alertings;

import io.inugami.framework.interfaces.commons.UnitTestData;
import io.inugami.framework.interfaces.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlerteLevelsTest {

    @Test
    void alerteLevels_enum() {
        UnitTestHelper.assertEnumRelative(AlerteLevels.class,
                                          "io/inugami/framework/interfaces/alertings/alerteLevels_enum.json");
    }

    @Test
    void getLevel_nominal() {
        assertThat(AlerteLevels.FATAL.getLevel()).isEqualTo(1000000);
    }

    @Test
    void getRegex_nominal() {
        assertThat(AlerteLevels.FATAL.getRegex()).hasToString(".*fatal.*");
    }

    @Test
    void getColor_nominal() {
        assertThat(AlerteLevels.FATAL.getColor()).isEqualTo("#980101");
    }

    @Test
    void getAlerteLevel_nominal() {
        assertThat(AlerteLevels.getAlerteLevel("FATAL")).isEqualTo(AlerteLevels.FATAL);
        assertThat(AlerteLevels.getAlerteLevel("fatal")).isEqualTo(AlerteLevels.FATAL);
    }

    @Test
    void getAlerteLevel_withNull() {
        assertThat(AlerteLevels.getAlerteLevel(null)).isEqualTo(AlerteLevels.UNDEFINE);
    }

    @Test
    void getAlerteLevel_other() {
        assertThat(AlerteLevels.getAlerteLevel(UnitTestData.OTHER)).isEqualTo(AlerteLevels.UNDEFINE);
    }

    @Test
    void getLevel_withDouble_nominal() {
        assertThat(AlerteLevels.getLevel(2000000.0)).isEqualTo(AlerteLevels.FATAL);
        assertThat(AlerteLevels.getLevel(1000000.0)).isEqualTo(AlerteLevels.FATAL);
        assertThat(AlerteLevels.getLevel(100000.0)).isEqualTo(AlerteLevels.ERROR);
        assertThat(AlerteLevels.getLevel(10000.0)).isEqualTo(AlerteLevels.WARN);
        assertThat(AlerteLevels.getLevel(1000.0)).isEqualTo(AlerteLevels.INFO);
        assertThat(AlerteLevels.getLevel(100.0)).isEqualTo(AlerteLevels.DEBUG);
        assertThat(AlerteLevels.getLevel(10.0)).isEqualTo(AlerteLevels.TRACE);
        assertThat(AlerteLevels.getLevel(0.0)).isEqualTo(AlerteLevels.OFF);
        assertThat(AlerteLevels.getLevel(-10.0)).isEqualTo(AlerteLevels.UNDEFINE);
        assertThat(AlerteLevels.getLevel(null)).isEqualTo(AlerteLevels.UNDEFINE);
    }
}