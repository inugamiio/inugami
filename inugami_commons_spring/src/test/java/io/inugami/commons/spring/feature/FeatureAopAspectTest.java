package io.inugami.commons.spring.feature;

import io.inugami.api.exceptions.UncheckedException;
import io.inugami.api.feature.Feature;
import io.inugami.api.feature.FeatureContext;
import io.inugami.api.feature.FeatureInterceptor;
import io.inugami.api.tools.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;
import static io.inugami.commons.test.UnitTestHelper.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureAopAspectTest {
    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    @Mock
    private ProceedingJoinPoint joinPoint;
    @Mock
    private MethodSignature     methodSignature;
    @Mock
    private FeatureInterceptor  featureInterceptor;

    @Captor
    private ArgumentCaptor<FeatureContext> featureContext;

    // ========================================================================
    // INIT
    // ========================================================================
    @BeforeEach
    void init() {
        lenient().when(joinPoint.getSignature()).thenReturn(methodSignature);
        lenient().when(joinPoint.getTarget()).thenReturn(new SimpleService());

        lenient().when(methodSignature.getDeclaringType()).thenReturn(SimpleService.class);

        lenient().when(featureInterceptor.onDone(any(), any(), any(), any())).thenAnswer(answer -> answer.getArgument(0));
    }

    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    @Test
    void interceptFeatureCalling_nominal() throws Throwable {
        when(methodSignature.getMethod()).thenReturn(ReflectionUtils.searchMethod(SimpleService.class, "nominal"));
        when(joinPoint.proceed()).thenReturn(new SimpleService().nominal());
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        final Object result = buildAspect().interceptFeatureCalling(joinPoint);

        assertThat(result).hasToString("nominal feature");
        verify(featureInterceptor).assertAllowsToInvoke(featureContext.capture());
        verify(featureInterceptor).onBegin(featureContext.capture());
        verify(featureInterceptor).onDone(any(), any(), any(), any(FeatureContext.class));

        assertTextRelative(featureContext.getValue(), "common/spring/feature/featureAopAspectTest/interceptFeatureCalling_nominal.json");
    }

    @Test
    void interceptFeatureCalling_nominalWithParameters() throws Throwable {
        when(methodSignature.getMethod()).thenReturn(ReflectionUtils.searchMethod(SimpleService.class, "nominalWithParameters"));
        when(joinPoint.proceed()).thenReturn(new SimpleService().nominalWithParameters());
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        final Object result = buildAspect().interceptFeatureCalling(joinPoint);

        assertThat(result).hasToString("nominal feature");
        verify(featureInterceptor).assertAllowsToInvoke(featureContext.capture());
        verify(featureInterceptor).onBegin(featureContext.capture());
        verify(featureInterceptor).onDone(any(), any(), any(), any(FeatureContext.class));

        assertTextRelative(featureContext.getValue(), "common/spring/feature/featureAopAspectTest/interceptFeatureCalling_nominalWithParameters.json");
    }


    @Test
    void interceptFeatureCalling_withoutAnnotation() throws Throwable {
        when(methodSignature.getMethod()).thenReturn(ReflectionUtils.searchMethod(SimpleService.class, "methodWithoutAnnotation"));
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        buildAspect().interceptFeatureCalling(joinPoint);

        verify(featureInterceptor, never()).onBegin(any());
    }

    @Test
    void interceptFeatureCalling_withError() throws Throwable {
        when(methodSignature.getMethod()).thenReturn(ReflectionUtils.searchMethod(SimpleService.class, "withError"));
        when(joinPoint.getArgs()).thenReturn(new Object[]{});
        when(joinPoint.proceed()).thenThrow(new UncheckedException("some error"));

        assertThrows("some error", () -> buildAspect().interceptFeatureCalling(joinPoint));

        verify(featureInterceptor).assertAllowsToInvoke(featureContext.capture());
        verify(featureInterceptor).onBegin(any(FeatureContext.class));
        verify(featureInterceptor, never()).allowFallback(any(Throwable.class), any(FeatureContext.class));
        verify(featureInterceptor).onDone(any(), any(), any(), any(FeatureContext.class));
    }

    @Test
    void interceptFeatureCalling_withErrorFallback() throws Throwable {
        when(methodSignature.getMethod()).thenReturn(ReflectionUtils.searchMethod(SimpleService.class, "withErrorWithFallback"));
        when(joinPoint.getArgs()).thenReturn(new Object[]{});
        when(joinPoint.proceed()).thenThrow(new UncheckedException("some error"));

        assertThrows("some error", () -> buildAspect().interceptFeatureCalling(joinPoint));

        verify(featureInterceptor).assertAllowsToInvoke(featureContext.capture());
        verify(featureInterceptor).onBegin(any(FeatureContext.class));
        verify(featureInterceptor).allowFallback(any(Throwable.class), any(FeatureContext.class));
        verify(featureInterceptor).onDone(any(), any(), any(), any(FeatureContext.class));
    }


    // ========================================================================
    // TOOLS
    // ========================================================================
    FeatureAopAspect buildAspect() {
        return FeatureAopAspect.builder()
                               .interceptors(List.of(featureInterceptor))
                               .build();
    }


    static class SimpleService {

        public String methodWithoutAnnotation() {
            return "no annotation";
        }

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

        @Feature
        public String withError() {
            throw new UncheckedException("some error");
        }

        @Feature(fallback = "this.fallback")
        public String withErrorWithFallback() {
            throw new UncheckedException("some error");
        }

        public String fallback() {
            return "fallback";
        }

    }
}