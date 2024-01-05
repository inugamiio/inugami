package io.inugami.framework.interfaces.exceptions.asserts;

import io.inugami.framework.interfaces.commons.UnitTestData;
import io.inugami.framework.interfaces.commons.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.MultiUncheckedException;
import io.inugami.framework.interfaces.functionnals.VoidFunctionWithException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class AssertModelTest {


    @Test
    void checkModel_nominal() {
        Assertions.assertThat(Asserts.checkModel(() -> Asserts.assertNotNull(UnitTestData.VALUE))).isEmpty();
        List<VoidFunctionWithException> nullValue = null;
        Assertions.assertThat(Asserts.checkModel(nullValue)).isEmpty();
        Assertions.assertThat(Asserts.checkModel(() -> Asserts.assertNotNull(null))).hasSize(1);
    }

    @Test
    void assertModel_nominal() {
        Asserts.assertModel(() -> Asserts.assertNotNull(UnitTestData.VALUE));
        UnitTestHelper.assertThrows(MultiUncheckedException.class, () -> Asserts.assertModel(() -> Asserts.assertNotNull(null)));

        Asserts.assertModel(List.of(() -> Asserts.assertNotNull(UnitTestData.VALUE)));
        UnitTestHelper.assertThrows(MultiUncheckedException.class, () -> Asserts.assertModel(List.of(() -> Asserts.assertNotNull(null))));
    }
}