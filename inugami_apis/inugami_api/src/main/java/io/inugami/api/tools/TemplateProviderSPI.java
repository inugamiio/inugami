package io.inugami.api.tools;

import java.util.Map;

public interface TemplateProviderSPI {
    String applyProperties(final String value, final Map<String, String> config);
}
