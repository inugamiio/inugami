package io.inugami.api.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtils {

    public static Annotation searchAnnotation(final Annotation[] annotations, final String... names) {
        return AnnotationTools.searchAnnotation(annotations, names);
    }

    public static Method searchMethod(final Annotation annotation, final String method) {
        return AnnotationTools.searchMethod(annotation, method);
    }

    public static Method searchMethod(final Class<?> objectClass, final String method) {
        return AnnotationTools.searchMethod(objectClass, method);
    }

    public static <T> T invoke(final Method method, final Object object, final Object... params) {
        return AnnotationTools.invoke(method, object, params);
    }

    public static <T> T invokeMethod(final String methodeName, final Object currentObject) {
        return AnnotationTools.invokeMethod(methodeName, currentObject);
    }

    public static String resolveNamed(final Object object) {
        return AnnotationTools.resolveNamed(object);
    }

    public static Class<?> extractCdiBeanClass(final Object bean) {
        return AnnotationTools.extractCdiBeanClass(bean);
    }

    public static <T> T invokeMethods(final Object object, final String... methodeNames) {
        return AnnotationTools.invokeMethods(object, methodeNames);
    }


    public static <T> List<Method> extractGetters(final T instance) {
        return AnnotationTools.extractGetters(instance);
    }

    public static void sortMethods(final List<Method> methods) {
        if (methods != null) {
            Collections.sort(methods, (ref, val) -> {
                return ref.getName().compareTo(val.getName());
            });
        }
    }

    public static <T> List<FieldGetterSetter> extractFieldGetterAndSetter(final T instance) {
        return AnnotationTools.extractFieldGetterAndSetter(instance);
    }
}
