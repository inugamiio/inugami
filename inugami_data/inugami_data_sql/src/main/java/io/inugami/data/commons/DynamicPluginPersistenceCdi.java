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
package io.inugami.data.commons;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.persistence.Entity;

/**
 * DynamicPluginPersistenceCdi
 * 
 * @author patrick_guillerm
 * @since 15 janv. 2018
 */
public class DynamicPluginPersistenceCdi implements Extension {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final List<Class<?>> ENTITIES = new ArrayList<>();
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public <X> void preLoad(final @Observes ProcessBean<X> event) {
        final Annotated annotated = event.getAnnotated();
        
        if (annotated.isAnnotationPresent(Entity.class)) {
            ENTITIES.add(event.getBean().getBeanClass());
        }
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    static List<Class<?>> getEntities() {
        return ENTITIES;
    }
}
