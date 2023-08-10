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
package io.inugami.data.commons.exceptions;

import io.inugami.api.dao.DaoException;
import io.inugami.api.dao.Identifiable;
import io.inugami.api.models.JsonBuilder;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 * DaoValidatorException
 *
 * @author patrick_guillerm
 * @since 18 janv. 2018
 */
@SuppressWarnings({"java:S110", "java:S119"})
public class DaoValidatorException extends DaoException {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 8109347157694140285L;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DaoValidatorException() {
        super();
    }

    public DaoValidatorException(final String message, final Object... values) {
        super(message, values);
    }

    public DaoValidatorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DaoValidatorException(final String message) {
        super(message);
    }

    public DaoValidatorException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
    }

    public DaoValidatorException(final Throwable cause) {
        super(cause);
    }

    public <E extends Identifiable<PK>, PK extends Serializable> DaoValidatorException(final Set<ConstraintViolation<E>> errors) {
        super(buildMessage(errors));
    }

    public static <E extends Identifiable<PK>, PK extends Serializable> String buildMessage(final Set<ConstraintViolation<E>> errors) {
        final JsonBuilder msg = new JsonBuilder();
        msg.openList();

        final Iterator<ConstraintViolation<E>> iterator = errors.iterator();
        ConstraintViolation<E>                 error;
        while (iterator.hasNext()) {
            error = iterator.next();
            msg.openObject();
            msg.addField(String.valueOf(error.getPropertyPath()));
            msg.valueQuot(error.getMessage());
            msg.closeObject();
            if (iterator.hasNext()) {
                msg.addSeparator();
            }
        }

        msg.closeList();
        return msg.toString();

    }

}
