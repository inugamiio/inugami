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
package io.inugami.commons.spring.feature;

import io.inugami.api.feature.Feature;
import io.inugami.api.feature.FeatureContext;
import io.inugami.api.feature.IFeatureService;
import io.inugami.api.tools.ReflectionUtils;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.inugami.commons.spring.feature.FeatureUtils.buildFeatureContext;

@Getter
@Service
public class FeatureService implements BeanPostProcessor, IFeatureService {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final List<FeatureContext> features = new ArrayList<>();

    // =================================================================================================================
    // SCAN
    // =================================================================================================================
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        features.addAll(searchFeatures(bean));
        return bean;
    }

    private List<FeatureContext> searchFeatures(final Object bean) {
        return ReflectionUtils.loadAllMethods(bean.getClass())
                              .stream()
                              .filter(method -> ReflectionUtils.hasAnnotation(method, Feature.class))
                              .map(method -> buildFeatureContext(method, bean.getClass(), null, bean))
                              .filter(Objects::nonNull)
                              .collect(Collectors.toList());
    }


}
