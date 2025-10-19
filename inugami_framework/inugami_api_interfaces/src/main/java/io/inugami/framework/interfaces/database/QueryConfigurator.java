package io.inugami.framework.interfaces.database;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.models.maven.Gav;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;


public interface QueryConfigurator {
    String GROUP_ID    = "groupId";
    String ARTIFACT_ID = "artifactId";
    String VERSION     = "version";

    default boolean accept(final String queryPath) {
        return false;
    }

    default ConfigHandler<String, String> configure(final String queryPath,
                                                    final Gav gav,
                                                    final ConfigHandler<String, String> configuration) {
        return configuration;
    }

    default Map<String, String> gavToMap(final Gav gav) {
        if (gav == null) {
            return new LinkedHashMap<>();
        } else {
            final Map<String, String> result = new LinkedHashMap<>();
            applyIfNotNull(gav.getGroupId(), value -> result.put(GROUP_ID, value));
            applyIfNotNull(gav.getArtifactId(), value -> result.put(ARTIFACT_ID, value));
            applyIfNotNull(gav.getVersion(), value -> result.put(VERSION, value));

            return result;
        }
    }
}
