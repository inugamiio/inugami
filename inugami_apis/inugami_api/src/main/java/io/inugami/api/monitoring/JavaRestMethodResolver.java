package io.inugami.api.monitoring;

import javax.servlet.http.HttpServletRequest;

/**
 * The <strong>JavaRestMethodResolver</strong> allows to resolve the Java method handler per HTTP request.
 */
@FunctionalInterface
public interface JavaRestMethodResolver {
    JavaRestMethodDTO resolve(final HttpServletRequest request);
}
