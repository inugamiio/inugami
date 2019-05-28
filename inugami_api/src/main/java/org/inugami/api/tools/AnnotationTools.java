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
package org.inugami.api.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.inugami.api.loggers.Loggers;
import org.jboss.weld.proxy.WeldClientProxy;

public final class AnnotationTools {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String JAVAX_NAMED = "javax.inject.Named";
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private AnnotationTools() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static Annotation searchAnnotation(final Annotation[] annotations, final String... names) {
        final Annotation result = null;
        if (annotations != null) {
            for (int i = annotations.length - 1; i >= 0; i--) {
                final String className = annotations[i].annotationType().getCanonicalName();
                
                for (final String name : names) {
                    if (name.equals(className)) {
                        return annotations[i];
                    }
                }
            }
        }
        return result;
    }
    
    public static Method searchMethod(final Annotation annotation, final String method) {
        Method result = null;
        final Class<?>[] paramsTypes = {};
        if (annotation != null) {
            try {
                result = annotation.annotationType().getDeclaredMethod(method, paramsTypes);
            }
            catch (NoSuchMethodException | SecurityException e) {
                Loggers.DEBUG.debug(e.getMessage(), e);
            }
        }
        return result;
    }
    
    public static <T> T invoke(final Method method, final Object object, final Object... params) {
        T result = null;
        if ((method != null) && (object != null)) {
            method.setAccessible(true);
            try {
                result = (T) method.invoke(object, params);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
        
        return result;
    }
    
    public static String resolveNamed(final Object object) {
        String result = null;
        if (object != null) {
            
            if (object instanceof NamedComponent) {
                result = ((NamedComponent) object).getName();
            }
            else {
                Class<?> clazz = object.getClass();
                if (object instanceof WeldClientProxy) {
                    clazz = ((WeldClientProxy) object).getMetadata().getBean().getBeanClass();
                }
                
                final Annotation annotation = searchAnnotation(clazz.getAnnotations(), JAVAX_NAMED);
                
                if (annotation != null) {
                    final Method getValue = searchMethod(annotation, "value");
                    if (getValue != null) {
                        result = invoke(getValue, annotation);
                    }
                }
                
                if ((result == null) || result.trim().isEmpty()) {
                    //@formatter:off
                    result = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
                    //@formatter:on
                }
            }
            
        }
        
        return result;
    }
}
