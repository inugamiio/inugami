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
package org.inugami.api.tools;

import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

/**
 * CalendarTools
 * 
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
public final class CalendarTools {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static TimeZone TIME_ZONE = TimeZone.getTimeZone(Optional.ofNullable(System.getProperty("user.timezone")).orElse("Europe/Paris"));
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private CalendarTools() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static Calendar buildCalendar() {
        final Calendar result = Calendar.getInstance(TIME_ZONE);
        return result;
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
