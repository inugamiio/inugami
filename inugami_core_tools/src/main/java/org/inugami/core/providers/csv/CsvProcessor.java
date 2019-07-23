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
package org.inugami.core.providers.csv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.data.JsonObject;
import org.inugami.api.models.data.graphite.TimeValue;
import org.inugami.api.models.data.graphite.number.FloatNumber;
import org.inugami.api.models.data.graphite.number.GraphiteNumber;
import org.inugami.api.models.data.graphite.number.LongNumber;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.providers.MockJsonHelper;

/**
 * CsvTask
 * 
 * @author patrickguillerm
 * @since 9 oct. 2018
 */
public class CsvProcessor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String          eventName;
    
    private final List<TimeValue> hits = new ArrayList<>();
    
    private final int             size;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CsvProcessor(final String path) {
        eventName = path.replaceAll(MockJsonHelper.MOCK_DIRECTORY, "").replaceAll(".csv", "").replaceAll("/", "-");
        final String rawContent = FilesUtils.readFileFromClassLoader(path);
        if (rawContent != null) {
            loadContent(rawContent);
        }
        size = hits.size();
    }
    
    private void loadContent(final String rawContent) {
        final Pattern csvLineRegEx = Pattern.compile("(?:\\s*)([0-9]+)(?:\\s*;\\s*)([0-9]+[.]{0,1}[0-9]*)(?:\\s*)");
        final Matcher matcher = csvLineRegEx.matcher("");
        
        final String[] lines = rawContent.split("\n");
        for (final String line : lines) {
            if (matcher.reset(line).matches()) {
                hits.add(buildTimeValue(matcher.group(1), matcher.group(2)));
            }
        }
        
    }
    
    private TimeValue buildTimeValue(final String timestamp, final String value) {
        //@formatter:off
        final GraphiteNumber number = value.contains(".")?new FloatNumber(Double.parseDouble(value))
                                                     :new LongNumber(Long.parseLong(value));
        //@formatter:on
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timestamp));
        cal.set(Calendar.YEAR, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        return new TimeValue(eventName, number, cal.getTimeInMillis());
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public <T extends SimpleEvent> boolean accept(final T event) {
        return eventName.equals(event.getName());
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public <T extends SimpleEvent> JsonObject process(final T event, final ConfigHandler<String, String> config) {
        final Calendar cal = Calendar.getInstance();
        
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.YEAR, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        final long now = cal.getTimeInMillis();
        
        TimeValue result = null;
        
        for (int i = 0; i < size; i++) {
            if ((result == null) || (hits.get(i).getTime() <= now)) {
                result = hits.get(i);
            }
            
            if (hits.get(i).getTime() > now) {
                break;
            }
        }
        logDate(result.getTime());
        return result;
    }
    
    private String logDate(final long now) {
        
        final Date time = new Date();
        time.setTime(now);
        final String result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
        Loggers.DEBUG.debug("time : {}", result);
        return result;
    }
}
