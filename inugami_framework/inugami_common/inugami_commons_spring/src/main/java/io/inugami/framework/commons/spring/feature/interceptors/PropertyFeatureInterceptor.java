/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.framework.commons.spring.feature.interceptors;

import io.inugami.framework.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.feature.FeatureContext;
import io.inugami.framework.interfaces.feature.FeatureInterceptor;
import io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"java:S112"})
@Slf4j
@RequiredArgsConstructor
@Getter(AccessLevel.PACKAGE)
@Builder
@Component
public class PropertyFeatureInterceptor implements FeatureInterceptor, ApplicationLifecycleSPI {
    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    public static final ErrorCode ERROR_CODE = DefaultErrorCode.buildUndefineErrorCode()
                                                               .statusCode(501)
                                                               .errorCode("FEATURE-0_1")
                                                               .message("feature is disabled")
                                                               .build();


    private final        ConfigurableEnvironment resolver;
    private static final Map<String, Boolean>    CACHE = new ConcurrentHashMap<>();

    public static Map<String, Boolean> getCache() {
        return CACHE;
    }

    @PostConstruct
    public void init() {
        DefaultApplicationLifecycleSPI.register(this);
    }

    @Override
    public void onContextRefreshed(final Object event) {
        CACHE.clear();
    }

    // ========================================================================
    // API
    // ========================================================================
    @Override
    public void assertAllowsToInvoke(final FeatureContext featureContext) {
        Boolean enabled = null;
        final String prefix =
                featureContext.getPropertyPrefix() == null ? "" : featureContext.getPropertyPrefix() + ".";
        final String featureName = featureContext.getFeatureName() == null ? featureContext.getMethod()
                                                                                           .getName() : featureContext.getFeatureName();
        final String propertyKey = prefix + featureName + ".enabled";

        enabled = CACHE.get(propertyKey);
        if (enabled == null) {
            final String propertyValue = resolver.getProperty(propertyKey);
            if (propertyValue == null) {
                if (!CACHE.containsKey(propertyKey)) {
                    log.warn("property {} not found, feature {} {}", propertyValue, featureName, featureContext.isEnabledByDefault() ? "enabled" : "disabled");
                }
                enabled = featureContext.isEnabledByDefault();
            } else {
                enabled = Boolean.parseBoolean(propertyValue);
            }
            CACHE.put(propertyKey, enabled);
        }

        if (enabled == null || !enabled) {
            throw new FeaturePropertyDisabled(ERROR_CODE.addDetail("feature {0} is disabled in property {1}", featureName, propertyKey))
                    ;
        }
    }


    public static class FeaturePropertyDisabled extends UncheckedException {
        public FeaturePropertyDisabled(final ErrorCode errorCode) {
            super(errorCode);
        }
    }
}
