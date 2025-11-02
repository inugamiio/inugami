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
package io.inugami.framework.commons.cron;

import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.FatalException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class CronResolver {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final String expression;

    private final CronExpression expr;

    // =================================================================================================================
    // CONSTRUCTORS
    // =================================================================================================================
    public static CronResolver of(final String expression) {
        return new CronResolver(expression);
    }

    public CronResolver(final String expression) {
        super();
        this.expression = expression;

        try {
            expr = new CronExpression(expression);
        } catch (final ParseException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }

    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    public boolean willFire(@NonNull final LocalDateTime date, @NonNull final ZoneOffset offset) {
        final Date currentDate = Date.from(date.toInstant(offset));
        return expr.isSatisfiedBy(currentDate);
    }

    public boolean willFire(final long date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return willFire(calendar);
    }

    public boolean willFire(@NonNull final Calendar date) {
        Asserts.assertNotNull("date is mandatory!", date);
        if (log.isTraceEnabled()) {
            log.trace("check for {} -> {}", expression, format(date));
        }

        return expr.isSatisfiedBy(date.getTime());
    }

    private String format(final Calendar date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(date.getTimeInMillis()));
    }
}
