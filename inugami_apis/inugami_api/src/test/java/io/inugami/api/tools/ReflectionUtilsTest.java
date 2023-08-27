package io.inugami.api.tools;

import io.inugami.api.spi.SpiPriority;
import io.inugami.api.tools.unit.test.UnitTestHelper;
import lombok.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static io.inugami.api.tools.ReflectionUtils.*;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertTextRelative;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"java:S5845"})
class ReflectionUtilsTest {

    public static final String HELLO            = "hello";
    public static final String FIELD_NAME       = "name";
    public static final String SATIC_FIELD_NAME = "TYPE";

    // =================================================================================================================
    // UTILITY
    // =================================================================================================================
    @Test
    void utilityClass() {
        UnitTestHelper.assertUtilityClassLombok(ReflectionUtils.class);
    }

    // =================================================================================================================
    // ANNOTATION
    // =================================================================================================================
    @Test
    void getAnnotation_nominal() {
        final SpiPriority result = getAnnotation(SomeService.class, SpiPriority.class);
        assertThat(result).isNotNull().isNotEqualTo(SpiPriority.class);

        assertThat(getAnnotation(SomeService.class, SpiPriority.class)).isNotNull();
        assertThat(getAnnotation(SomeService.class, Deprecated.class)).isNull();
        assertThat(getAnnotation(null, Deprecated.class)).isNull();
    }

    @Test
    void searchAnnotation_nominal() {
        final Annotation annotation = searchAnnotation(SomeService.class.getAnnotations(), SpiPriority.class.getName());
        assertThat(annotation).isNotNull();

        assertThat(searchAnnotation(SomeService.class.getAnnotations(), "Joe")).isNull();
    }

    @Test
    void hasAnnotation_nominal() {
        assertThat(hasAnnotation(SomeService.class, SpiPriority.class)).isTrue();
        assertThat(hasAnnotation(SomeService.class, Deprecated.class)).isFalse();
        assertThat(hasAnnotation(null, Deprecated.class)).isFalse();
        assertThat(hasAnnotation(SomeService.class, null)).isFalse();
    }

    @Test
    void ifHasAnnotation_nominal() {
        List<String> result = new ArrayList<>();

        ifHasAnnotation(SomeService.class, SpiPriority.class, c -> result.add("SpiPriority"));
        ifHasAnnotation(SomeService.class, Deprecated.class, c -> result.add("Deprecated"));
        ifHasAnnotation(null, Deprecated.class, c -> result.add("DeprecatedNull"));

        assertThat(result).hasSize(1).hasToString("[SpiPriority]");
    }

    @Test
    void ifHasAnnotation_withDefault() {
        List<String> result = new ArrayList<>();

        ifHasAnnotation(SomeService.class, SpiPriority.class, c -> result.add("SpiPriority"), () -> "SpiPriorityDefault");
        ifHasAnnotation(SomeService.class, Deprecated.class, c -> result.add("Deprecated"), () -> "DeprecatedDefault");
        ifHasAnnotation(null, Deprecated.class, c -> result.add("DeprecatedNull"), () -> "DeprecatedNullDefault");

        assertThat(result).hasSize(1).hasToString("[SpiPriority]");
    }

    @Test
    void processOnAnnotation_nominal() {
        List<String> result = new ArrayList<>();

        processOnAnnotation(SomeService.class, SpiPriority.class, a -> result.add(String.valueOf(a.value())));
        processOnAnnotation(SomeService.class, SpiPriority.class, null);
        processOnAnnotation(SomeService.class, null, a -> result.add("nullValue"));
        processOnAnnotation(null, SpiPriority.class, a -> result.add("nullService"));

        assertThat(result).hasToString("[1]");
    }


    // =================================================================================================================
    // enum
    // =================================================================================================================
    @Test
    void getEnumValues_nominal() {
        assertTextRelative(getEnumValues(Levels.class), "api/tools/reflectionUtilsTest/getEnumValues_nominal.json");

        assertThat(getEnumValues(null)).isEmpty();
    }


    // =================================================================================================================
    // CLASS
    // =================================================================================================================
    @Test
    void scan_nominal() {
        assertTextRelative(splitResult(scan("io.inugami.api.tools",
                                            this.getClass().getClassLoader(),
                                            objClass -> objClass.getSimpleName().endsWith("Test"))),
                           "api/tools/reflectionUtilsTest/scan_nominal.txt");

        assertTextRelative(splitResult(scan("io.inugami.api.tools",
                                            this.getClass().getClassLoader())),
                           "api/tools/reflectionUtilsTest/scan_withoutFilter.txt");

    }

    @Test
    void scanAllFiles_withFileNotExists() {
        assertThat(scanAllFiles(new File("/some/path"), List.of())).isEmpty();
    }

    @Test
    void chooseBase_nominal() {
        assertThat(chooseBase("/dev/workspaces/app/io/inugami/Service.class", List.of("/dev/workspaces/app"))).isEqualTo("/dev/workspaces/app");
        assertThat(chooseBase("/dev/workspaces/app/io/inugami/Service.class", List.of("/other/workspaces/app"))).isEmpty();
    }


    @Test
    void scanJar_nominal() throws IOException, URISyntaxException {
        final URL jarUrl = UnitTestHelper.buildTestFilePath("inugami-springboot-demo-user-webapp.jar")
                                         .toURI()
                                         .toURL();
        final List<String> result = scanJar(jarUrl);
        assertTextRelative(String.join("\n", result), "api/tools/reflectionUtilsTest/scanJar_nominal.txt");
    }

    // =================================================================================================================
    // FIELDS
    // =================================================================================================================
    @Test
    void setFieldValue_nominal() {
        final Field       field    = getField(FIELD_NAME, SomeService.class);
        final SomeService instance = SomeService.builder().build();
        final String      result   = setFieldValue(field, HELLO, instance).getName();
        assertThat(result).isEqualTo(HELLO);
    }

    @Test
    void getStaticFieldValue_nominal() {
        assertThat(getStaticFieldValue(SATIC_FIELD_NAME, SomeService.class)).isEqualTo("service");
        assertThat(getStaticFieldValue(null, SomeService.class)).isNull();
        assertThat(getStaticFieldValue(SATIC_FIELD_NAME, null)).isNull();
    }

    @Test
    void getFieldValue_nominal() {
        assertThat(getFieldValue(FIELD_NAME, SomeService.builder().name(HELLO).build())).isEqualTo(HELLO);
        assertThat(getFieldValue(null, SomeService.builder().name(HELLO).build())).isNull();
        assertThat(getFieldValue(FIELD_NAME, null)).isNull();
    }

    @Test
    void getField_nominal() {
        assertThat(getField(FIELD_NAME, SomeService.builder().name(HELLO).build())).isNotNull();
        assertThat(getField("joe", SomeService.builder().name(HELLO).build())).isNull();
        assertThat(getField(null, SomeService.builder().name(HELLO).build())).isNull();
        assertThat(getField(FIELD_NAME, null)).isNull();
    }

    @Test
    void getField_withClass() {
        assertThat(getField(FIELD_NAME, SomeService.class)).isNotNull();
        assertThat(getField("joe", SomeService.class)).isNull();
        assertThat(getField(null, SomeService.class)).isNull();
        assertThat(getField(FIELD_NAME, null)).isNull();
    }

    @Test
    void getAllFields_nominal() {
        assertThat(getAllFields(SomeService.class)).hasToString("[private static final java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.TYPE, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.name, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$ParentService.url]");
        assertThat(getAllFields(Object.class)).isEmpty();
        assertThat(getAllFields(null)).isEmpty();
    }


    @Test
    void loadAllFields_nominal() {
        assertThat(loadAllFields(SomeService.class)).hasToString("[private static final java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.TYPE, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.name, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$ParentService.url]");
        assertThat(loadAllFields(Object.class)).isEmpty();
        assertThat(loadAllFields(null)).isEmpty();
    }

    @Test
    void loadAllStaticFields_nominal() {
        assertThat(loadAllStaticFields(SomeService.class)).hasToString("[private static final java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.TYPE]");
        assertThat(loadAllStaticFields(Object.class)).isEmpty();
        assertThat(loadAllStaticFields(null)).isEmpty();
    }

    @Test
    void extractParentsFields_nominal() {
        assertThat(extractParentsFields(SomeService.class)).hasToString("[private static final java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.TYPE, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.name, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$ParentService.url]");
        assertThat(extractParentsFields(Object.class)).isEmpty();
        assertThat(extractParentsFields(null)).isEmpty();
    }


    @Test
    void buildField_nominal() {
        final Field field = buildField(SomeService.class, FIELD_NAME);
        assertThat(field).isNotNull();
        assertThat(field.getName()).isEqualTo(FIELD_NAME);
    }


    @Test
    void extractFieldGetterAndSetter_nominal() {
        final List<FieldGetterSetter> result = extractFieldGetterAndSetter(SomeService.builder()
                                                                                      .name("Joe")
                                                                                      .build());
        assertThat(String.join("\n", result.stream()
                                           .map(v -> v.toString())
                                           .collect(Collectors.toList())))
                .hasToString("FieldGetterSetter(field=private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.name, value=Joe, getter=public java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.getName(), setter=public void io.inugami.api.tools.ReflectionUtilsTest$SomeService.setName(java.lang.String))");
        assertThat(extractFieldGetterAndSetter(SomeService.builder().build())).hasToString("[]");
        assertThat(extractFieldGetterAndSetter(null)).hasToString("[]");
    }

    @Test
    void extractGetters_nominal() {
        assertThat(extractGetters(SomeService.builder()
                                             .name("Joe")
                                             .build())).hasToString("[public java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.getName()]");
        assertThat(extractGetters(SomeService.builder()
                                             .build())).hasToString("[public java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.getName()]");
        assertThat(extractGetters(null)).hasToString("[]");
    }


    @Test
    void sortMethods_nominal() {
        final List<Method> methods = new ArrayList<>(Arrays.asList(SomeService.class.getMethods()));
        sortMethods(methods);
        assertThat(String.join("\n", methods.stream().map(m -> m.getName()).collect(Collectors.toList())))
                .hasToString("builder\n" +
                             "equals\n" +
                             "getClass\n" +
                             "getName\n" +
                             "getUrl\n" +
                             "hashCode\n" +
                             "notify\n" +
                             "notifyAll\n" +
                             "setName\n" +
                             "setUrl\n" +
                             "toString\n" +
                             "wait\n" +
                             "wait\n" +
                             "wait");
        sortMethods(null);
    }


    @Test
    void callGetterForField_nominal() {
        final SomeService instance = SomeService.builder().name(HELLO).build();
        assertThat(callGetterForField(FIELD_NAME, instance)).hasToString(HELLO);

        assertThat(callGetterForField(null, instance)).isNull();
        assertThat(callGetterForField(FIELD_NAME, null)).isNull();
    }

    @Test
    void setFieldValue_withValue() {
        final SomeService instance = SomeService.builder().name(HELLO).build();
        final Field       field    = getField(FIELD_NAME, SomeService.class);

        setFieldValue(field, "Joe", instance);
        assertThat(instance.getName()).isEqualTo("Joe");

        try {
            setFieldValue(field, 42L, instance);
        } catch (Throwable e) {

        }
        setFieldValue(null, "Joe", instance);
        setFieldValue(field, "Joe", null);
    }

    // =================================================================================================================
    // CONSTRUCTORS
    // =================================================================================================================
    @Test
    void loadAllConstructors_nominal() {
        assertThat(loadAllConstructors(SomeService.class)).hasToString("[public io.inugami.api.tools.ReflectionUtilsTest$SomeService(java.lang.String), io.inugami.api.tools.ReflectionUtilsTest$ParentService()]");
        assertThat(loadAllConstructors(null)).isEmpty();
    }


    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    @Test
    void loadAllMethods_nominal() {
        assertThat(loadAllMethods(SomeService.class)).isNotEmpty();
        assertThat(loadAllMethods(null)).isEmpty();
    }

    @Test
    void searchMethod_nominal() {
        assertThat(searchMethod(SomeService.class, "getName")).hasToString("public java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.getName()");
        Class<?> objClass = null;
        assertThat(searchMethod(objClass, "getName")).isNull();
        assertThat(searchMethod(SomeService.class, null)).isNull();
    }

    @Test
    void searchMethod_withAnnotation() {
        assertThat(searchMethod(searchAnnotation(SomeService.class.getAnnotations(), SpiPriority.class.getName()), "value")).hasToString("public abstract int io.inugami.api.spi.SpiPriority.value()");
        Annotation annotation = null;
        assertThat(searchMethod(annotation, "getName")).isNull();
        assertThat(searchMethod(searchAnnotation(SomeService.class.getAnnotations(), SpiPriority.class.getName()), null)).isNull();
    }

    @Test
    void searchMethodByName_nominal() {
        assertThat(searchMethodByName(SomeService.class, "getName")).hasToString("public java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.getName()");
        Class<?> objClass = null;
        assertThat(searchMethodByName(objClass, "getName")).isNull();
        assertThat(searchMethodByName(SomeService.class, null)).isNull();
    }

    // =================================================================================================================
    // ASSERTS ENUM
    // =================================================================================================================
    @Test
    void assertEnum_nominal() {
        assertThat(convertEnumToMap(Levels.class)).hasToString("{ADMIN={label=admin, level=10}, GUEST={label=guest, level=0}}");
    }

    @Test
    void assertEnum_nullValue() {
        assertThat(convertEnumToMap(null)).isEmpty();
    }

    // =================================================================================================================
    // INVOKE
    // =================================================================================================================


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private String splitResult(final Set<Class<?>> test) {
        return String.join("\n", Optional.ofNullable(test)
                                         .orElse(Set.of())
                                         .stream()
                                         .map(c -> c.getName())
                                         .collect(Collectors.toList()));
    }


    @Setter
    @Getter
    static class ParentService {
        private String url;
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    @SpiPriority(1)
    static class SomeService extends ParentService {
        private static final String TYPE = "service";
        private              String name;
    }


    @RequiredArgsConstructor
    public enum Levels {
        ADMIN("admin", 10),
        GUEST("guest", 0);

        private final String label;
        private final int    level;
    }


}