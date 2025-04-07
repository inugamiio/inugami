package io.inugami.framework.commons.spring.feature.interceptors;

import io.inugami.framework.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.framework.api.tools.ReflectionUtils;
import io.inugami.framework.interfaces.feature.Feature;
import io.inugami.framework.interfaces.feature.FeatureContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.ConfigurableEnvironment;

import static io.inugami.commons.test.UnitTestHelper.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropertyFeatureInterceptorTest {
    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    @Mock
    private ConfigurableEnvironment resolver;

    @BeforeEach
    void clean() {
        new DefaultApplicationLifecycleSPI().onContextRefreshed(null);
    }

    // ========================================================================
    // TEST
    // ========================================================================
    @Test
    void assertAllowsToInvoke_nominal() {
        when(resolver.getProperty("nominal.enabled")).thenReturn("true");

        final PropertyFeatureInterceptor interceptor = buildInterceptor();

        final FeatureContext context = FeatureContext.builder()
                                                     .bean(SimpleService.class)
                                                     .method(ReflectionUtils.searchMethod(SimpleService.class, "nominal"))
                                                     .build();
        interceptor.assertAllowsToInvoke(context);
        interceptor.assertAllowsToInvoke(context);

        assertThat(PropertyFeatureInterceptor.getCache().get("nominal.enabled")).isTrue();
        verify(resolver).getProperty("nominal.enabled");

        new DefaultApplicationLifecycleSPI().onContextRefreshed(null);

        assertThat(PropertyFeatureInterceptor.getCache()).isEmpty();
    }

    @Test
    void assertAllowsToInvoke_nominal_withPropertyAsFalse() {
        when(resolver.getProperty("nominal.enabled")).thenReturn("false");

        assertThrows(PropertyFeatureInterceptor.ERROR_CODE,
                     () -> buildInterceptor().assertAllowsToInvoke(FeatureContext.builder()
                                                                                 .bean(SimpleService.class)
                                                                                 .method(ReflectionUtils.searchMethod(SimpleService.class, "nominal"))
                                                                                 .build()),
                     "common/spring/feature/interceptors/propertyFeatureInterceptorTest/assertAllowsToInvoke_nominal_withPropertyAsFalse.json");
        assertThat(PropertyFeatureInterceptor.getCache().get("nominal.enabled")).isFalse();
    }

    @Test
    void assertAllowsToInvoke_disabledByDefault() {
        assertThrows(PropertyFeatureInterceptor.ERROR_CODE,
                     () -> buildInterceptor().assertAllowsToInvoke(FeatureContext.builder()
                                                                                 .bean(SimpleService.class)
                                                                                 .method(ReflectionUtils.searchMethod(SimpleService.class, "disabledByDefault"))
                                                                                 .build()),
                     "common/spring/feature/interceptors/propertyFeatureInterceptorTest/assertAllowsToInvoke_disabledByDefault.json");
        assertThat(PropertyFeatureInterceptor.getCache().get("disabledByDefault.enabled")).isFalse();
    }

    // ========================================================================
    // TOOLS
    // ========================================================================
    PropertyFeatureInterceptor buildInterceptor() {
        final PropertyFeatureInterceptor result = PropertyFeatureInterceptor.builder()
                                                                            .resolver(resolver)
                                                                            .build();
        result.init();
        return result;
    }

    static class SimpleService {
        @Feature
        public String nominal() {
            return "nominal feature";
        }

        @Feature(
                value = "withParams",
                enabledByDefault = true,
                propertyPrefix = "io.inugami",
                fallback = "this.fallback"
        )
        public String nominalWithParameters() {
            return "nominal feature";
        }

        @Feature(enabledByDefault = false)
        public String disabledByDefault() {
            return "disabledByDefault";
        }


    }
}