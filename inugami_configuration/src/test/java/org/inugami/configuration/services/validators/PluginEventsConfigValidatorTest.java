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
package org.inugami.configuration.services.validators;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.inugami.commons.engine.JavaScriptEngine;
import org.junit.Test;

/**
 * PluginEventsConfigValidatorTest
 * 
 * @author patrick_guillerm
 * @since 20 déc. 2017
 */
public class PluginEventsConfigValidatorTest {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testvalidateAlerteCondition() throws Exception {
        final PluginEventsConfigValidator validator = new PluginEventsConfigValidator(null, null);
        //@formatter:off
        final String scriptA=String.join("\n", 
               "var a=2;",
               "var u = {'foo':{'title':'hop'}};"
        );
        //@formatter:on
        JavaScriptEngine.getInstance().checkScriptInnerFunction(scriptA);
        
        final List<Condition> conditionsA = validator.validateAlerteCondition(scriptA);
        assertTrue(conditionsA.isEmpty());
    }
    
}
