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
package io.inugami.framework.commons.files;

import io.inugami.framework.interfaces.exceptions.TechnicalException;

public class FileUtilsException extends TechnicalException {

    private static final long serialVersionUID = 2994314231451872696L;

    public FileUtilsException(final String message, final Object... values) {
        super(message, values);
    }

    public FileUtilsException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
