package io.inugami.framework.interfaces.monitoring;


import jakarta.servlet.http.HttpServletRequest;

/**
 * The <strong>JavaRestMethodResolver</strong> allows to resolve the Java method handler per HTTP request.
 */
@FunctionalInterface
public interface JavaRestMethodResolver {
    JavaRestMethodDTO resolve(final HttpServletRequest request);
}
