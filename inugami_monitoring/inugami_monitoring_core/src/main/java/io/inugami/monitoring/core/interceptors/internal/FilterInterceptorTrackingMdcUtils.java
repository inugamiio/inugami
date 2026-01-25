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
package io.inugami.monitoring.core.interceptors.internal;

import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.monitoring.ErrorResult;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodDTO;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodResolver;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.framework.interfaces.rest.RestService;
import io.inugami.monitoring.core.interceptors.ResponseWrapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

import static io.inugami.framework.api.tools.RunSafeUtils.runSafe;
import static io.inugami.framework.api.tools.RunSafeUtils.runSafeVoid;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@Slf4j
@UtilityClass
public class FilterInterceptorTrackingMdcUtils {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String SERVICE_SEPARATOR = "_";

    // =================================================================================================================
    // HEADERS
    // =================================================================================================================
    public static Map<String, List<String>> extractHeaders(final HttpServletRequest httpRequest) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        if (httpRequest == null) {
            return result;
        }
        final List<String> headerNames = new ArrayList<>();
        final Iterator<String> names = Optional.ofNullable(httpRequest.getHeaderNames())
                                               .map(Enumeration::asIterator)
                                               .orElse(new ArrayList<String>().iterator());
        while (names.hasNext()) {
            headerNames.add(names.next());
        }
        Collections.sort(headerNames);
        for (String name : headerNames) {
            final String headerValue = httpRequest.getHeader(name);
            applyIfNotNull(headerValue, value -> result.put(name, List.of(value)));
        }

        return result;
    }

    // =================================================================================================================
    // CORRELATION ID AND TRACE ID
    // =================================================================================================================
    public static void initCorrelationIdAndTraceId(@NonNull final RequestData requestInfo,
                                                   @Nullable final ServletRequest request) {
        MdcService.getInstance()
                  .correlationId(requestInfo.getCorrelationId())
                  .traceId(requestInfo.getTraceId())
                  .requestId();

        HttpServletRequest httpServletRequest = null;
        if (request instanceof HttpServletRequest httpRequest) {
            httpServletRequest = httpRequest;
        }
        final HttpServletRequest currentHttpRequest = httpServletRequest;
        applyIfNotNull(currentHttpRequest, FilterInterceptorTrackingMdcUtils::initMethodAndUri);
    }

    private static void initMethodAndUri(final HttpServletRequest httpServletRequest) {
        runSafeVoid(() -> MdcService.getInstance()
                                    .verb(httpServletRequest.getMethod())
                                    .url(httpServletRequest.getRequestURI()));
    }


    public static void addTrackingInformation(final HttpServletResponse response,
                                              final JavaRestMethodDTO javaRestMethod,
                                              final List<JavaRestMethodTracker> javaRestMethodTrackers) {
        if (javaRestMethod != null) {
            for (final JavaRestMethodTracker tracker : Optional.ofNullable(javaRestMethodTrackers).orElse(List.of())) {
                if (tracker.accept(javaRestMethod)) {
                    runSafeVoid(() -> tracker.track(javaRestMethod));
                }
            }
        }
        final var mdc = MdcService.getInstance();
        response.setHeader(Headers.X_CORRELATION_ID, mdc.correlationId());
        response.setHeader(Headers.X_B_3_TRACEID, mdc.traceId());
    }

    // =================================================================================================================
    // SERVICE NAME
    // =================================================================================================================

    public static String resolveServiceName(final JavaRestMethodDTO javaRestMethod) {
        if (javaRestMethod == null) {
            return null;
        }

        final List<String> values = new ArrayList<>();

        if (javaRestMethod.getRestMethod() != null) {
            final var annotation = javaRestMethod.getRestClass().getAnnotation(RestService.class);
            values.add(Optional.ofNullable(annotation)
                               .map(RestService::value)
                               .orElse(javaRestMethod.getRestClass().getSimpleName()));
        }
        if (javaRestMethod.getRestMethod() != null) {
            final var annotation = javaRestMethod.getRestMethod().getAnnotation(RestService.class);
            values.add(Optional.ofNullable(annotation)
                               .map(RestService::value)
                               .orElse(javaRestMethod.getRestMethod().getName()));
        }

        return String.join(SERVICE_SEPARATOR, values);
    }

    public static JavaRestMethodDTO resolveJavaRestMethod(final ServletRequest request,
                                                          final List<JavaRestMethodResolver> javaRestMethodResolvers) {
        JavaRestMethodDTO        result      = null;
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        for (final JavaRestMethodResolver resolver : Optional.ofNullable(javaRestMethodResolvers).orElse(List.of())) {
            result = runSafe(() -> resolver.resolve(httpRequest), log);
            if (result != null) {
                return result;
            }

        }
        return result;
    }

    // =================================================================================================================
    // LIFECYCLE
    // =================================================================================================================
    public static void onBeginInitMdcFields(final RequestData requestData, final HttpServletRequest httpRequest) {
        if (httpRequest == null) {
            return;
        }

        runSafeVoid(() -> {
            final MdcService mdc = MdcService.getInstance();

            mdc.setMdc(MDCKeys.callType, MdcService.CALL_TYPE_REST);
            mdc.setMdc(MDCKeys.uri, requestData.getUri());
            mdc.setMdc(MDCKeys.verb, httpRequest.getMethod());
            mdc.setMdc(MDCKeys.authProtocol, httpRequest.getAuthType());
            mdc.setMdc(MDCKeys.url, Optional.ofNullable(httpRequest.getRequestURL())
                                            .map(StringBuffer::toString)
                                            .orElse(null));

            applyIfNotNull(httpRequest.getUserPrincipal(),
                           principal -> mdc.setMdc(MDCKeys.principal, httpRequest.getUserPrincipal().getName()));

        }, log);
    }

    public static void onEndInitMdcFields(final ErrorResult error,
                                          final long duration,
                                          final ResponseWrapper httpResponse) {
        final MdcService mdc = MdcService.getInstance();
        runSafeVoid(() -> {
            mdc.duration(duration);
            mdc.setMdc(MDCKeys.httpStatus, httpResponse.getStatus());
            Optional.ofNullable(error)
                    .map(ErrorResult::getCurrentErrorCode)
                    .ifPresent(mdc::errorCode);
        });
    }
}
