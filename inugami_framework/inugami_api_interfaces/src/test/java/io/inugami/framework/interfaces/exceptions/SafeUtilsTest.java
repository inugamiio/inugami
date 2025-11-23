package io.inugami.framework.interfaces.exceptions;

import io.inugami.framework.interfaces.functionnals.SupplierWithThrowable;
import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.inugami.framework.interfaces.exceptions.SafeUtils.grabSafe;
import static io.inugami.framework.interfaces.exceptions.SafeUtils.processSafe;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class SafeUtilsTest {

    @Test
    void utilityClass() {
        UnitTestHelper.assertUtilityClassLombok(SafeUtils.class);
    }

    @Test
    void grabSafe_nominal() {
        assertThat(grabSafe(() -> "nominal", "defaultValue")).isEqualTo("nominal");

        SupplierWithThrowable<String> handler = () -> {
            throw new UncheckedException("sorry");
        };
        assertThat(grabSafe(handler, "defaultValue")).isEqualTo("defaultValue");
    }

    @Test
    void processSafe_nominal() {
        final List<String> values = new ArrayList<>();
        processSafe(() -> values.add("nominal"));
        processSafe(() -> {
            new UncheckedException("sorry");
        });
        assertText(values,
                   """
                           [ "nominal" ]
                           """);
    }
}