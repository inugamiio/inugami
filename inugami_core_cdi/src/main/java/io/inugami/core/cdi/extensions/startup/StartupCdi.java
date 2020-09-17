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
package io.inugami.core.cdi.extensions.startup;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Singleton;

import io.inugami.api.exceptions.TechnicalException;

/**
 * StartupCDI
 * 
 * @author patrick_guillerm
 * @since 10 août 2016
 */
public class StartupCdi<X> implements Extension {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The eager beans list. */
    private final List<Bean<?>> eagerBeansList = new ArrayList<>();
    private BeanManager beanManager;

    // =========================================================================
    // METHODS
    // =========================================================================
    /**
     * Pre load.
     * 
     * @param <X> the generic type
     * @param event the event
     * @param beanManager the bean manager
     * @throws TechnicalException the technical exception
     */
    @SuppressWarnings("hiding")
    public <X> void preLoad(final @Observes ProcessBean<X> event, final BeanManager beanManager) {
        this.beanManager=beanManager;
        final Annotated annotated = event.getAnnotated();

        if (annotated.isAnnotationPresent(Startup.class)) {
            final boolean applicationScoped = annotated.isAnnotationPresent(ApplicationScoped.class);
            final boolean singletons = annotated.isAnnotationPresent(Singleton.class);

            if (applicationScoped || singletons) {
                eagerBeansList.add(event.getBean());
            }
        }

    }

    /**
     * Load.
     * 
     * @param event the event
     * @param beanManager the bean manager
     */
    /*
    public void load(@Observes @Initialized(ApplicationScoped.class) Object init) {
        for (final Bean<?> bean : eagerBeansList) {
            CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
            beanManager.getReference(bean, bean.getBeanClass(), ctx);
        }
    }
    */
}