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
package io.inugami.api.tools;

import io.inugami.api.loggers.Loggers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S1872"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AnnotationTools {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String JAVAX_NAMED = "javax.inject.Named";

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
        if (annotation == null || method == null) {
            return null;
        }
        Method           result      = null;
        final Class<?>[] paramsTypes = {};

        try {
            result = annotation.annotationType().getDeclaredMethod(method, paramsTypes);
        } catch (final NoSuchMethodException | SecurityException e) {
            Loggers.DEBUG.debug(e.getMessage(), e);
        }

        return result;
    }

    public static Method searchMethod(final Class<?> objectClass, final String method) {
        if (objectClass == null || method == null) {
            return null;
        }
        Method           result      = null;
        final Class<?>[] paramsTypes = {};

        try {
            result = objectClass.getDeclaredMethod(method, paramsTypes);
        } catch (final NoSuchMethodException | SecurityException e) {
            Loggers.DEBUG.debug(e.getMessage(), e);
        }

        return result;
    }

    public static <T> T invoke(final Method method, final Object object, final Object... params) {
        T result = null;
        if ((method != null) && (object != null)) {
            method.setAccessible(true);
            try {
                result = (T) method.invoke(object, params);
            } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }

        return result;
    }

    public static String resolveNamed(final Object object) {

        if (object == null) {
            return null;
        }

        String result = null;
        if (object instanceof NamedComponent) {
            result = ((NamedComponent) object).getName();
        } else {
            Class<?> clazz = object.getClass();
            if ("org.jboss.weld.proxy.WeldClientProxy".equals(clazz.getName())) {
                clazz = extractCdiBeanClass(object);
            }

            if (clazz == null) {
                return null;
            }

            final Annotation annotation = searchAnnotation(clazz.getAnnotations(), JAVAX_NAMED);
            Method           getValue   = null;
            if (annotation != null) {
                getValue = searchMethod(annotation, "value");
            }
            if (getValue != null) {
                result = invoke(getValue, annotation);
            }
            if ((result == null) || result.trim().isEmpty()) {
                result = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
            }
        }


        return result;
    }

    static Class<?> extractCdiBeanClass(final Object bean) {
        return bean == null ? null : invokeMethods(bean, "getMetadata", "getBean", "getBeanClass");

    }

    static <T> T invokeMethods(final Object object, final String... methodeNames) {
        Object result = null;
        if (object != null) {
            Object currentObject = object;
            for (final String methodeName : methodeNames) {
                currentObject = invokeMethod(methodeName, currentObject);
                if (currentObject == null) {
                    break;
                }
            }
            result = currentObject;
        }
        return (T) result;


    }

    static <T> T invokeMethod(final String methodeName, final Object currentObject) {
        T        result         = null;
        Method   methodToInvoke = null;
        Method[] methods        = null;
        if (currentObject != null) {
            methods = currentObject.getClass().getDeclaredMethods();
        }

        if (methods != null) {
            for (final Method method : methods) {
                if (method.getName().equals(methodeName) && method.getParameterCount() == 0) {
                    methodToInvoke = method;
                }
            }
        }

        if (methodToInvoke != null) {
            try {
                result = (T) methodToInvoke.invoke(currentObject);
            } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public static <T> List<Method> extractGetters(final T instance) {
        final List<Method>      result      = new ArrayList<>();
        Field[]                 fields      = null;
        Class<? extends Object> objectClass = null;
        if (instance != null) {
            objectClass = instance.getClass();
            fields = objectClass.getDeclaredFields();
        }

        if (fields != null) {
            final Method[] methods = objectClass.getDeclaredMethods();

            for (final Field field : fields) {
                final Method method = searchGetterMethod(field, methods);
                if (method != null) {
                    result.add(method);
                }
            }
        }
        return result;
    }

    private static Method searchGetterMethod(final Field field, final Method[] methods) {
        String methodName = "";
        if (field.getType() == boolean.class) {
            methodName = "is" + field.getName();
        } else {
            methodName = "get" + field.getName();
        }

        for (final Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName) && method.getParameterCount() == 0) {
                return method;
            }
        }
        return null;
    }

    private static Method searchSetterMethod(final Field field, final Method[] methods) {
        final String methodName = "set" + field.getName();
        for (final Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName) && method.getParameterCount() == 1) {
                return method;
            }
        }
        return null;
    }

    public static <T> List<FieldGetterSetter> extractFieldGetterAndSetter(final T instance) {
        final List<FieldGetterSetter> result = new ArrayList<>();

        if (instance != null) {
            final Class<? extends Object> objectClass = instance.getClass();
            final Field[]                 fields      = objectClass.getDeclaredFields();
            for (final Field field : fields) {
                final FieldGetterSetter info = extractFieldInfo(field, objectClass, instance);
                if (info != null) {
                    result.add(info);
                }
            }
        }
        return result;
    }

    private static <T> FieldGetterSetter extractFieldInfo(final Field field, final Class<?> objectClass, final T instance) {
        if (isInvalidField(field)) {
            return null;
        }

        field.setAccessible(true);
        Object currentValue = null;

        try {
            currentValue = field.get(instance);
        } catch (final Throwable e) {
        }
        if (currentValue == null) {
            return null;
        }
        final Method[] methods = objectClass.getMethods();
        final Method   getter  = searchGetterMethod(field, methods);
        final Method   setter  = searchSetterMethod(field, methods);

        if (getter == null || setter == null) {
            return null;
        }
        return FieldGetterSetter.builder()
                                .field(field)
                                .value(currentValue)
                                .getter(getter)
                                .setter(setter)
                                .build();
    }

    private static boolean isInvalidField(final Field field) {
        return Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers());
    }
}
