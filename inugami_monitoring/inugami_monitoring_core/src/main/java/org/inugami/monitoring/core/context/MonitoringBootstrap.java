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
package org.inugami.monitoring.core.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * MonitoringBootstrap
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@WebListener
public class MonitoringBootstrap implements ServletContextListener {
    
    // =========================================================================
    // ATTRIBUTE
    // =========================================================================
    private static final MonitoringContext CONTEXT        = new MonitoringContext();
    
    private static boolean                 hasInitialized = false;
    
    // =========================================================================
    // INITIALIZE
    // =========================================================================
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        contextInitialized();
    }
    
    public synchronized void contextInitialized() {
        if (!hasInitialized) {
            CONTEXT.bootrap(null);
        }
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        contextDestroyed();
    }
    
    public void contextDestroyed() {
        CONTEXT.shutdown(null);
    }
    
    // =========================================================================
    // GETTER
    // =========================================================================
    public static MonitoringContext getContext() {
        return CONTEXT;
    }
}
