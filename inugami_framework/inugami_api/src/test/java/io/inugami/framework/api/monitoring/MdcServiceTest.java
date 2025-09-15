package io.inugami.framework.api.monitoring;

import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.monitoring.MdcServiceSpi;
import io.inugami.framework.interfaces.monitoring.MdcServiceSpiFactory;
import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
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

}