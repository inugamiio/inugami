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
package io.inugami.core.cdi.producers;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

import io.inugami.api.loggers.Loggers;
import io.inugami.core.context.ApplicationContext;
import io.inugami.core.context.BootstrapContext;
import io.inugami.core.context.Context;

/**
 * Bootstrap
 * 
 * @author patrick_guillerm
 * @since 16 déc. 2016
 */
@ApplicationScoped
public final class Bootstrap {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private ApplicationContext application;
    
    // =========================================================================
    // METHODS BOOTSTRAP
    // =========================================================================
    public void load(@Observes
    @Initialized(ApplicationScoped.class)
    final Object init) {
        Loggers.INIT.info(">>>>> CDI BOOTSTRAP >>>>>");
        try {
            
            ((BootstrapContext) Context.getInstance()).startup();
        }
        catch (final Exception e) {
            Loggers.XLLOG.error("error on initialize context :{}", e.getMessage());
            Loggers.DEBUG.error(e.getMessage(), e);
            throw new Error(e.getMessage());
        }
    }
    
    @PreDestroy
    public void contextDestroyed() {
        ((BootstrapContext) Context.getInstance()).shutdown();
    }
    
    @Produces
    @ApplicationScoped
    public ApplicationContext produce() {
        if (application == null) {
            application = Context.getInstance();
        }
        return application;
    }
    
}
