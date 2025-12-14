package io.inugami.framework.api.tools;

import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.interfaces.functionnals.GenericActionWithException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.inugami.framework.api.tools.RunSafeUtils.*;
import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class RunSafeUtilsTest {

    public static final String HELLO = "hello";
    public static final String ERROR = "error";

    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(RunSafeUtils.class);
    }

    @Test
    void runSafeOrElse_nominal() {
        assertThat(runSafeOrElse(() -> HELLO, ERROR)).isEqualTo(HELLO);

        GenericActionWithException<String> action = () -> {
            throw new Exception("sorry");
        };
        assertThat(runSafeOrElse(action, ERROR)).isEqualTo(ERROR);
    }

    @Test
    void runSafe_nominal() {
        assertThat(runSafe(() -> HELLO)).isEqualTo(HELLO);
        GenericActionWithException<String> action = () -> {
            throw new Exception("sorry");
        };
        assertThat(runSafe(action)).isNull();
    }

    @Test
    void runSafeVoid_nominal() {
        final List<String> data = new ArrayList<>();
        runSafeVoid(() -> data.add("OK"));
        runSafeVoid(() -> {
            throw new Exception("sorry");
        });
        assertText(data, """
                [ "OK" ]
                """);
    }
}