package io.inugami.framework.interfaces.connectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.framework.interfaces.exceptions.connector.ConnectorMarshallingException;
import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import io.inugami.framework.interfaces.testing.commons.UnitTestData;
import io.inugami.framework.interfaces.testing.commons.marshaller.JsonMarshaller;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    @Test
    void httpRequest() {
        assertDto(AssertDtoContext.<HttpRequest>builder()
                                  .objectClass(HttpRequest.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(HttpRequest::new)
                                  .fullArgConstructor(this::buildHttpRequest)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/connectors/httpRequest/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/connectors/httpRequest/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/connectors/httpRequest/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .checkSetters(true)
                                  .build());
    }

    private void notEquals(final HttpRequest instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().verb(null).build());
        assertThat(instance.toBuilder().verb(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().verb(OTHER).build());
        assertThat(instance.toBuilder().verb(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().verb(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().verb(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().url(null).build());
        assertThat(instance.toBuilder().url(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().url(OTHER).build());
        assertThat(instance.toBuilder().url(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().url(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().url(OTHER).build().hashCode());
    }

    private HttpRequest buildHttpRequest() {
        return HttpRequest.builder()
                          .verb("GET")
                          .url("http://localhost/user")
                          .addHeader("source", "main")
                          .addOption("page", 1)
                          .token("token")
                          .body(UnitTestData.USER_1)
                          .partner("inugami-partner")
                          .partnerService("USER")
                          .marshaller(this::marshall)
                          .build()
                          .toBuilder()
                          .build();
    }

    private String marshall(Object value) throws ConnectorMarshallingException {
        try {
            return JsonMarshaller.getInstance()
                                 .getIndentedObjectMapper()
                                 .writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new ConnectorMarshallingException(e);
        }
    }
}