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
package io.inugami.monitoring.springboot.request;

import io.inugami.framework.interfaces.monitoring.JavaRestMethodDTO;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodResolver;
import io.inugami.framework.interfaces.spi.SpiLoader;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ServletRequestPathUtils;
import org.springframework.web.util.WebUtils;

import java.util.List;

@SuppressWarnings({"java:S1172"})
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringRestMethodResolver implements JavaRestMethodResolver {
    public static final String               PATH_ATTRIBUTE = ServletRequestPathUtils.class.getName() + ".PATH";
    private final       List<HandlerMapping> handlerMappings;
    private final       MultipartResolver    multipartResolver;

    public SpringRestMethodResolver() {
        handlerMappings = SpiLoader.getInstance().loadSpiServicesByPriority(HandlerMapping.class);
        multipartResolver = SpiLoader.getInstance().loadSpiSingleServicesByPriority(MultipartResolver.class);
    }

    public JavaRestMethodDTO resolve(final HttpServletRequest request) {

        log.info("TODO");

        final HandlerExecutionChain handler = resolveHandlerMethod(request);
        return handler == null ? null : buildJavaRestResolver(handler, request);
    }

    private JavaRestMethodDTO buildJavaRestResolver(final HandlerExecutionChain resolver,
                                                    final HttpServletRequest request) {


        final HandlerMethod currentHandler = resolveHandler(resolver);
        if (currentHandler == null) {
            return null;
        }
        return JavaRestMethodDTO.builder()
                                .restClass(currentHandler.getBeanType())
                                .restMethod(currentHandler.getMethod())
                                .build();
    }

    private HandlerMethod resolveHandler(final HandlerExecutionChain resolver) {
        final Object handler = resolver.getHandler();
        return handler instanceof HandlerMethod ? (HandlerMethod) handler : null;
    }


    private HandlerExecutionChain resolveHandlerMethod(final HttpServletRequest request) {
        HttpServletRequest processedRequest = checkMultipart(request);
        processedRequest.setAttribute(PATH_ATTRIBUTE, new SpringDefaultRequestPath(request.getRequestURI(), request.getContextPath()));

        try {
            return getHandler(processedRequest);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings({"java:S3776"})
    private HttpServletRequest checkMultipart(final HttpServletRequest request) {
        if (this.multipartResolver == null || !this.multipartResolver.isMultipart(request)) {
            return request;
        }

        if (WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class) != null) {
            if (DispatcherType.REQUEST.equals(request.getDispatcherType())) {
                log.trace("Request already resolved to MultipartHttpServletRequest, e.g. by MultipartFilter");
            }
        } else if (hasMultipartException(request)) {
            log.debug("Multipart resolution previously failed for current request - " +
                      "skipping re-resolution for undisturbed error rendering");
        } else {
            try {
                return this.multipartResolver.resolveMultipart(request);
            } catch (MultipartException ex) {
                if (request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) != null) {
                    log.debug("Multipart resolution failed for error dispatch", ex);
                } else {
                    throw ex;
                }
            }
        }

        return request;
    }

    private boolean hasMultipartException(HttpServletRequest request) {
        Throwable error = (Throwable) request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE);
        while (error != null) {
            if (error instanceof MultipartException) {
                return true;
            }
            error = error.getCause();
        }
        return false;
    }

    private HandlerExecutionChain getHandler(final HttpServletRequest processedRequest) throws Exception {
        if (this.handlerMappings != null) {
            for (HandlerMapping mapping : this.handlerMappings) {
                HandlerExecutionChain handler = mapping.getHandler(processedRequest);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

}
