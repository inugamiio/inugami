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
package org.inugami.core.providers.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

import org.inugami.configuration.services.functions.FunctionData;
import org.junit.Test;

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
        final long timeWeek = new Cal(timeTodayMs).add(Calendar.WEEK_OF_YEAR, -1).toTimestamp();
        final long timeMonth= new Cal(timeTodayMs).add(Calendar.MONTH, -1).toTimestamp();
        final long timeYear= new Cal(timeTodayMs).add(Calendar.YEAR, -1).toTimestamp();
        
        
        final String timestampToday = function.apply(new FunctionData("startHourAt", new String[] { "06:00" },0,0));
        assertNotNull(timestampToday);
        assertEquals(String.valueOf(timeToday), timestampToday);

        final String timestampYesterday = function.apply(new FunctionData("startHourAt", new String[] { "06:00", "-1d" },0,0));
        assertNotNull(timestampYesterday);
        assertEquals(String.valueOf(timeYesterday), timestampYesterday);

        
        final String five = function.apply(new FunctionData("startHourAt", new String[] { "05:59"},0,0));
        
        final String timestampWeek = function.apply(new FunctionData("startHourAt", new String[] { "06:00", "-1w" },0,0));
        assertNotNull(timestampWeek);
        assertEquals(String.valueOf(timeWeek), timestampWeek);
        
        
        

        final String timestampMonth = function.apply(new FunctionData("startHourAt", new String[] { "06:00", "-1M" },0,0));
        assertNotNull(timestampMonth);
        assertEquals(String.valueOf(timeMonth), timestampMonth);

        final String timestampYear = function.apply(new FunctionData("startHourAt", new String[] { "06:00", "-1y" },0,0));
        assertNotNull(timestampYear);
        assertEquals(String.valueOf(timeYear), timestampYear);

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
