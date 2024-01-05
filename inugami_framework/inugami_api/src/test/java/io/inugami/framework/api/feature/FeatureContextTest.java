package io.inugami.framework.api.feature;

import io.inugami.framework.api.tools.ReflectionUtils;
import io.inugami.framework.api.tools.unit.test.UnitTestData;
import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.api.tools.unit.test.dto.AssertDtoContext;
import io.inugami.framework.api.tools.unit.test.dto.UserDataDTO;
import io.inugami.framework.interfaces.feature.Feature;
import io.inugami.framework.interfaces.feature.FeatureContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FeatureContextTest {


    @Test
    void featureContext() {
        UnitTestHelper.assertDto(new AssertDtoContext<FeatureContext>().toBuilder()
                                                                       .objectClass(FeatureContext.class)
                                                                       .fullArgConstructorRefPath("io/inugami/framework/api/featureContext/model.json")
                                                                       .toStringRefPath("io/inugami/framework/api/featureContext/toString.txt")
                                                                       .cloneFunction(instance -> instance.toBuilder()
                                                                                                          .build())
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
        assertThat(instance).isNotEqualTo(instance.toBuilder().featureName(UnitTestData.OTHER).build());
        assertThat(instance.toBuilder().featureName(UnitTestData.OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().featureName(null).build().hashCode());
        assertThat(instance.toBuilder()
                           .featureName(UnitTestData.OTHER)
                           .build()
                           .hashCode()).isNotEqualTo(instance.hashCode());

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
                             .args(new Object[]{UnitTestData.UID})
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