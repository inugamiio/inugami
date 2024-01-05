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
package io.inugami.framework.interfaces.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

/**
 * CalendarTools
 *
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CalendarTools {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone(Optional.ofNullable(System.getProperty("user.timezone"))
                                                                           .orElse("Europe/Paris"));

    // =========================================================================
    // METHODS
    // =========================================================================
    public static Calendar buildCalendar() {
        return Calendar.getInstance(TIME_ZONE);
    }

    public static Calendar buildCalendarBySecond() {
        final Calendar result = Calendar.getInstance(TIME_ZONE);
        result.set(Calendar.MILLISECOND, 0);
        return result;
    }

    public static Calendar buildCalendarByMin() {
        final Calendar result = Calendar.getInstance(TIME_ZONE);
        result.set(Calendar.MILLISECOND, 0);
        result.set(Calendar.SECOND, 0);
        return result;
    }

}
