package io.inugami.framework.interfaces.models.search;

import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static org.assertj.core.api.Assertions.assertThat;

class SortOrderTest {
    @Test
    void assertEnum() {
        UnitTestHelper.assertEnum(SortOrder.class,
                                  """
                                          {
                                            "ASC" : { },
                                            "DESC" : { }
                                          }
                                          """);
    }

    @Test
    void getEnum_nominal() {
        assertThat(SortOrder.getEnum("ASC")).isEqualTo(SortOrder.ASC);
        assertThat(SortOrder.getEnum("DESC")).isEqualTo(SortOrder.DESC);
        assertThat(SortOrder.getEnum(null)).isEqualTo(SortOrder.ASC);
        assertThat(SortOrder.getEnum(OTHER)).isEqualTo(SortOrder.ASC);

    }
}