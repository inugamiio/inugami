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
package io.inugami.configuration.services.functions;

import io.inugami.api.exceptions.FatalException;

/**
 * ProviderAttributFunctionException
 *
 * @author patrick_guillerm
 * @since 20 sept. 2017
 */
@SuppressWarnings({"java:S110"})
public class ProviderAttributFunctionException extends FatalException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -8555607978261054581L;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderAttributFunctionException() {
        super();
    }

    public ProviderAttributFunctionException(final String message, final Object... values) {
        super(message, values);
    }

    public ProviderAttributFunctionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ProviderAttributFunctionException(final String message) {
        super(message);
    }

    public ProviderAttributFunctionException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public ProviderAttributFunctionException(final Throwable cause) {
        super(cause);
    }

    public static void assertNotNull(final FunctionData data) {
        assertNotNull("function data mustn't be null!", data);
    }

    public static void assertNotNull(final String message, final Object data) {
        if (data == null) {
            throw new ProviderAttributFunctionException(message);
        }
    }

    public static void assertParams(final int nbItems, final FunctionData data) {
        assertNotNull(data);
        if (nbItems != data.getParameters().length) {
            throw new ProviderAttributFunctionException("Invalid number parameters");
        }
    }

    public static void assertParams(final int minParam, final int maxParam, final FunctionData data) {
        assertNotNull(data);
        if ((data.getParameters().length < minParam) || (data.getParameters().length > maxParam)) {
            throw new ProviderAttributFunctionException("Invalid number parameters");
        }
    }
}
