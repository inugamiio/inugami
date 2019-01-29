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
package org.inugami.core.services.scheduler;

/**
 * SchedulerNameHelper
 * 
 * @author patrick_guillerm
 * @since 12 sept. 2017
 */
public final class SchedulerNameHelper {
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private SchedulerNameHelper() {
        
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static String buildName(final String cronExpression) {
        String localCronExpr = cronExpression;
        if (cronExpression.startsWith("{{")) {
            localCronExpr = cronExpression.replaceAll("[{}]", "");
        }
        
        return localCronExpr;
    }
}
