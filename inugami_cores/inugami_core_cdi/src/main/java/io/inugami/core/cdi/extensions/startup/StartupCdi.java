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

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * StartupCDI
 *
 * @author patrick_guillerm
 * @since 10 août 2016
 */
@SuppressWarnings({"java:S2326"})
public class StartupCdi<X> implements Extension {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The eager beans list.
     */
    private final List<Bean<?>> eagerBeansList = new ArrayList<>();


    // =========================================================================
    // METHODS
    // =========================================================================

    @SuppressWarnings("hiding")
    public <X> void preLoad(final @Observes ProcessBean<X> event) {
        final Annotated annotated = event.getAnnotated();

        if (annotated.isAnnotationPresent(Startup.class)) {
            final boolean applicationScoped = annotated.isAnnotationPresent(ApplicationScoped.class);
            final boolean singletons        = annotated.isAnnotationPresent(Singleton.class);

            if (applicationScoped || singletons) {
                eagerBeansList.add(event.getBean());
            }
        }

    }

}