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
package io.inugami.monitoring.springboot.actuator.feature;

import io.inugami.api.feature.FeatureContext;
import io.inugami.api.feature.IFeatureService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@RequiredArgsConstructor
public class FeatureIndicator implements HealthIndicator {

    private final IFeatureService featureService;

    @Override
    public Health health() {
        return getHealth(true);
    }

    @Override
    public Health getHealth(final boolean includeDetails) {
        final List<FeatureContext> features = featureService.getFeatures();

        return new Health.Builder().status(resolveStatus(features))
                                   .withDetail("features", rebuildFeatures(features))
                                   .build();
    }

    private List<FeatureContext> rebuildFeatures(final List<FeatureContext> features) {
        return Optional.ofNullable(features)
                       .orElse(List.of())
                       .stream()
                       .map(feature -> FeatureContext.builder()
                                                     .featureName(feature.getFeatureName())
                                                     .status(feature.getStatus())
                                                     .monitored(feature.isMonitored())
                                                     .enabledByDefault(feature.isEnabledByDefault())
                                                     .build())
                       .collect(Collectors.toList());
    }

    private Status resolveStatus(final List<FeatureContext> features) {
        FeatureContext.Status status = FeatureContext.Status.UNKNOWN;

        for (FeatureContext feature : Optional.ofNullable(features).orElse(List.of())) {
            final FeatureContext.Status currentStatus =
                    feature.getStatus() == null ? FeatureContext.Status.UNKNOWN : feature.getStatus();
            if (currentStatus.ordinal() > status.ordinal()) {
                status = currentStatus;
            }
        }

        Status result = null;
        switch (status) {
            case UP:
                result = Status.UP;
                break;

            case OUT_OF_SERVICE:
                result = Status.OUT_OF_SERVICE;
                break;
            default:
                result = Status.UNKNOWN;
                break;
        }


        return result;
    }
}
