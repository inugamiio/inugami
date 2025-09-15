package io.inugami.framework.interfaces.tools;

import java.util.Map;

public interface TemplateProviderSPI {
    String applyProperties(final String value, final Map<String, String> config);
}
