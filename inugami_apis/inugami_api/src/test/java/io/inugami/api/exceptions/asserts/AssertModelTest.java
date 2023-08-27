package io.inugami.api.exceptions.asserts;

import io.inugami.api.exceptions.MultiUncheckedException;
import io.inugami.api.functionnals.VoidFunctionWithException;
import io.inugami.api.tools.unit.test.UnitTestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.api.exceptions.Asserts.*;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class AssertModelTest {


    @Test
    void checkModel_nominal() {
        assertThat(checkModel(() -> assertNotNull(UnitTestData.VALUE))).isEmpty();
        List<VoidFunctionWithException> nullValue = null;
        assertThat(checkModel(nullValue)).isEmpty();
        assertThat(checkModel(() -> assertNotNull(null))).hasSize(1);
    }

    @Test
    void assertModel_nominal() {
        assertModel(() -> assertNotNull(UnitTestData.VALUE));
        assertThrows(MultiUncheckedException.class, () -> assertModel(() -> assertNotNull(null)));

        assertModel(List.of(() -> assertNotNull(UnitTestData.VALUE)));
        assertThrows(MultiUncheckedException.class, () -> assertModel(List.of(() -> assertNotNull(null))));
    }
}