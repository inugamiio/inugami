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
package io.inugami.core.providers.functions;

import io.inugami.api.exceptions.Asserts;
import io.inugami.configuration.services.functions.FunctionData;
import io.inugami.configuration.services.functions.ProviderAttributFunction;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StartHourAtFunction
 *
 * @author patrick_guillerm
 * @since 17 août 2017
 */
@SuppressWarnings({"java:S6397", "java:S6395", "java:S6353", "java:S6353"})
public class StartHourAtFunction implements ProviderAttributFunction {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    private static final String NAME = "startHourAt";

    private static final String HOUR = "hour";

    private static final String MIN = "min";

    private static final String NEGATIVE = "negatif";

    private static final String TIME = "time";

    private static final String TIME_UNIT = "timeUnit";

    private static final String NOW = "now";

    private static final Pattern HOUR_REGEX = Pattern.compile("((?<hour>(?:[0-1][0-9])|(?:2[0-3]))(?:[:])(?<min>[0-5][0-9]))");

    private static final Pattern TIME_SHIFT_REGEX = Pattern.compile("(?<negatif>[-]{0,1})(\\s*)(?<time>[0-9]+)(\\s*)(?<timeUnit>s|min|h|d|M|w|y)");

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String apply(final FunctionData data) {
        String result = null;
        if (data.getParameters() != null) {
            long timestamp = computeHourFromToday(data.getParameters()[0]);
            
            if (data.getParameters().length == 2) {
                timestamp = computeTimeShift(timestamp, data.getParameters()[1]);
            }

            result = formatTime(timestamp);
        }
        return result;
    }

    protected String formatTime(final long timestamp) {
        return String.valueOf((timestamp / 1000));
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    protected long computeHourFromToday(final String hourStr) {
        final Matcher  matcher  = HOUR_REGEX.matcher(hourStr);
        int            hour     = 0;
        int            min      = 0;
        final Calendar calendar = Calendar.getInstance();

        if (NOW.equals(hourStr)) {
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
        } else if (matcher.matches()) {
            hour = Integer.parseInt(matcher.group(HOUR));
            min = Integer.parseInt(matcher.group(MIN));
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }
        return calendar.getTimeInMillis();
    }

    @SuppressWarnings({"java:S2259"})
    public long computeTimeShift(final long timestamp, final String timeShift) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        final Matcher matcher = TIME_SHIFT_REGEX.matcher(timeShift);
        if (matcher.matches()) {
            final String negative    = matcher.group(NEGATIVE);
            final String time        = matcher.group(TIME);
            final String timeUnitStr = matcher.group(TIME_UNIT);

            int timeValue = Integer.parseInt(time);
            if ((negative != null) && !negative.isEmpty()) {
                timeValue = -timeValue;
            }
            final TimeUnit timeUnit = TimeUnit.getEnum(timeUnitStr);
            Asserts.assertNotNull("unkwnow timeunit :" + timeUnitStr, timeUnit);
            calendar.set(timeUnit.getCalendarField(), calendar.get(timeUnit.getCalendarField()) + timeValue);

        }
        return calendar.getTimeInMillis();
    }

    // =========================================================================
    // TIME UNIT
    // =========================================================================
    private enum TimeUnit {
        //@formatter:off
        SECOND("s", Calendar.SECOND),
        MIN("min", Calendar.MINUTE),
        HOUR("h", Calendar.HOUR_OF_DAY),
        DAY("d", Calendar.DAY_OF_YEAR),
        MONTH("M", Calendar.MONTH),
        WEEK("w", Calendar.WEEK_OF_YEAR),
        YEAR("y", Calendar.YEAR);
        //@formatter:on

        private final String key;

        private final int calendarField;

        TimeUnit(final String key, final int calendarField) {
            this.key = key;
            this.calendarField = calendarField;
        }

        public String getKey() {
            return key;
        }

        public int getCalendarField() {
            return calendarField;
        }

        public static synchronized TimeUnit getEnum(final String value) {
            TimeUnit result = null;
            for (final TimeUnit item : TimeUnit.values()) {
                if (item.getKey().equals(value)) {
                    result = item;
                    break;
                }
            }
            return result;
        }
    }

}
