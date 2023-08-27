package io.inugami.api.feature;

import io.inugami.api.tools.ReflectionUtils;
import io.inugami.api.tools.unit.test.dto.AssertDtoContext;
import io.inugami.api.tools.unit.test.dto.UserDataDTO;
import org.junit.jupiter.api.Test;

import static io.inugami.api.tools.unit.test.UnitTestData.OTHER;
import static io.inugami.api.tools.unit.test.UnitTestData.UID;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class FeatureContextTest {


    @Test
    void featureContext() {
        assertDto(new AssertDtoContext<FeatureContext>().toBuilder()
                                                        .objectClass(FeatureContext.class)
                                                        .fullArgConstructorRefPath("api/feature/featureContext/model.json")
                                                        .toStringRefPath("api/feature/featureContext/toString.txt")
                                                        .cloneFunction(instance -> instance.toBuilder().build())
                                                        .noArgConstructor(FeatureContext::new)
                                                        .fullArgConstructor(this::buildDataSet)
                                                        .noEqualsFunction(this::notEquals)
                                                        .checkSetters(false)
                                                        .checkSerialization(false)
                                                        .build());
    }

    void notEquals(final FeatureContext instance) {
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().featureName(null).build());
        assertThat(instance.toBuilder().featureName(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().featureName(OTHER).build());
        assertThat(instance.toBuilder().featureName(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().featureName(null).build().hashCode());
        assertThat(instance.toBuilder().featureName(OTHER).build().hashCode()).isNotEqualTo(instance.hashCode());

    }

    private FeatureContext buildDataSet() {
        return FeatureContext.builder()
                             .featureName("search")
                             .enabledByDefault(true)
                             .propertyPrefix("io.inungami.feature.featureContext")
                             .fallback("searchFallback")
                             .monitored(true)
                             .bean(Service.class)
                             .method(ReflectionUtils.searchMethodByName(Service.class, "search"))
                             .args(new Object[]{UID})
                             .instance(new Service())
                             .build();
    }

    private static class Service {

        @Feature
        public UserDataDTO search(final String uid) {
            return null;
        }
    }
}