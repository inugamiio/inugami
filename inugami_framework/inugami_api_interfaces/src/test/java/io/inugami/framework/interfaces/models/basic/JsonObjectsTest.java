package io.inugami.framework.interfaces.models.basic;

import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class JsonObjectsTest {
    @Test
    void jsonObjects() {
        assertText(buildJsonObjects(),
                   """
                           {
                             "data" : [ {
                               "value" : "value"
                             } ]
                           }
                           """);
        notEquals(buildJsonObjects());
    }

    private void notEquals(JsonObjects<Json> instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().data(null).build());
        assertThat(instance.toBuilder().data(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().data(List.of()).build());
        assertThat(instance.toBuilder().data(List.of()).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().data(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().data(List.of()).build().hashCode());

    }

    private JsonObjects<Json> buildJsonObjects() {
        return JsonObjects.<Json>builder()
                          .data(List.of(Json.builder()
                                            .value("value")
                                            .build()))
                          .build()
                          .toBuilder()
                          .build();
    }
}