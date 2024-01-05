package io.inugami.framework.api.tools;

import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.interfaces.feature.Feature;
import io.inugami.framework.interfaces.spi.SpiPriority;
import io.inugami.framework.interfaces.tools.reflection.FieldGetterSetter;
import lombok.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertTextRelative;
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
        final SpiPriority result = ReflectionUtils.getAnnotation(SomeService.class, SpiPriority.class);
        assertThat(result).isNotNull().isNotEqualTo(SpiPriority.class);

        Assertions.assertThat(ReflectionUtils.getAnnotation(SomeService.class, SpiPriority.class)).isNotNull();
        Assertions.assertThat(ReflectionUtils.getAnnotation(SomeService.class, Deprecated.class)).isNull();
        Assertions.assertThat(ReflectionUtils.getAnnotation(null, Deprecated.class)).isNull();
    }

    @Test
    void searchAnnotation_nominal() {
        final Annotation annotation = ReflectionUtils.searchAnnotation(SomeService.class.getAnnotations(), SpiPriority.class.getName());
        assertThat(annotation).isNotNull();

        Assertions.assertThat(ReflectionUtils.searchAnnotation(SomeService.class.getAnnotations(), "Joe")).isNull();
    }

    @Test
    void hasAnnotation_nominal() {
        Assertions.assertThat(ReflectionUtils.hasAnnotation(SomeService.class, SpiPriority.class)).isTrue();
        Assertions.assertThat(ReflectionUtils.hasAnnotation(SomeService.class, Deprecated.class)).isFalse();
        Assertions.assertThat(ReflectionUtils.hasAnnotation(null, Deprecated.class)).isFalse();
        Assertions.assertThat(ReflectionUtils.hasAnnotation(SomeService.class, null)).isFalse();
    }

    @Test
    void ifHasAnnotation_nominal() {
        List<String> result = new ArrayList<>();

        ReflectionUtils.ifHasAnnotation(SomeService.class, SpiPriority.class, c -> result.add("SpiPriority"));
        ReflectionUtils.ifHasAnnotation(SomeService.class, Deprecated.class, c -> result.add("Deprecated"));
        ReflectionUtils.ifHasAnnotation(null, Deprecated.class, c -> result.add("DeprecatedNull"));

        assertThat(result).hasSize(1).hasToString("[SpiPriority]");
    }

    @Test
    void ifHasAnnotation_withDefault() {
        List<String> result = new ArrayList<>();

        ReflectionUtils.ifHasAnnotation(SomeService.class, SpiPriority.class, c -> result.add("SpiPriority"), () -> "SpiPriorityDefault");
        ReflectionUtils.ifHasAnnotation(SomeService.class, Deprecated.class, c -> result.add("Deprecated"), () -> "DeprecatedDefault");
        ReflectionUtils.ifHasAnnotation(null, Deprecated.class, c -> result.add("DeprecatedNull"), () -> "DeprecatedNullDefault");

        assertThat(result).hasSize(1).hasToString("[SpiPriority]");
    }

    @Test
    void processOnAnnotation_nominal() {
        List<String> result = new ArrayList<>();

        ReflectionUtils.processOnAnnotation(SomeService.class, SpiPriority.class, a -> result.add(String.valueOf(a.value())));
        ReflectionUtils.processOnAnnotation(SomeService.class, SpiPriority.class, null);
        ReflectionUtils.processOnAnnotation(SomeService.class, null, a -> result.add("nullValue"));
        ReflectionUtils.processOnAnnotation(null, SpiPriority.class, a -> result.add("nullService"));

        assertThat(result).hasToString("[1]");
    }

    @Test
    void searchAnnotationInInterface_withMethod() {
        final Feature case1 = ReflectionUtils.searchAnnotationInInterface(ReflectionUtils.searchMethodByName(Clientmpl.class, "retrieveInfo"), Feature.class);
        assertThat(case1).isNotNull();
        assertThat(case1.value()).isEqualTo("feature_retrieveInfo");

        final Feature case2 = ReflectionUtils.getAnnotation(ReflectionUtils.searchMethodByName(Clientmpl.class, "retrieveInfo"), Feature.class);
        assertThat(case2).isNotNull();
        assertThat(case2.value()).isEqualTo("feature_retrieveInfo");
    }

    @Test
    void searchAnnotationInInterface_withClass() {
        final BasicAnnotation case1 = ReflectionUtils.searchAnnotationInInterface(Clientmpl.class, BasicAnnotation.class);
        assertThat(case1).isNotNull();
        assertThat(case1.value()).isEqualTo("partner");

        final BasicAnnotation case2 = ReflectionUtils.getAnnotation(Clientmpl.class, BasicAnnotation.class);
        assertThat(case2).isNotNull();
        assertThat(case2.value()).isEqualTo("partner");
    }

    // =================================================================================================================
    // enum
    // =================================================================================================================
    @Test
    void getEnumValues_nominal() {
        assertTextRelative(ReflectionUtils.getEnumValues(Levels.class), "io/inugami/framework/api/tools/reflectionUtilsTest/getEnumValues_nominal.json");

        Assertions.assertThat(ReflectionUtils.getEnumValues(null)).isEmpty();
    }


    // =================================================================================================================
    // CLASS
    // =================================================================================================================
    @Test
    void scan_nominal() {
        assertTextRelative(splitResult(ReflectionUtils.scan("io.inugami.api.tools",
                                                            this.getClass().getClassLoader(),
                                                            objClass -> objClass.getSimpleName().endsWith("Test"))),
                           "io/inugami/framework/api/tools/reflectionUtilsTest/scan_nominal.txt");

        assertTextRelative(splitResult(ReflectionUtils.scan("io.inugami.api.tools",
                                                            this.getClass().getClassLoader())),
                           "io/inugami/framework/api/tools/reflectionUtilsTest/scan_withoutFilter.txt");

    }

    @Test
    void scanAllFiles_withFileNotExists() {
        Assertions.assertThat(ReflectionUtils.scanAllFiles(new File("/some/path"), List.of())).isEmpty();
    }

    @Test
    void chooseBase_nominal() {
        Assertions.assertThat(ReflectionUtils.chooseBase("/dev/workspaces/app/io/inugami/Service.class", List.of("/dev/workspaces/app")))
                  .isEqualTo("/dev/workspaces/app");
        Assertions.assertThat(ReflectionUtils.chooseBase("/dev/workspaces/app/io/inugami/Service.class", List.of("/other/workspaces/app")))
                  .isEmpty();
    }


    @Test
    void scanJar_nominal() throws IOException, URISyntaxException {
        final URL jarUrl = UnitTestHelper.buildTestFilePath("inugami-springboot-demo-user-webapp.jar")
                                         .toURI()
                                         .toURL();
        final List<String> result = ReflectionUtils.scanJar(jarUrl);
        assertTextRelative(String.join("\n", result), "io/inugami/framework/api/tools/reflectionUtilsTest/scanJar_nominal.txt");
    }

    // =================================================================================================================
    // FIELDS
    // =================================================================================================================
    @Test
    void setFieldValue_nominal() {
        final Field       field    = ReflectionUtils.getField(FIELD_NAME, SomeService.class);
        final SomeService instance = SomeService.builder().build();
        final String      result   = ReflectionUtils.setFieldValue(field, HELLO, instance).getName();
        assertThat(result).isEqualTo(HELLO);
    }

    @Test
    void getStaticFieldValue_nominal() {
        Assertions.assertThat(ReflectionUtils.getStaticFieldValue(SATIC_FIELD_NAME, SomeService.class))
                  .isEqualTo("service");
        Assertions.assertThat(ReflectionUtils.getStaticFieldValue(null, SomeService.class)).isNull();
        Assertions.assertThat(ReflectionUtils.getStaticFieldValue(SATIC_FIELD_NAME, null)).isNull();
    }

    @Test
    void getFieldValue_nominal() {
        Assertions.assertThat(ReflectionUtils.getFieldValue(FIELD_NAME, SomeService.builder().name(HELLO).build()))
                  .isEqualTo(HELLO);
        Assertions.assertThat(ReflectionUtils.getFieldValue(null, SomeService.builder().name(HELLO).build())).isNull();
        Assertions.assertThat(ReflectionUtils.getFieldValue(FIELD_NAME, null)).isNull();
    }

    @Test
    void getField_nominal() {
        Assertions.assertThat(ReflectionUtils.getField(FIELD_NAME, SomeService.builder().name(HELLO).build()))
                  .isNotNull();
        Assertions.assertThat(ReflectionUtils.getField("joe", SomeService.builder().name(HELLO).build())).isNull();
        Assertions.assertThat(ReflectionUtils.getField(null, SomeService.builder().name(HELLO).build())).isNull();
        Assertions.assertThat(ReflectionUtils.getField(FIELD_NAME, null)).isNull();
    }

    @Test
    void getField_withClass() {
        Assertions.assertThat(ReflectionUtils.getField(FIELD_NAME, SomeService.class)).isNotNull();
        Assertions.assertThat(ReflectionUtils.getField("joe", SomeService.class)).isNull();
        Assertions.assertThat(ReflectionUtils.getField(null, SomeService.class)).isNull();
        Assertions.assertThat(ReflectionUtils.getField(FIELD_NAME, null)).isNull();
    }

    @Test
    void getAllFields_nominal() {
        Assertions.assertThat(ReflectionUtils.getAllFields(SomeService.class))
                  .hasToString("[private static final java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.TYPE, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.name, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$ParentService.url]");
        Assertions.assertThat(ReflectionUtils.getAllFields(Object.class)).isEmpty();
        Assertions.assertThat(ReflectionUtils.getAllFields(null)).isEmpty();
    }


    @Test
    void loadAllFields_nominal() {
        Assertions.assertThat(ReflectionUtils.loadAllFields(SomeService.class))
                  .hasToString("[private static final java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.TYPE, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.name, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$ParentService.url]");
        Assertions.assertThat(ReflectionUtils.loadAllFields(Object.class)).isEmpty();
        Assertions.assertThat(ReflectionUtils.loadAllFields(null)).isEmpty();
    }

    @Test
    void loadAllStaticFields_nominal() {
        Assertions.assertThat(ReflectionUtils.loadAllStaticFields(SomeService.class))
                  .hasToString("[private static final java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.TYPE]");
        Assertions.assertThat(ReflectionUtils.loadAllStaticFields(Object.class)).isEmpty();
        Assertions.assertThat(ReflectionUtils.loadAllStaticFields(null)).isEmpty();
    }

    @Test
    void extractParentsFields_nominal() {
        Assertions.assertThat(ReflectionUtils.extractParentsFields(SomeService.class))
                  .hasToString("[private static final java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.TYPE, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.name, private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$ParentService.url]");
        Assertions.assertThat(ReflectionUtils.extractParentsFields(Object.class)).isEmpty();
        Assertions.assertThat(ReflectionUtils.extractParentsFields(null)).isEmpty();
    }


    @Test
    void extractFieldGetterAndSetter_nominal() {
        final List<FieldGetterSetter> result = ReflectionUtils.extractFieldGetterAndSetter(SomeService.builder()
                                                                                                      .name("Joe")
                                                                                                      .build());
        assertThat(String.join("\n", result.stream()
                                           .map(v -> v.toString())
                                           .collect(Collectors.toList())))
                .hasToString("FieldGetterSetter(field=private java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.name, value=Joe, getter=public java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.getName(), setter=public void io.inugami.api.tools.ReflectionUtilsTest$SomeService.setName(java.lang.String))");
        Assertions.assertThat(ReflectionUtils.extractFieldGetterAndSetter(SomeService.builder().build()))
                  .hasToString("[]");
        Assertions.assertThat(ReflectionUtils.extractFieldGetterAndSetter(null)).hasToString("[]");
    }

    @Test
    void extractGetters_nominal() {
        Assertions.assertThat(ReflectionUtils.extractGetters(SomeService.builder()
                                                                        .name("Joe")
                                                                        .build()))
                  .hasToString("[public java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.getName()]");
        Assertions.assertThat(ReflectionUtils.extractGetters(SomeService.builder()
                                                                        .build()))
                  .hasToString("[public java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.getName()]");
        Assertions.assertThat(ReflectionUtils.extractGetters(null)).hasToString("[]");
    }


    @Test
    void sortMethods_nominal() {
        final List<Method> methods = new ArrayList<>(Arrays.asList(SomeService.class.getMethods()));
        ReflectionUtils.sortMethods(methods);
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
        ReflectionUtils.sortMethods(null);
    }


    @Test
    void callGetterForField_nominal() {
        final SomeService instance = SomeService.builder().name(HELLO).build();
        Assertions.assertThat(ReflectionUtils.callGetterForField(FIELD_NAME, instance)).hasToString(HELLO);

        Assertions.assertThat(ReflectionUtils.callGetterForField(null, instance)).isNull();
        Assertions.assertThat(ReflectionUtils.callGetterForField(FIELD_NAME, null)).isNull();
    }

    @Test
    void setFieldValue_withValue() {
        final SomeService instance = SomeService.builder().name(HELLO).build();
        final Field       field    = ReflectionUtils.getField(FIELD_NAME, SomeService.class);

        ReflectionUtils.setFieldValue(field, "Joe", instance);
        assertThat(instance.getName()).isEqualTo("Joe");

        try {
            ReflectionUtils.setFieldValue(field, 42L, instance);
        } catch (Throwable e) {

        }
        ReflectionUtils.setFieldValue(null, "Joe", instance);
        ReflectionUtils.setFieldValue(field, "Joe", null);
    }

    // =================================================================================================================
    // CONSTRUCTORS
    // =================================================================================================================
    @Test
    void loadAllConstructors_nominal() {
        Assertions.assertThat(ReflectionUtils.loadAllConstructors(SomeService.class))
                  .hasToString("[public io.inugami.api.tools.ReflectionUtilsTest$SomeService(java.lang.String), io.inugami.api.tools.ReflectionUtilsTest$ParentService()]");
        Assertions.assertThat(ReflectionUtils.loadAllConstructors(null)).isEmpty();
    }


    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    @Test
    void loadAllMethods_nominal() {
        Assertions.assertThat(ReflectionUtils.loadAllMethods(SomeService.class)).isNotEmpty();
        Assertions.assertThat(ReflectionUtils.loadAllMethods(null)).isEmpty();
    }

    @Test
    void searchMethod_nominal() {
        Assertions.assertThat(ReflectionUtils.searchMethod(SomeService.class, "getName"))
                  .hasToString("public java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.getName()");
        Class<?> objClass = null;
        Assertions.assertThat(ReflectionUtils.searchMethod(objClass, "getName")).isNull();
        Assertions.assertThat(ReflectionUtils.searchMethod(SomeService.class, null)).isNull();
    }

    @Test
    void searchMethod_withAnnotation() {
        Assertions.assertThat(ReflectionUtils.searchMethod(ReflectionUtils.searchAnnotation(SomeService.class.getAnnotations(), SpiPriority.class.getName()), "value"))
                  .hasToString("public abstract int io.inugami.interfaces.spi.SpiPriority.value()");
        Annotation annotation = null;
        Assertions.assertThat(ReflectionUtils.searchMethod(annotation, "getName")).isNull();
        Assertions.assertThat(ReflectionUtils.searchMethod(ReflectionUtils.searchAnnotation(SomeService.class.getAnnotations(), SpiPriority.class.getName()), null))
                  .isNull();
    }

    @Test
    void searchMethodByName_nominal() {
        Assertions.assertThat(ReflectionUtils.searchMethodByName(SomeService.class, "getName"))
                  .hasToString("public java.lang.String io.inugami.api.tools.ReflectionUtilsTest$SomeService.getName()");
        Class<?> objClass = null;
        Assertions.assertThat(ReflectionUtils.searchMethodByName(objClass, "getName")).isNull();
        Assertions.assertThat(ReflectionUtils.searchMethodByName(SomeService.class, null)).isNull();
    }

    // =================================================================================================================
    // ASSERTS ENUM
    // =================================================================================================================
    @Test
    void assertEnum_nominal() {
        Assertions.assertThat(ReflectionUtils.convertEnumToMap(Levels.class))
                  .hasToString("{ADMIN={label=admin, level=10}, GUEST={label=guest, level=0}}");
    }

    @Test
    void assertEnum_nullValue() {
        Assertions.assertThat(ReflectionUtils.convertEnumToMap(null)).isEmpty();
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
    enum Levels {
        ADMIN("admin", 10),
        GUEST("guest", 0);

        private final String label;
        private final int    level;
    }

    @BasicAnnotation("partner")
    interface ParentClient {
        @Feature("feature_retrieveInfo")
        String retrieveInfo();
    }

    interface Client extends ParentClient {

    }


    static class Clientmpl implements Client {

        @Override
        public String retrieveInfo() {
            return null;
        }
    }


    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface BasicAnnotation {
        String value() default "";
    }

}