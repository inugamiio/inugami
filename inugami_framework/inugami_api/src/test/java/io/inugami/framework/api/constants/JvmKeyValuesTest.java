package io.inugami.framework.api.constants;

import io.inugami.framework.api.tools.unit.test.UnitTestData;
import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JvmKeyValuesTest {

    @BeforeAll
    static void init() {
        System.setProperty("inugami-home", "/home/inugami");
        System.setProperty("security.users.password", "password");
    }

    @Test
    void jvmKeyValues() {
        UnitTestHelper.assertEnumRelative(JvmKeyValues.class,
                                          "io/inugami/framework/api/jvmKeyValues/jvmKeyValues.json");
    }

    @Test
    void getKey_nominal() {
        Assertions.assertThat(JvmKeyValues.JVM_HOME_PATH.getKey()).isEqualTo("inugami-home");
    }

    @Test
    void get_nominal() {
        Assertions.assertThat(JvmKeyValues.JVM_HOME_PATH.get()).isEqualTo("/home/inugami");
    }

    @Test
    void getValue_nominal() {
        assertThat(JvmKeyValues.getValue(JvmKeyValues.SECURITY_SQL_INJECT_REGEX, UnitTestData.OTHER)).isEqualTo(UnitTestData.OTHER);
    }

    @Test
    void or_nominal() {
        Assertions.assertThat(JvmKeyValues.APPLICATION_MAX_USER_SOCKETS.or(1)).isEqualTo("1");
    }

    @Test
    void or_withSuffix() {
        Assertions.assertThat(JvmKeyValues.SECURITY_USERS.or("password", UnitTestData.OTHER)).isEqualTo("password");
        Assertions.assertThat(JvmKeyValues.SECURITY_USERS.or("user", UnitTestData.OTHER)).isEqualTo(UnitTestData.OTHER);
    }
}