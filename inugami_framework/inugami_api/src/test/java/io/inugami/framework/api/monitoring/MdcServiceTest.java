package io.inugami.framework.api.monitoring;

import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.monitoring.MdcServiceSpi;
import io.inugami.framework.interfaces.monitoring.MdcServiceSpiFactory;
import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
import io.inugami.framework.interfaces.monitoring.models.IoInfoDTO;
import io.inugami.framework.interfaces.spi.JavaSpiLoaderServiceSPI;
import io.inugami.framework.interfaces.spi.SpiLoader;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MdcServiceTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String SEP   = "_";
    private static final String EMPTY = "";

    private static final LocalDateTime datetime       = LocalDateTime.of(2023, 3, 30, 22, 24);
    private static final String        DEFAULT_STRING = "XXXX";

    // =========================================================================
    // TEST KEYS
    // =========================================================================
    @Test
    void load() {

        final ServiceLoader<MdcServiceSpi> spi = (ServiceLoader<MdcServiceSpi>) ServiceLoader.load(MdcServiceSpi.class);
        assertThat(spi.findFirst()).isPresent();
        assertThat(new JavaSpiLoaderServiceSPI().loadServices(MdcServiceSpi.class)).isNotNull();

        final var service = SpiLoader.getInstance().loadSpiSingleServicesByPriority(MdcServiceSpi.class);
        assertThat(service).isNotNull();
    }

    @Test
    void mdcKeys_shouldHaveGetterAndSetter() {
        final Method[] methods = MdcService.class.getDeclaredMethods();
        for (final MDCKeys key : MDCKeys.VALUES) {
            verify(key, methods);
        }
    }

    private void verify(final MDCKeys key, final Method[] methods) {
        switch (key) {
            case partnerResponseCharset:
            case partnerRequestCharset:
                return;
        }

        final Method getter = searchGetter(key, methods);
        final Method setter = searchSetter(key, methods);

        Asserts.assertNotNull("getter not found for " + key.name(), getter);
        Asserts.assertNotNull("setter not found for " + key.name(), setter);
        
        MdcServiceSpiFactory.getInstance().clear();

        final Object value = createValue(setter);
        Asserts.assertNotNull("no value for " + key.name(), value);

        try {
            if (setter.getParameters()[0].getType().getName().equals("[Ljava.lang.String;")) {
                setter.invoke(MdcServiceSpiFactory.getInstance(), new Object[]{new String[]{DEFAULT_STRING}});
            } else {
                setter.invoke(MdcServiceSpiFactory.getInstance(), new Object[]{value});
            }

        } catch (final Throwable e) {
            throw new UncheckedException("unable to set value for " + key.name());
        }

        Object newValue = null;
        try {
            newValue = getter.invoke(MdcServiceSpiFactory.getInstance(), new Object[]{});
        } catch (final Throwable e) {
            throw new UncheckedException("unable to get value for " + key.name());
        }
        Asserts.assertNotNull("value isn't define for " + key.name(), newValue);

        switch (key) {
            case errorCode:
            case partnerResponseCharset:
            case partnerRequestCharset:
            case status:
                break;
            default:
                Asserts.assertEquals("value isn't define for " + key.name(), value, newValue);
        }

    }


    private Method searchGetter(final MDCKeys key, final Method[] methods) {
        Method       method     = null;
        final String methodName = buildMethodName(key.name());

        for (final Method currentMethod : methods) {
            if (currentMethod.getName().equals(methodName) && currentMethod.getParameters().length == 0) {
                method = currentMethod;
                break;
            }
        }
        return method;
    }


    private Method searchSetter(final MDCKeys key, final Method[] methods) {
        Method       method     = null;
        final String methodName = buildMethodName(key.name());

        for (final Method currentMethod : methods) {
            if (currentMethod.getName().equals(methodName) && currentMethod.getParameters().length == 1) {
                method = currentMethod;
                break;
            }
        }
        return method;
    }


    private String buildMethodName(final String name) {
        if (!name.contains(SEP)) {
            return name;
        }
        final String[]     part   = name.split(SEP);
        final List<String> values = new ArrayList<>();
        values.add(part[0]);

        for (int i = 1; i < part.length; i++) {
            values.add(part[i].substring(0, 1).toUpperCase() + part[i].substring(1));
        }

        return String.join(EMPTY, values);
    }

    private Object createValue(final Method setter) {
        final Parameter parameter = setter.getParameters()[0];

        if (String.class.isAssignableFrom(parameter.getType()) ||
            "[Ljava.lang.String;".equals(parameter.getType().getName())) {
            return DEFAULT_STRING;
        } else if (Charset.class.isAssignableFrom(parameter.getType())) {
            return StandardCharsets.UTF_8;
        } else if (Integer.class.isAssignableFrom(parameter.getType()) || parameter.getType() == int.class) {
            return Integer.valueOf(1);
        } else if (Long.class.isAssignableFrom(parameter.getType()) || parameter.getType() == long.class) {
            return Long.valueOf(1L);
        } else if (Double.class.isAssignableFrom(parameter.getType()) || parameter.getType() == double.class) {
            return Double.valueOf(1.0);
        } else if (Boolean.class.isAssignableFrom(parameter.getType()) || parameter.getType() == boolean.class) {
            return Boolean.TRUE;
        } else if (LocalDateTime.class.isAssignableFrom(parameter.getType())) {
            return datetime;
        } else if (ErrorCode.class.isAssignableFrom(parameter.getType())) {
            return DefaultErrorCode.buildUndefineErrorCode()
                                   .category(DEFAULT_STRING)
                                   .url(DEFAULT_STRING)
                                   .message(DEFAULT_STRING)
                                   .messageDetail(DEFAULT_STRING)
                                   .statusCode(500)
                                   .retryable(true)
                                   .exploitationError()
                                   .field(DEFAULT_STRING)
                                   .rollback(true)
                                   .build();
        }


        return null;
    }

    // =========================================================================
    // Set MDC
    // =========================================================================
    @Test
    void setMdc_withNullValue_shouldRemove() {
        final MdcServiceSpi mdc = MdcServiceSpiFactory.getInstance().clear();

        mdc.callFrom(null);
        assertThat(mdc.callFrom()).isNull();

        mdc.callFrom("joe");
        mdc.callFrom("null");
        assertThat(mdc.callFrom()).isNull();
    }

    // =========================================================================
    // ADDITIONAL COVERAGE TESTS
    // =========================================================================

    @Test
    void should_handle_conversions_and_edge_cases() {
        final MdcServiceSpi mdc = MdcServiceSpiFactory.getInstance().clear();

        // Test des types numériques et parsing
        mdc.setMdc("int_val", "123");
        mdc.setMdc("long_val", "456");
        mdc.setMdc("double_val", "78.9");
        mdc.setMdc("bool_val", "true");

        assertThat(mdc.getInt("int_val")).isEqualTo(123);
        assertThat(mdc.getLong("long_val")).isEqualTo(456L);
        assertThat(mdc.getDouble("double_val")).isEqualTo(78.9);
        assertThat(mdc.getBoolean("bool_val")).isTrue();

        // Test des valeurs invalides (doit retourner 0 ou default sans crash)
        mdc.setMdc("invalid_int", "not_a_number");
        assertThat(mdc.getInt("invalid_int")).isZero();
        assertThat(mdc.getDouble("invalid_int")).isZero();
    }

    @Test
    void should_handle_charset_and_dates() {
        final MdcServiceSpi mdc = MdcServiceSpiFactory.getInstance().clear();

        // Charset
        mdc.setMdc("my_charset", "UTF-16");
        assertThat(mdc.getCharset("my_charset")).isEqualTo(StandardCharsets.UTF_16);

        mdc.setMdc("bad_charset", "UNKNOWN_CHARSET");
        assertThat(mdc.getCharset("bad_charset")).isEqualTo(StandardCharsets.UTF_8);

        // Date / LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        mdc.setMdc("my_date", now);
        // On vérifie que le getter typé arrive à relire ce que le setter a écrit
        assertThat(mdc.getLocalDateTime("my_date")).isNotNull();
    }

    @Test
    void should_manage_tuples_and_collections() {
        final MdcService mdc = MdcService.getInstance().clear();

        // Test addMdc avec des Tuples
        mdc.addMdc(new io.inugami.framework.interfaces.models.Tuple<>("key1", "val1"),
                   new io.inugami.framework.interfaces.models.Tuple<>("key2", 100));

        assertThat(mdc.getMdc("key1")).isEqualTo("val1");
        assertThat(mdc.getInt("key2")).isEqualTo(100);

        // Test removal
        mdc.remove("key1", "key2");
        assertThat(mdc.getMdc("key1")).isNull();
    }

    @Test
    void should_handle_all_mdc_and_extended_maps() {
        final MdcService mdc = MdcService.getInstance().clear();
        mdc.setMdc("b_key", "val_b");
        mdc.setMdc("a_key", "val_a");

        // Vérification du tri (LinkedHashMap + Collections.sort)
        java.util.Map<String, String> all = mdc.getAllMdc();
        assertThat(all.keySet().iterator().next()).isEqualTo("a_key");

        // Extended (sollicite les mdcMappers chargés par SPI)
        java.util.Map<String, java.io.Serializable> extended = mdc.getAllMdcExtended();
        assertThat(extended).isNotNull();
    }

    @Test
    void should_handle_global_status_shortcuts() {
        final MdcService mdc = MdcService.getInstance().clear();

        mdc.globalStatusSuccess();
        assertThat(mdc.globalStatus()).isEqualTo("success");

        mdc.globalStatusError();
        assertThat(mdc.globalStatus()).isEqualTo("error");

        mdc.removeGlobalStatus();
        assertThat(mdc.globalStatus()).isNull();
    }

    @Test
    void should_handle_ioinfo_dtos() {
        final MdcService mdc = MdcService.getInstance().clear();
        final var info = IoInfoDTO.builder()
                .url("http://service.com")
                .method("POST")
                .duration(500L)
                .build();

        mdc.ioinfoIoLog(info);
        assertThat(mdc.getMdc(MDCKeys.url)).isEqualTo("http://service.com");
        assertThat(mdc.duration()).isEqualTo(500L);

        mdc.removeIoinfoIoLog();
        assertThat(mdc.getMdc(MDCKeys.url)).isNull();
    }

}