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
package io.inugami.interfaces.dao;

/**
 * DaoEntityNullException
 *
 * @author patrick_guillerm
 * @since 9 janv. 2018
 */
@SuppressWarnings({"java:S110"})
public class DaoEntityNullException extends DaoException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -2850730806091793776L;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DaoEntityNullException() {
        super();
    }

    public DaoEntityNullException(final String message, final Object... values) {
        super(message, values);
    }

    public DaoEntityNullException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DaoEntityNullException(final String message) {
        super(message);
    }

    public DaoEntityNullException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public DaoEntityNullException(final Throwable cause) {
        super(cause);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public static void assertNotNull(final Object values) throws DaoEntityNullException {
        if (values == null) {
            throw new DaoEntityNullException("Object mustn't be null!");
        }
    }

    public static void assertNotNull(final String message, final Object values) throws DaoEntityNullException {
        if (values == null) {
            throw new DaoEntityNullException(message);
        }
    }

}
