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

import io.inugami.configuration.services.functions.FunctionData;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * StartHourAtFunctionTest
 *
 * @author patrick_guillerm
 * @since 17 août 2017
 */
public class StartHourAtFunctionTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testApply() {
        final StartHourAtFunction function = new StartHourAtFunction();

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //@formatter:off
        final long timeTodayMs = calendar.getTimeInMillis();
        final long timeToday = timeTodayMs/1000;
        final long timeYesterday = new Cal(timeTodayMs).add(Calendar.DAY_OF_YEAR, -1).toTimestamp();
        
        
        final String timestampToday = function.apply(new FunctionData("startHourAt", new String[] { "06:00" },0,0));
        assertNotNull(timestampToday);
        assertEquals(String.valueOf(timeToday), timestampToday);

        final String timestampYesterday = function.apply(new FunctionData("startHourAt", new String[] { "06:00", "-1d" },0,0));
        assertNotNull(timestampYesterday);
        assertEquals(String.valueOf(timeYesterday), timestampYesterday);


    }


    
    private class Cal {
        final Calendar calendar;
        
        public Cal(final long timestamp){
            this.calendar = Calendar.getInstance();
            this.calendar.setTimeInMillis(timestamp);
        }
        
        public Cal add(final int field, final int value){
            calendar.add(field, value);
            return this;
        }
        
        public long toTimestamp(){
            return calendar.getTimeInMillis()/1000;
        }
    }
}
