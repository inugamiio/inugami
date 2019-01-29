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
package org.inugami.api.alertings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * AlertingResultTest
 * 
 * @author patrick_guillerm
 * @since 9 mars 2018
 */
public class AlertingResultTest {
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testConvertToJson() {
        final AlertingResult alert = new AlertingResult();
        alert.setAlerteName("alertName");
        alert.setLevel("error ios");
        alert.setMessage("foobar message");
        alert.setSubLabel("sub message");
        alert.setDuration(60L);
        alert.setCreated(1520605752814L);
        alert.setChannel("plugin");
        
        final String json = alert.convertToJson();
        assertNotNull(json);
        assertEquals(buildRefData(), json);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private String buildRefData() {
        //@formatter:off
        return "{\"alertName\":\"alertName\","
                + "\"channel\":\"plugin\","
                + "\"level\":\"error ios\","
                + "\"levelType\":\"ERROR\","
                + "\"levelNumber\":100000,"
                + "\"message\":\"foobar message\","
                + "\"subLabel\":\"sub message\","
                + "\"url\":null,"
                + "\"created\":1520605752814,"
                + "\"duration\":60,"
                + "\"data\":null,"
                + "\"multiAlerts\":false}";
        //@formatter:on
    }
}
