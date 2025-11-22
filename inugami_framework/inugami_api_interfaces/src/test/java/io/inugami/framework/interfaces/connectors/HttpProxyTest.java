package io.inugami.framework.interfaces.connectors;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class HttpProxyTest {

    @Test
    void httpProxy() {
        assertDto(AssertDtoContext.<HttpProxy>builder()
                                  .objectClass(HttpProxy.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(HttpProxy::new)
                                  .fullArgConstructor(this::buildHttpProxy)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/connectors/httpProxy/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/connectors/httpProxy/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/connectors/httpProxy/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(HttpProxy instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().host(null).build());
        assertThat(instance.toBuilder().host(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().host(OTHER).build());
        assertThat(instance.toBuilder().host(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().host(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().host(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().port(0).build());
        assertThat(instance.toBuilder().port(0).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().port(0).build());
        assertThat(instance.toBuilder().port(0).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().port(0).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().port(0).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().protocol(null).build());
        assertThat(instance.toBuilder().protocol(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().protocol(OTHER).build());
        assertThat(instance.toBuilder().protocol(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().protocol(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().protocol(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().user(null).build());
        assertThat(instance.toBuilder().user(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().user(OTHER).build());
        assertThat(instance.toBuilder().user(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().user(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().user(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().password(null).build());
        assertThat(instance.toBuilder().password(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().password(OTHER).build());
        assertThat(instance.toBuilder().password(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().password(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().password(OTHER).build().hashCode());
    }

    private HttpProxy buildHttpProxy() {
        return HttpProxy.builder()
                        .host("127.0.1")
                        .port(1598)
                        .user("user")
                        .password("password")
                        .build()
                        .toBuilder()
                        .build();

    }

}