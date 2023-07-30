package io.inugami.api.tools;

import io.inugami.api.exceptions.UncheckedException;
import io.inugami.api.functionnals.GenericActionWithException;
import io.inugami.api.loggers.Loggers;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings({"java:S119", "java:S3011", "java:S1452"})
@Slf4j
@UtilityClass
public final class ReflectionUtils {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final ClassLoader CLASS_LOADER         = null;
    public static final  String      GET                  = "get";
    public static final  String      IS                   = "is";
    private static final int         CLASS_EXTENSION_SIZE = ".class".length();

    private static final List<Class<?>> PRIMITIVE_TYPES = List.of(
            boolean.class,
            byte.class,
            char.class,
            short.class,
            int.class,
            long.class,
            float.class,
            double.class,
            long.class);
    public static final  String         NAME            = "name";

    // =========================================================================
    // ENUM
    // =========================================================================
    public static List<Object> getEnumValues(final Class<?> enumClass) {
        final Object[] values = runSafe(() -> enumClass.getEnumConstants());
        return values == null ? new ArrayList<Object>() : Arrays.asList(values);
    }

    // =========================================================================
    // CLASS
    // =========================================================================
    @SafeVarargs
    public static Set<Class<?>> scan(final String basePackage, final Predicate<Class<?>>... filters) {
        try {
            return processScan(basePackage, filters);
        } catch (final IOException e) {
            return Set.of();
        }
    }

    private static Set<Class<?>> processScan(final String basePackage, final Predicate<Class<?>>... filters) throws IOException {
        final Set<Class<?>> result      = new LinkedHashSet<>();
        final ClassLoader   classloader = getClassloader();

        final List<String>     bases    = new ArrayList<>();
        final Enumeration<URL> allBases = classloader.getResources("");
        while (allBases.hasMoreElements()) {
            bases.add(allBases.nextElement().getFile());
        }

        final Enumeration<URL> urls = classloader.getResources(basePackage == null ? "." : basePackage.replace('.', '/'));

        final List<String> classes = new ArrayList<>();
        while (urls.hasMoreElements()) {
            final URL  url         = urls.nextElement();
            final File currentFile = new File(url.getFile());
            classes.addAll(scanAllFiles(currentFile, bases));
        }

        for (final String className : classes) {
            Class<?> currentClass = null;
            try {
                currentClass = classloader.loadClass(className);
            } catch (final ClassNotFoundException e) {
            }

            if (currentClass != null && classFiltersMatch(currentClass, filters)) {
                result.add(currentClass);
            }
        }

        final List<Class<?>> buffer = new ArrayList<>(result);
        Collections.sort(buffer, (r, v) -> r.getName().compareTo(v.getName()));
        return new LinkedHashSet<>(buffer);
    }

    private static boolean classFiltersMatch(final Class<?> currentClass, final Predicate<Class<?>>[] filters) {
        if (filters == null || filters.length == 0) {
            return true;
        }
        boolean result = true;
        for (final Predicate<Class<?>> filter : filters) {
            result = filter.test(currentClass);
            if (!result) {
                break;
            }
        }
        return result;
    }

    private static String extractBasePath(final ClassLoader classloader) {
        final URL result = classloader.getResource("");
        return result == null ? "" : result.getFile();
    }

    private static List<String> scanAllFiles(final File currentFile, final List<String> bases) {
        final List<String> result = new ArrayList<>();

        if (!currentFile.exists()) {
            return result;
        }
        if (currentFile.isDirectory()) {
            for (final File childFile : currentFile.listFiles()) {
                result.addAll(scanAllFiles(childFile, bases));
            }
        } else if (currentFile.isFile()) {
            final String base     = chooseBase(currentFile.getAbsolutePath(), bases);
            final String filePath = currentFile.getAbsolutePath().replace(base, "");
            if (filePath.endsWith(".class")) {
                result.add(filePath.substring(0, filePath.length() - CLASS_EXTENSION_SIZE).replace('/', '.'));
            }
        }
        return result;
    }

    private static String chooseBase(final String absolutePath, final List<String> bases) {
        for (final String base : bases) {
            if (absolutePath.startsWith(base)) {
                return base;
            }
        }
        return "";
    }


    // =========================================================================
    // ANNOTATION
    // =========================================================================
    public static Annotation searchAnnotation(final Annotation[] annotations, final String... names) {
        return AnnotationTools.searchAnnotation(annotations, names);
    }

    public static <AE extends AnnotatedElement> boolean hasAnnotation(final AE annotatedElement,
                                                                      final Class<? extends Annotation>... annotations) {
        boolean result = false;
        if (annotatedElement != null && annotations != null) {
            for (final Class<? extends Annotation> annotation : annotations) {
                result = annotatedElement.getDeclaredAnnotation(annotation) != null;
                if (result) {
                    break;
                }
            }
        }
        return result;
    }


    public static <T, A extends Annotation, AE extends AnnotatedElement> T ifHasAnnotation(final AE annotatedElement,
                                                                                           final Class<A> annotation,
                                                                                           final Function<A, T> handler) {
        return ifHasAnnotation(annotatedElement, annotation, handler, null);
    }


    public static <T, A extends Annotation, AE extends AnnotatedElement> T ifHasAnnotation(final AE annotatedElement,
                                                                                           final Class<A> annotation,
                                                                                           final Function<A, T> handler,
                                                                                           final Supplier<T> defaultValue) {
        T result = null;
        if (hasAnnotation(annotatedElement, annotation)) {
            result = handler == null ? null : handler.apply(annotatedElement.getDeclaredAnnotation(annotation));
        }

        if (result == null && defaultValue != null) {
            result = defaultValue.get();
        }
        return result;
    }


    public static <T, A extends Annotation, AE extends AnnotatedElement> void processOnAnnotation(
            final AE annotatedElement,
            final Class<A> annotationClass,
            final Consumer<A> handler) {
        final A annotation = annotatedElement == null ? null : annotatedElement.getDeclaredAnnotation(annotationClass);
        if (annotation != null && handler != null) {
            handler.accept(annotation);
        }
    }


    public static <A extends Annotation, AE extends AnnotatedElement> A getAnnotation(final AE annotatedElement,
                                                                                      final Class<A> annotation) {
        A result = null;
        if (annotatedElement != null) {
            result = annotatedElement.getDeclaredAnnotation(annotation);
            if (result == null) {
                result = annotatedElement.getAnnotation(annotation);
            }
        }
        return result;
    }
    // =========================================================================
    // FIELDS
    // =========================================================================

    public static Object getStaticFieldValue(final String fieldName, final Class<?> clazz) {
        final Field currentField = getField(fieldName, clazz);
        currentField.setAccessible(true);
        return runSafe(() -> currentField.get(null));
    }

    public static Object getFieldValue(final String fieldName, final Object instance) {
        final Field currentField = getField(fieldName, instance);
        setAccessible(currentField);
        return runSafe(() -> currentField.get(instance));
    }


    public static Field getField(final String fieldName, final Object instance) {
        if (instance == null || fieldName == null) {
            return null;
        }
        return getField(fieldName, instance.getClass());
    }


    public static Field getField(final String fieldName, final Class<?> instanceClass) {
        if (instanceClass == null || fieldName == null) {
            return null;
        }
        Field             currentField = null;
        final List<Field> fields       = getAllFields(instanceClass);
        for (final Field classField : fields) {
            if (classField.getName().equals(fieldName)) {
                currentField = classField;
                break;
            }
        }
        return currentField;
    }

    public static List<Field> getAllFields(final Class<?> instanceClasss) {
        final List<Field> result = new ArrayList<>();
        if (instanceClasss == null || instanceClasss == Object.class) {
            return result;
        }
        result.addAll(Arrays.asList(instanceClasss.getDeclaredFields()));
        if (instanceClasss.getSuperclass() != null) {
            result.addAll(getAllFields(instanceClasss.getSuperclass()));
        }

        return result;
    }


    public static Set<Field> loadAllFields(final Class<?> clazz) {
        final Set<Field> result = new LinkedHashSet<>();
        try {
            if (clazz != null && clazz != Object.class) {
                result.addAll(Arrays.asList(clazz.getDeclaredFields()));
                if (clazz.getSuperclass() != null) {
                    result.addAll(loadAllFields(clazz.getSuperclass()));
                }
            }
        } catch (final Throwable err) {
            Loggers.DEBUG.warn(err.getMessage());
        }

        return result;
    }

    public static Set<Field> loadAllStaticFields(final Class<?> clazz) {
        final Set<Field> result = new LinkedHashSet<>();
        loadAllFields(clazz).stream()
                            .filter(field -> Modifier.isStatic(field.getModifiers()))
                            .forEach(result::add);
        return result;
    }

    public static List<Field> extractParentsFields(final Class<?> superclass) {
        final List<Field> result = new ArrayList<>();
        if (superclass != null) {
            result.addAll(Arrays.asList(superclass.getDeclaredFields()));
            if (superclass.getSuperclass() != null) {
                result.addAll(extractParentsFields(superclass.getSuperclass()));
            }
        }
        return result;
    }

    public static Field buildField(final Class<?> type, final String name) {
        Field                field       = null;
        final Constructor<?> constructor = Field.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        try {
            field = (Field) constructor.newInstance(type, name, type, 0, 0, null, null);
        } catch (final Exception e) {
            Loggers.DEBUG.error(e.getMessage());
        }
        return field;
    }


    public static <T extends Object> T setFieldValue(final Field field, final Object value, final T instance) {
        if (field != null && instance != null) {
            field.setAccessible(true);
            try {
                field.set(instance, value);
            } catch (final IllegalAccessException e) {
                throw new UncheckedException(e.getMessage(), e);
            }
        }
        return instance;
    }

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public static Set<Constructor<?>> loadAllConstructors(final Class<?> clazz) {
        final Set<Constructor<?>> result = new LinkedHashSet<>();
        try {
            if (clazz != null && clazz != Object.class) {
                result.addAll(Arrays.asList(clazz.getDeclaredConstructors()));
                if (clazz.getSuperclass() != null) {
                    result.addAll(loadAllConstructors(clazz.getSuperclass()));
                }
            }
        } catch (final Throwable err) {
            Loggers.DEBUG.warn(err.getMessage());
        }

        return result;
    }


    // =========================================================================
    // METHODS
    // =========================================================================
    public static List<Method> loadAllMethods(final Class<?> clazz) {
        final List<Method> result = new ArrayList<>();

        try {
            if (clazz != null && clazz != Object.class) {
                result.addAll(Arrays.asList(clazz.getDeclaredMethods()));
                if (clazz.getSuperclass() != null) {
                    result.addAll(loadAllMethods(clazz.getSuperclass()));
                }
            }

        } catch (final Throwable err) {
            Loggers.DEBUG.warn(err.getMessage());
        }

        return result;
    }

    public static Method searchMethod(final Annotation annotation, final String method) {
        return AnnotationTools.searchMethod(annotation, method);
    }

    public static Method searchMethod(final Class<?> objectClass, final String method) {
        return AnnotationTools.searchMethod(objectClass, method);
    }

    public static <T> List<FieldGetterSetter> extractFieldGetterAndSetter(final T instance) {
        return AnnotationTools.extractFieldGetterAndSetter(instance);
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

    public static Object callGetterForField(final String field, final Object instance) {
        if (field == null || instance == null) {
            return null;
        }
        final List<Method> methods = loadAllMethods(instance.getClass());
        final Method getter = methods.stream()
                                     .filter(m -> m.getName().equalsIgnoreCase(GET + field)
                                             || m.getName().equalsIgnoreCase(IS + field))
                                     .findFirst()
                                     .orElse(null);
        setAccessible(getter);
        return runSafe(() -> getter.invoke(instance));
    }

    // =========================================================================
    // INVOKE
    // =========================================================================
    public static <T> T invoke(final Method method, final Object object, final Object... params) {
        return AnnotationTools.invoke(method, object, params);
    }

    public static <T> T invokeMethod(final String methodeName, final Object currentObject) {
        return AnnotationTools.invokeMethod(methodeName, currentObject);
    }


    public static <T> T invokeMethods(final Object object, final String... methodeNames) {
        return AnnotationTools.invokeMethods(object, methodeNames);
    }


    public static String resolveNamed(final Object object) {
        return AnnotationTools.resolveNamed(object);
    }

    public static Class<?> extractCdiBeanClass(final Object bean) {
        return AnnotationTools.extractCdiBeanClass(bean);
    }

    // =========================================================================
    // ACCESSIBLE
    // =========================================================================
    public static void setAccessible(final Method method) {
        if (method != null) {
            try {
                method.setAccessible(true);
            } catch (final Throwable e) {
                traceError(e, log);
            }
        }
    }

    public static void setAccessible(final Field field) {
        if (field != null) {
            try {
                field.setAccessible(true);
            } catch (final Throwable e) {
                traceError(e, log);
            }
        }
    }


    // =========================================================================
    // GENERIC TYPE
    // =========================================================================
    public static boolean isBasicType(final Class<?> currentClass) {
        return currentClass == null ? true :
                PRIMITIVE_TYPES.contains(currentClass) || currentClass.getName().startsWith("java.lang", 0);
    }

    public static Class<?> getGenericType(final Type type) {
        Class<?> result = null;
        if (type instanceof ParameterizedType) {
            final ParameterizedType paramType = (ParameterizedType) type;
            final Type[]            argTypes  = paramType.getActualTypeArguments();
            if (argTypes.length > 0) {
                result = argTypes[0].getClass();
            }
        }
        return result;
    }

    public static Class<?> extractGenericType(final Type genericType) {
        return extractGenericType(genericType, 0);
    }

    public static Class<?> extractGenericType(final Type genericType, final int typeIndex) {
        Class<?> result = null;
        if (genericType != null) {
            if (genericType instanceof ParameterizedType) {
                final String className = ((ParameterizedType) genericType).getActualTypeArguments()[typeIndex]
                        .getTypeName();
                try {
                    result = getClassloader().loadClass(className);
                } catch (final ClassNotFoundException e) {
                    log.error("no class def found : {}", genericType);
                }
            } else if (genericType instanceof Class) {
                result = (Class<?>) genericType;
            }

        }
        return result;
    }

    private static ClassLoader getClassloader() {
        return CLASS_LOADER == null ? Thread.currentThread().getContextClassLoader() : CLASS_LOADER;
    }

    // =========================================================================
    // API CONVERTORS
    // =========================================================================
    public static int parseInt(final Object value) {
        int result = 500;
        try {
            result = (int) value;
        } catch (final Throwable e) {
            traceError(e, log);
        }
        return result;
    }

    // =========================================================================
    // ERRORS
    // =========================================================================
    public static <T> T runSafe(final GenericActionWithException<T> action) {
        if (action == null) {
            return null;
        }

        try {
            return action.process();
        } catch (final Throwable e) {
            traceError(e, log);
            return null;
        }
    }

    public static void traceError(final Throwable e, final Logger logger) {
        if (logger.isDebugEnabled()) {
            if (e instanceof ClassNotFoundException) {
                logger.error(e.getMessage());
            } else {
                logger.error(e.getMessage(), e);
            }

        }
    }

    public static Map<String, Map<String, Object>> convertEnumToMap(final Class<? extends Enum<?>> enumClass) {
        if (enumClass == null) {
            return null;
        }

        final Map<String, Map<String, Object>> result = new LinkedHashMap<>();
        final Enum<?>[]                        values = enumClass.getEnumConstants();

        try {
            for (final Enum<?> enumItem : values) {
                final Map<String, Object> enumFields;
                enumFields = extractEnumFields(enumItem);
                result.put(enumItem.name(), enumFields);
            }
        } catch (final IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }


        return result;
    }

    private static Map<String, Object> extractEnumFields(final Enum<?> enumItem) throws IllegalAccessException {
        final Map<String, Object> result = new LinkedHashMap<>();

        final Field[] fields = enumItem.getDeclaringClass().getDeclaredFields();
        for (final Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                result.put(field.getName(), field.get(enumItem));
            }
        }
        return result;
    }

}
