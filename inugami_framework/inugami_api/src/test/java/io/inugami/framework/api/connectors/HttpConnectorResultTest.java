package io.inugami.framework.api.connectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.api.tools.unit.test.UnitTestData;
import io.inugami.framework.api.tools.unit.test.dto.AssertDtoContext;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static io.inugami.framework.api.tools.unit.test.UnitTestData.OTHER;
import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class HttpConnectorResultTest {
    @Test
    void httpConnectorResult() {
        assertDto(AssertDtoContext.<HttpConnectorResult>builder()
                                  .objectClass(HttpConnectorResult.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(HttpConnectorResult::new)
                                  .fullArgConstructor(this::buildHttpConnectorResult)
                                  .fullArgConstructorRefPath("io/inugami/framework/api/connectors/httpConnectorResult/model.json")
                                  .getterRefPath("io/inugami/framework/api/connectors/httpConnectorResult/getter.json")
                                  .toStringRefPath("io/inugami/framework/api/connectors/httpConnectorResult/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .checkSetters(true)
                                  .build());
    }

    private void notEquals(final HttpConnectorResult instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().hashHumanReadable(null).build());
        assertThat(instance.toBuilder().hashHumanReadable(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().hashHumanReadable(OTHER).build());
        assertThat(instance.toBuilder().hashHumanReadable(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashHumanReadable(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .hashHumanReadable(OTHER)
                                                             .build()
                                                             .hashCode());

    }

    private HttpConnectorResult buildHttpConnectorResult() {
        return HttpConnectorResult.builder()
                                  .verb("POST")
                                  .url("http://some.mock.url")
                                  .requestData(toJson(UnitTestData.USER_1))
                                  .statusCode(500)
                                  .bodyData(toJson(UnitTestData.USER_1)
                                                    .getBytes(StandardCharsets.UTF_8))
                                  .contentType("application/json")
                                  .responseAt(1764418571272L)
                                  .delay(15L)
                                  .encoding("UTF-8")
                                  .addRequestHeader("source", "main")
                                  .addResponseHeader("warn-1", "some warning")
                                  .build()
                                  .toBuilder()
                                  .build();
    }

    private String toJson(final Object value) {
        try {
            return JsonMarshaller.getInstance().getIndentedObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}