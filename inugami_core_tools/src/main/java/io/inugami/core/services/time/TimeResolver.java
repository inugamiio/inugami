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
package io.inugami.core.services.time;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.inugami.api.exceptions.Asserts;

/**
 * TimeResolver
 * 
 * @author patrick_guillerm
 * @since 11 oct. 2016
 */
public class TimeResolver {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String KEY_SIGNE = "signe";
    
    private final static String KEY_VALUE = "value";
    
    private final static String KEY_TIME  = "time";
    
    private final Pattern       REGEX     = Pattern.compile("(?<" + KEY_SIGNE + ">[-]{0,1})(?<" + KEY_VALUE
                                                            + ">[0-9]+)(?<" + KEY_TIME
                                                            + ">y|Y|M|w|W|D|d|H|h|min|m|s|S)");
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public boolean resolveSign(final String from) {
        boolean result = false;
        final Matcher matcher = REGEX.matcher(from);
        if (matcher.matches()) {
            final String sign = matcher.group(KEY_SIGNE);
            result = "-".equals(sign);
        }
        return result;
    }
    
    public int resolveShiftTime(final String from) {
        int result = 0;
        final Matcher matcher = REGEX.matcher(from);
        if (matcher.matches()) {
            result = Integer.parseInt(matcher.group(KEY_VALUE));
        }
        return result;
    }
    
    public int resolveCalendarField(final String from) {
        int result = 0;
        final Matcher matcher = REGEX.matcher(from);
        if (matcher.matches()) {
            final String time = matcher.group(KEY_TIME);
            final MapKeyTime timeType = MapKeyTime.getEnum(time);
            Asserts.notNull("unkwnon tiem type : " + time, timeType);
            result = timeType.getField();
        }
        return result;
    }
    
    public Calendar shiftTime(final Calendar calendar, final int field, final int shift, final boolean negatif) {
        final Calendar result = Calendar.getInstance();
        result.setTimeInMillis(calendar.getTimeInMillis());
        result.add(field, negatif ? -shift : shift);
        return result;
    }
    
    public Calendar shiftTime(final Calendar inputCalendar, final String from) {
        final Calendar result = Calendar.getInstance();
        result.setTimeInMillis(inputCalendar.getTimeInMillis());
        
        final boolean negatif = resolveSign(from);
        final int shift = resolveShiftTime(from);
        final int field = resolveCalendarField(from);
        result.add(field, negatif ? -shift : shift);
        
        return result;
    }
    // =========================================================================
    // TIME SHIFT
    // =========================================================================
    
    private enum MapKeyTime {
        //@formatter:off
        YEAR        (Calendar.YEAR,         new String[]{"y","Y"}),
        MONTH       (Calendar.MONTH,        new String[]{"M"}),
        WEEK        (Calendar.WEEK_OF_YEAR, new String[]{"w","W"}),
        DAY         (Calendar.DAY_OF_MONTH, new String[]{"d","D"}),
        HOUR        (Calendar.HOUR_OF_DAY,  new String[]{"h","H"}),
        MINUTE      (Calendar.MINUTE,       new String[]{"m","min"}),
        SECOND      (Calendar.SECOND,       new String[]{"s"}),
        MILLISECOND (Calendar.MILLISECOND,  new String[]{"S"});
        //@formatter:on
        
        private final int      calendarField;
        
        private final String[] matching;
        
        MapKeyTime(final int calendarField, final String... matching) {
            this.calendarField = calendarField;
            this.matching = matching;
        }
        
        public int getField() {
            return calendarField;
        }
        
        public synchronized static MapKeyTime getEnum(final String value) {
            MapKeyTime result = null;
            if (value != null) {
                for (final MapKeyTime item : MapKeyTime.values()) {
                    for (final String matcher : item.matching) {
                        if (matcher.equals(value)) {
                            result = item;
                            break;
                        }
                    }
                    if (result != null) {
                        break;
                    }
                }
            }
            return result;
        }
    }
    
}
