package io.inugami.interfaces.tools;

import java.util.Map;

public interface TemplateProviderSPI {
    String applyProperties(final String value, final Map<String, String> config);
}
