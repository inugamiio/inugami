package io.inugami.framework.interfaces.exceptions.asserts;

import io.inugami.framework.interfaces.commons.UnitTestData;
import io.inugami.framework.interfaces.commons.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.functionnals.IsEmptyFacet;
import io.inugami.framework.interfaces.functionnals.VoidFunctionWithException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"java:S5961"})
class AssertEmptyTest {
    private static final String              MESSAGE          = "some error";
    private static final Supplier<String>    MESSAGE_PRODUCER = () -> MESSAGE;
    private static final String              VALUE            = "VALUE";
    private static final List<String>        LIST             = List.of(VALUE);
    private static final Map<String, String> MAP              = Map.of("key", VALUE);
    private static final List<String>        EMPTY_LIST       = List.of();

    private static final Map<String, String> EMPTY_MAP = Map.of();

    private static final IsEmptyFacet IS_EMPTY     = () -> true;
    private static final IsEmptyFacet IS_NOT_EMPTY = () -> false;
    private static final ErrorCode    ERROR_CODE   = DefaultErrorCode.buildUndefineError();

    private static final IsEmptyFacet NULL_IS_EMPTY_FACET  = null;
    private static final IsEmptyFacet EMPTY_IS_EMPTY_FACET = new IsEmptyFacet() {
        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    private static final IsEmptyFacet NOT_EMPTY_IS_EMPTY_FACET = new IsEmptyFacet() {
        @Override
        public boolean isEmpty() {
            return false;
        }
    };


    @Test
    void checkIsBlank_nominal() {
        Assertions.assertThat(Asserts.checkIsBlank(UnitTestData.OTHER)).isFalse();
        Assertions.assertThat(Asserts.checkIsBlank(UnitTestData.EMPTY)).isTrue();
        Assertions.assertThat(Asserts.checkIsBlank(UnitTestData.EMPTY_NOT_TRIM)).isTrue();
    }

    // =========================================================================
    // assertNotEmpty
    // =========================================================================
    @Test
    void assertNotEmpty_shouldPass() {
        Asserts.assertNotEmpty(VALUE);
        Asserts.assertNotEmpty(MESSAGE, VALUE);
        Asserts.assertNotEmpty(MESSAGE_PRODUCER, VALUE);
        Asserts.assertNotEmpty(ERROR_CODE, VALUE);

        Asserts.assertNotEmpty(LIST);
        Asserts.assertNotEmpty(MESSAGE, LIST);
        Asserts.assertNotEmpty(MESSAGE_PRODUCER, LIST);
        Asserts.assertNotEmpty(ERROR_CODE, LIST);

        Asserts.assertNotEmpty(MAP);
        Asserts.assertNotEmpty(MESSAGE, MAP);
        Asserts.assertNotEmpty(MESSAGE_PRODUCER, MAP);
        Asserts.assertNotEmpty(ERROR_CODE, MAP);

        Asserts.assertNotEmpty(IS_NOT_EMPTY);
        Asserts.assertNotEmpty(MESSAGE, IS_NOT_EMPTY);
        Asserts.assertNotEmpty(MESSAGE_PRODUCER, IS_NOT_EMPTY);
        Asserts.assertNotEmpty(ERROR_CODE, IS_NOT_EMPTY);
    }

    @Test
    void assertNotEmpty__shouldThrow() {
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(UnitTestData.EMPTY));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(MESSAGE, UnitTestData.EMPTY));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(MESSAGE_PRODUCER, UnitTestData.EMPTY));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(ERROR_CODE, UnitTestData.EMPTY));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(UnitTestData.EMPTY));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(MESSAGE, UnitTestData.EMPTY));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(MESSAGE_PRODUCER, UnitTestData.EMPTY_NOT_TRIM));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(ERROR_CODE, UnitTestData.EMPTY_NOT_TRIM));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(EMPTY_LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(MESSAGE, EMPTY_LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(MESSAGE_PRODUCER, EMPTY_LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(ERROR_CODE, EMPTY_LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(EMPTY_MAP));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(MESSAGE, EMPTY_MAP));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(MESSAGE_PRODUCER, EMPTY_MAP));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(ERROR_CODE, EMPTY_MAP));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(IS_EMPTY));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(MESSAGE, IS_EMPTY));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(MESSAGE_PRODUCER, IS_EMPTY));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNotEmpty(ERROR_CODE, IS_EMPTY));
    }

    // =========================================================================
    // assertEmpty
    // =========================================================================
    @Test
    void assertEmpty_shouldPass() {
        Asserts.assertEmpty("");
        Asserts.assertEmpty(MESSAGE, "");
        Asserts.assertEmpty(MESSAGE_PRODUCER, "");
        Asserts.assertEmpty(ERROR_CODE, "");
        Asserts.assertEmpty("    ");
        Asserts.assertEmpty(MESSAGE, "    ");
        Asserts.assertEmpty(MESSAGE_PRODUCER, "    ");
        Asserts.assertEmpty(ERROR_CODE, "    ");

        Asserts.assertEmpty(EMPTY_LIST);
        Asserts.assertEmpty(MESSAGE, EMPTY_LIST);
        Asserts.assertEmpty(MESSAGE_PRODUCER, EMPTY_LIST);
        Asserts.assertEmpty(ERROR_CODE, EMPTY_LIST);

        Asserts.assertEmpty(EMPTY_MAP);
        Asserts.assertEmpty(MESSAGE, EMPTY_MAP);
        Asserts.assertEmpty(MESSAGE_PRODUCER, EMPTY_MAP);
        Asserts.assertEmpty(ERROR_CODE, EMPTY_MAP);

        Asserts.assertEmpty(IS_EMPTY);
        Asserts.assertEmpty(MESSAGE, IS_EMPTY);
        Asserts.assertEmpty(MESSAGE_PRODUCER, IS_EMPTY);
        Asserts.assertEmpty(ERROR_CODE, IS_EMPTY);
    }

    @Test
    void assertEmpty__shouldThrow() {
        final List<VoidFunctionWithException> functions = List.of(
                () -> Asserts.assertEmpty(VALUE),
                () -> Asserts.assertEmpty(MESSAGE, VALUE),
                () -> Asserts.assertEmpty(MESSAGE_PRODUCER, VALUE),
                () -> Asserts.assertEmpty(ERROR_CODE, VALUE),

                () -> Asserts.assertEmpty(LIST),
                () -> Asserts.assertEmpty(MESSAGE, LIST),
                () -> Asserts.assertEmpty(MESSAGE_PRODUCER, LIST),
                () -> Asserts.assertEmpty(ERROR_CODE, LIST),

                () -> Asserts.assertEmpty(MAP),
                () -> Asserts.assertEmpty(MESSAGE, MAP),
                () -> Asserts.assertEmpty(MESSAGE_PRODUCER, MAP),
                () -> Asserts.assertEmpty(ERROR_CODE, MAP),

                () -> Asserts.assertEmpty(IS_NOT_EMPTY),
                () -> Asserts.assertEmpty(MESSAGE, IS_NOT_EMPTY),
                () -> Asserts.assertEmpty(MESSAGE_PRODUCER, IS_NOT_EMPTY),
                () -> Asserts.assertEmpty(ERROR_CODE, IS_NOT_EMPTY)
        );
        for (int i = 0; i < functions.size(); i++) {
            final int index = i;
            Asserts.assertThrow("assertEmpty__shouldThrow  " + i, () -> functions.get(index).process());
        }
    }

    @Test
    void assertNullOrEmpty_nominal() {
        Asserts.assertNullOrEmpty(UnitTestData.NULL_STR);
        Asserts.assertNullOrEmpty(MESSAGE, UnitTestData.NULL_STR);
        Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, UnitTestData.NULL_STR);
        Asserts.assertNullOrEmpty(ERROR_CODE, UnitTestData.NULL_STR);
        Asserts.assertNullOrEmpty(UnitTestData.EMPTY);
        Asserts.assertNullOrEmpty(MESSAGE, UnitTestData.EMPTY);
        Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, UnitTestData.EMPTY);
        Asserts.assertNullOrEmpty(ERROR_CODE, UnitTestData.EMPTY);
        Asserts.assertNullOrEmpty(UnitTestData.EMPTY_NOT_TRIM);
        Asserts.assertNullOrEmpty(MESSAGE, UnitTestData.EMPTY_NOT_TRIM);
        Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, UnitTestData.EMPTY_NOT_TRIM);
        Asserts.assertNullOrEmpty(ERROR_CODE, UnitTestData.EMPTY_NOT_TRIM);
        Asserts.assertNullOrEmpty(EMPTY_LIST);
        Asserts.assertNullOrEmpty(MESSAGE, EMPTY_LIST);
        Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, EMPTY_LIST);
        Asserts.assertNullOrEmpty(ERROR_CODE, EMPTY_LIST);
        Asserts.assertNullOrEmpty(UnitTestData.NULL_LIST);
        Asserts.assertNullOrEmpty(MESSAGE, UnitTestData.NULL_LIST);
        Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, UnitTestData.NULL_LIST);
        Asserts.assertNullOrEmpty(ERROR_CODE, UnitTestData.NULL_LIST);
        Asserts.assertNullOrEmpty(EMPTY_LIST);
        Asserts.assertNullOrEmpty(MESSAGE, EMPTY_LIST);
        Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, EMPTY_LIST);
        Asserts.assertNullOrEmpty(ERROR_CODE, EMPTY_LIST);
        Asserts.assertNullOrEmpty(UnitTestData.NULL_MAP);
        Asserts.assertNullOrEmpty(MESSAGE, UnitTestData.NULL_MAP);
        Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, UnitTestData.NULL_MAP);
        Asserts.assertNullOrEmpty(ERROR_CODE, UnitTestData.NULL_MAP);
        Asserts.assertNullOrEmpty(EMPTY_MAP);
        Asserts.assertNullOrEmpty(MESSAGE, EMPTY_MAP);
        Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, EMPTY_MAP);
        Asserts.assertNullOrEmpty(ERROR_CODE, EMPTY_MAP);

        Asserts.assertNullOrEmpty(NULL_IS_EMPTY_FACET);
        Asserts.assertNullOrEmpty(MESSAGE, NULL_IS_EMPTY_FACET);
        Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, NULL_IS_EMPTY_FACET);
        Asserts.assertNullOrEmpty(ERROR_CODE, NULL_IS_EMPTY_FACET);

        Asserts.assertNullOrEmpty(EMPTY_IS_EMPTY_FACET);
        Asserts.assertNullOrEmpty(MESSAGE, EMPTY_IS_EMPTY_FACET);
        Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, EMPTY_IS_EMPTY_FACET);
        Asserts.assertNullOrEmpty(ERROR_CODE, EMPTY_IS_EMPTY_FACET);


        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(VALUE));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MESSAGE, VALUE));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, VALUE));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(ERROR_CODE, VALUE));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MESSAGE, LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(ERROR_CODE, LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MESSAGE, LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(ERROR_CODE, LIST));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MAP));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MESSAGE, MAP));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, MAP));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(ERROR_CODE, MAP));

        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(NOT_EMPTY_IS_EMPTY_FACET));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MESSAGE, NOT_EMPTY_IS_EMPTY_FACET));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(MESSAGE_PRODUCER, NOT_EMPTY_IS_EMPTY_FACET));
        UnitTestHelper.assertThrows(UncheckedException.class, () -> Asserts.assertNullOrEmpty(ERROR_CODE, NOT_EMPTY_IS_EMPTY_FACET));


    }

}