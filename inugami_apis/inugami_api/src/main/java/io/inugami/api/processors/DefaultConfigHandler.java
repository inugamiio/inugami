package io.inugami.api.processors;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.listeners.ApplicationLifecycleSPI;
import io.inugami.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.api.mapping.JsonUnmarshalling;
import io.inugami.api.spi.SpiLoader;
import io.inugami.api.tools.ConfigTemplateValues;
import io.inugami.api.tools.TemplateProviderSPI;

import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings({"java:S108", "java:S1168"})
public class DefaultConfigHandler implements ConfigHandler<String, String>, ApplicationLifecycleSPI {

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    private static final Map<String, String> PROPERTIES = new LinkedHashMap<>();
    private              TemplateProviderSPI template;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public DefaultConfigHandler() {
        template = SpiLoader.getInstance()
                            .loadSpiServiceByPriority(TemplateProviderSPI.class, new ConfigTemplateValues());
        DefaultApplicationLifecycleSPI.register(this);
        final Map<String, String> env = System.getenv();
        if (env != null) {
            for (final Map.Entry<String, String> entry : env.entrySet()) {
                PROPERTIES.put(entry.getKey(), entry.getValue());
            }
        }

        final Properties sysProperties = System.getProperties();
        sysProperties.forEach((key, value) -> PROPERTIES.put(String.valueOf(key), String.valueOf(value)));
    }

    @Override
    public void onContextRefreshed(final Object event) {
        template = SpiLoader.getInstance()
                            .loadSpiServiceByPriority(TemplateProviderSPI.class, new ConfigTemplateValues());
    }

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public String applyProperties(final String value) {
        return value == null ? null : template.applyProperties(value, this);
    }

    @Override
    public String grabOrDefault(final String key, final String defaultValue) {
        final String value  = PROPERTIES.get(key);
        final String result = value == null ? defaultValue : value;
        return applyProperties(result);
    }

    @Override
    public String grab(final String key) {
        return applyProperties(PROPERTIES.get(key));
    }

    @Override
    public String grab(final String message, final String key) {
        return grab(key);
    }

    @Override
    public Integer grab(final String key, final int defaultValue) {
        final String value  = grab(key);
        int          result = defaultValue;
        if (value != null) {
            try {
                result = Integer.parseInt(value);
            } catch (final Throwable e) {
            }
        }
        return result;
    }

    @Override
    public Integer grabInt(final String key) {
        final String value  = grab(key);
        Integer      result = null;
        if (value != null) {
            try {
                result = Integer.parseInt(value);
            } catch (final Throwable e) {
            }
        }
        return result;
    }

    @Override
    public Integer grabInt(final String key, final Integer defaultValue) {
        final String value  = grab(key);
        Integer      result = defaultValue;
        if (value != null) {
            try {
                result = Integer.parseInt(value);
            } catch (final Throwable e) {
            }
        }
        return result;
    }

    @Override
    public long grabLong(final String key, final long defaultValue) {
        final String value  = grab(key);
        long         result = defaultValue;
        if (value != null) {
            try {
                result = Long.parseLong(value);
            } catch (final Throwable e) {
            }
        }
        return result;
    }

    @Override
    public boolean grabBoolean(final String key) {
        final String value  = grab(key);
        boolean      result = false;
        if (value != null) {
            try {
                result = Boolean.parseBoolean(value);
            } catch (final Throwable e) {
            }
        }
        return result;
    }

    @Override
    public boolean grabBoolean(final String key, final boolean defaultValue) {
        final String value  = grab(key);
        boolean      result = defaultValue;
        if (value != null) {
            try {
                result = Boolean.parseBoolean(value);
            } catch (final Throwable e) {
            }
        }
        return result;
    }

    @Override
    public Double grab(final String key, final double defaultValue) {
        final String value  = grab(key);
        double       result = defaultValue;
        if (value != null) {
            try {
                result = Double.parseDouble(value);
            } catch (final Throwable e) {
            }
        }
        return result;
    }

    @Override
    public Double grabDouble(final String key) {
        final String value  = grab(key);
        Double       result = null;
        if (value != null) {
            try {
                result = Double.parseDouble(value);
            } catch (final Throwable e) {
            }
        }
        return result;
    }

    @Override
    public <T> T grabJson(final String key, final String json, final JsonUnmarshalling unmarshaller) {
        final String value  = grab(key);
        T            result = null;
        if (value != null) {
            result = unmarshaller.process(value);
        }
        return result;
    }

    @Override
    public <T> T grabJson(final String key, final JsonUnmarshalling unmarshaller) {
        final String value  = grab(key);
        T            result = null;
        if (value != null) {
            result = unmarshaller.process(value);
        }
        return result;
    }

    @Override
    public <T> T grabJson(final String key, final Object jsonObj, final JsonUnmarshalling unmarshaller) {
        final String value  = grab(key);
        T            result = null;
        if (value != null) {
            result = unmarshaller.process(value);
        }
        return result;
    }

    @Override
    public ConfigHandler<String, String> optionnal() {
        return this;
    }

    @Override
    public List<String> grabValues(final String prefix) {
        Asserts.assertNotNull("property prefix is mandatory!", prefix);
        final List<String> result = new ArrayList<>();
        final Pattern      regex  = Pattern.compile("^" + prefix + "[.][0-9]+$");

        for (final Map.Entry<String, String> entry : entrySet()) {
            if (regex.matcher(entry.getKey()).matches()) {
                result.add(applyProperties(entry.getValue()));
            }
        }
        return result;
    }

    @Override
    public int size() {
        return PROPERTIES.size();
    }

    @Override
    public boolean isEmpty() {
        return PROPERTIES.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return PROPERTIES.containsKey(String.valueOf(key));
    }

    @Override
    public boolean containsValue(final Object value) {
        return PROPERTIES.containsKey(String.valueOf(value));
    }

    @Override
    public String get(final Object key) {
        return PROPERTIES.get(String.valueOf(key));
    }

    @Override
    public String put(final String key, final String value) {
        return PROPERTIES.put(key, value);
    }

    @Override
    public String remove(final Object key) {
        return PROPERTIES.remove(String.valueOf(key));
    }

    @Override
    public void putAll(final Map<? extends String, ? extends String> values) {
        if (values != null) {
            PROPERTIES.putAll(values);
        }
    }

    @Override
    public void clear() {
        PROPERTIES.clear();
    }

    @Override
    public Set<String> keySet() {
        return PROPERTIES.keySet();
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return PROPERTIES.entrySet();
    }
}
