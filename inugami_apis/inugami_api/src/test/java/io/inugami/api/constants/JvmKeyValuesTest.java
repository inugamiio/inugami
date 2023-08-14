package io.inugami.api.constants;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.inugami.api.constants.JvmKeyValues.*;
import static io.inugami.api.tools.unit.test.UnitTestData.OTHER;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertEnumRelative;
import static org.assertj.core.api.Assertions.assertThat;

class JvmKeyValuesTest {

    @BeforeAll
    static void init() {
        System.setProperty("inugami-home", "/home/inugami");
        System.setProperty("security.users.password", "password");
    }

    @Test
    void jvmKeyValues() {
        assertEnumRelative(JvmKeyValues.class,
                           "api/constants/jvmKeyValues/jvmKeyValues.json");
    }

    @Test
    void getKey_nominal() {
        assertThat(JVM_HOME_PATH.getKey()).isEqualTo("inugami-home");
    }

    @Test
    void get_nominal() {
        assertThat(JVM_HOME_PATH.get()).isEqualTo("/home/inugami");
    }

    @Test
    void getValue_nominal() {
        assertThat(JvmKeyValues.getValue(SECURITY_SQL_INJECT_REGEX, OTHER)).isEqualTo(OTHER);
    }

    @Test
    void or_nominal() {
        assertThat(APPLICATION_MAX_USER_SOCKETS.or(1)).isEqualTo("1");
    }

    @Test
    void or_withSuffix() {
        assertThat(SECURITY_USERS.or("password", OTHER)).isEqualTo("password");
        assertThat(SECURITY_USERS.or("user", OTHER)).isEqualTo(OTHER);
    }
}