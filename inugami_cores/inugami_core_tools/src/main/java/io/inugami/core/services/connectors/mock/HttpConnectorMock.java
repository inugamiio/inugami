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
package io.inugami.core.services.connectors.mock;

import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.NotYetImplementedException;
import io.inugami.commons.connectors.HttpConnectorResult;
import io.inugami.commons.connectors.HttpConnectorResultBuilder;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.ProxyBuilder;
import io.inugami.core.services.connectors.HttpConnector;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * HttpConnectorMock
 *
 * @author patrick_guillerm
 * @since 6 sept. 2018
 */
@SuppressWarnings({"java:S5411"})
public class HttpConnectorMock implements MethodInterceptor {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final        List<MockHttpProcessing> resultsGet  = new ArrayList<>();
    private static final int                      STATUS_OK   = 200;
    private static final Pattern                  GET_PATTERN = Pattern.compile("get.*");


    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public HttpConnectorMock() {
    }

    public HttpConnectorMock(final MockHttpProcessing... results) {
        this.resultsGet.addAll(Arrays.asList(results));
    }

    public HttpConnector build() {
        return new ProxyBuilder<HttpConnector>().addSuperClass(HttpConnector.class).addCallback(this).build();
    }

    // =========================================================================
    // ADD
    // =========================================================================
    public HttpConnectorMock addGetResult(final File file) {
        try {
            return addGetResult(u -> true, STATUS_OK, FilesUtils.readBytes(file));
        } catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }

    public HttpConnectorMock addGetResult(final Function<String, Boolean> function, final File file) {
        try {
            return addGetResult(function, STATUS_OK, FilesUtils.readBytes(file));
        } catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }

    public HttpConnectorMock addGetResult(final String content) {
        return addGetResult(u -> true, STATUS_OK, content);
    }

    public HttpConnectorMock addGetResult(final Function<String, Boolean> function, final String content) {
        return addGetResult(function, STATUS_OK, content);
    }

    public HttpConnectorMock addGetResult(final Function<String, Boolean> acceptHandler, final int statusCode,
                                          final String content) {
        resultsGet.add(new MockHttpProcessing(GET_PATTERN, acceptHandler, statusCode, content.getBytes()));
        return this;
    }

    public HttpConnectorMock addGetResult(final Function<String, Boolean> function, final byte[] content) {
        return addGetResult(function, STATUS_OK, content);
    }

    public HttpConnectorMock addGetResult(final Function<String, Boolean> acceptHandler, final int statusCode,
                                          final byte[] content) {
        resultsGet.add(new MockHttpProcessing(GET_PATTERN, acceptHandler, statusCode, content));
        return this;
    }

    // =========================================================================
    // INTERCEPT
    // =========================================================================
    @Override
    public Object intercept(final Object obj, final Method method, final Object[] args,
                            final MethodProxy proxy) throws Throwable {
        Object result = null;

        final String url = extractUrl(method, args);
        if (url != null) {
            if (isGet(method)) {
                result = processGet(url);
            } else {
                throw new NotYetImplementedException();
            }

        }

        return result;
    }

    // =========================================================================
    // PROCESS
    // =========================================================================
    private HttpConnectorResult processGet(final String url) {
        HttpConnectorResult result = null;
        for (final MockHttpProcessing mock : resultsGet) {
            if ((mock.getAcceptHandler() != null) && mock.getAcceptHandler().apply(url)) {
                //@formatter:off
                result =  new HttpConnectorResultBuilder()
                                 .addData(mock.getContent())
                                 .addStatusCode(mock.getStatusCode())
                                 .build();
                //@formatter:on
            }
        }

        return result;
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private String extractUrl(final Method method, final Object[] args) {
        String result = null;
        if ((args != null) && (args.length > 0) && (method.getParameters().length > 0)) {
            //@formatter:off
            final int argIndex = Arrays.asList(Optional.ofNullable(method.getParameters()).orElse(new Parameter[] {}))
                                 .stream()
                                 .map(Parameter::getName)
                                 .map(name->name==null?"undefine":name)
                                 .map(String::toLowerCase)
                                 .collect(Collectors.toList())
                                 .indexOf("url");
            //@formatter:on   
            if ((argIndex != -1) && (argIndex < args.length)) {
                final Object value = args[argIndex];
                if (value instanceof String) {
                    result = (String) value;
                }
            }

        }
        return result;
    }

    private boolean isGet(final Method method) {
        return GET_PATTERN.matcher(method.getName()).matches();
    }

}
