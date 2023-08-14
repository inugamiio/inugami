/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.commons.spring.feature;

import io.inugami.api.feature.FeatureContext;
import io.inugami.api.feature.FeatureInterceptor;
import io.inugami.api.models.tools.Chrono;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

import static io.inugami.commons.spring.feature.FeatureUtils.buildFeatureContext;

@SuppressWarnings({"java:S3358", "java:S112", "java:S1172"})
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Builder
public class FeatureAopAspect {
    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    private final List<FeatureInterceptor> interceptors;


    // ========================================================================
    // INTERCEPTOR
    // ========================================================================
    @Pointcut("@annotation(io.inugami.api.feature.Feature)")
    public void annotatedMethod() {
    }

    @Around("execution(* *(..)) && annotatedMethod()")
    public Object interceptFeatureCalling(final ProceedingJoinPoint jointPoint) throws Throwable {
        Object               result         = null;
        final FeatureContext featureContext = resolveFeatureDTO(jointPoint);
        if (featureContext == null) {
            result = jointPoint.proceed();
        } else {
            result = interceptFeature(jointPoint, featureContext);
        }


        return result;
    }

    private Object interceptFeature(final ProceedingJoinPoint jointPoint,
                                    final FeatureContext featureContext) throws Throwable {
        Object       result = null;
        Throwable    error  = null;
        final Chrono chrono = Chrono.startChrono();
        try {
            assertAllowsToInvoke(featureContext);
            onBegin(featureContext);
            result = jointPoint.proceed();
            chrono.stop();
        } catch (final Throwable e) {
            log.error(e.getMessage(), e);
            error = e;
            if (allowFallback(e, featureContext)) {
                result = fallback(e, featureContext);
                chrono.stop();
            } else {
                chrono.stop();
                throw e;
            }
        } finally {
            result = onDone(result, error, chrono, featureContext);
        }
        return result;
    }


    private void assertAllowsToInvoke(final FeatureContext featureContext) throws Exception {
        for (final FeatureInterceptor interceptor : interceptors) {
            interceptor.assertAllowsToInvoke(featureContext);
        }
    }

    // ========================================================================
    // RESOLVER
    // ========================================================================
    private FeatureContext resolveFeatureDTO(final ProceedingJoinPoint jointPoint) {
        final MethodSignature methodSignature = (MethodSignature) jointPoint.getSignature();
        final Class<?>        bean            = methodSignature.getDeclaringType();
        final Method          method          = methodSignature.getMethod();
        final Object[]        args            = jointPoint.getArgs();
        final Object          instance        = jointPoint.getTarget();

        return buildFeatureContext(method, bean, args, instance);
    }

    // ========================================================================
    // PRIVATE
    // ========================================================================
    private boolean allowFallback(final Throwable error, final FeatureContext featureContext) {
        if (featureContext.getFallback().isEmpty()) {
            return false;
        }
        boolean result = true;
        for (final FeatureInterceptor interceptor : interceptors) {
            result = interceptor.allowFallback(error, featureContext);
            if (!result) {
                break;
            }
        }
        return result;
    }


    private void onBegin(final FeatureContext featureContext) {
        for (final FeatureInterceptor interceptor : interceptors) {
            try {
                interceptor.onBegin(featureContext);
            } catch (final Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private Object onDone(final Object inputResult,
                          final Throwable error,
                          final Chrono chrono,
                          final FeatureContext featureContext) {
        Object result = inputResult;
        for (final FeatureInterceptor interceptor : interceptors) {
            try {
                result = interceptor.onDone(result, error, chrono, featureContext);
            } catch (final Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    private Object fallback(final Throwable error, final FeatureContext featureContext) throws Throwable {
        throw error;
    }


}
