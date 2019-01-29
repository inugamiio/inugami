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
package org.inugami.core.cdi.extensions.producers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Priority;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

/**
 * BeansProducer
 * 
 * @author patrickguillerm
 * @since 20 janv. 2018
 */
public interface BeansProducer<T> {

    BeanManager getBeanManager();

    Class<? extends T> getType();

    default boolean mustFailOnInit() {
        return false;
    }

    @SuppressWarnings("unchecked")
    default List<T> produceBeansInstances() {
        final List<T> result = new ArrayList<>();
        final Map<Integer, List<T>> instances = new HashMap<>();

        Set<Bean<?>> beans = getBeanManager().getBeans(getType());
        for (Bean<?> bean : beans) {
            CreationalContext<?> creationalContext = getBeanManager().createCreationalContext(bean);

            T instance = null;
            try {
                instance = (T) getBeanManager().getReference(bean, getType(), creationalContext);
            } catch (Exception e) {
                if (mustFailOnInit()) {
                    throw e;
                }
            }

            if (instance != null) {
                final int priority = loadBeanPriority(bean);
                if (!instances.containsKey(priority)) {
                    instances.put(priority, new ArrayList<>());
                }

                instances.get(priority).add(instance);
            }

        }

        final List<Integer> keys = Arrays.asList(instances.keySet().toArray(new Integer[] {}));
        keys.sort((ref, value) -> ref.compareTo(value));
        for (Integer key : keys) {
            result.addAll(instances.get(key));
        }
        return result;
    }

    default int loadBeanPriority(Bean<?> bean) {
        int result = 0;
        final Priority priority = bean.getBeanClass().getAnnotation(Priority.class);
        if (priority != null) {
            result = priority.value();
        }
        return result;
    }

}
