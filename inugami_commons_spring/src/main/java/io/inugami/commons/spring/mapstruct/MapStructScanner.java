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
package io.inugami.commons.spring.mapstruct;

import io.inugami.api.mapping.MapStructMapper;
import io.inugami.api.tools.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings({"java:S3740"})


@Slf4j
@Builder
@AllArgsConstructor
public class MapStructScanner {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final  String                          PROPERTY              = "io.inugami.spring.mapstruct.scanner.enabled";
    public static final  String                          PROPERTY_BASE_PACKAGE = "io.inugami.spring.mapstruct.scanner.basePackage";
    private static final Set<Class<?>>                   MAPPERS               = new LinkedHashSet<>();
    private final        ConfigurableListableBeanFactory beanFactory;
    private final        ClassLoader                     classLoader;
    private final        ConfigurableEnvironment         environment;

    public static Set<Class<?>> getMappers() {
        return MAPPERS;
    }

    public static void clearMappers() {
        MAPPERS.clear();
    }


    // =================================================================================================================
    // SCAN
    // =================================================================================================================


    public MapStructScanner processScan() {

        final String enabled     = environment.getProperty(PROPERTY);
        final String basePackage = environment.getProperty(PROPERTY_BASE_PACKAGE);

        if (enabled == null || !Boolean.parseBoolean(enabled)) {
            log.info("Mapstruct scan disabled (to enable please define {} as true)", PROPERTY);
            return this;
        }

        final String currentBasePackage = basePackage == null ? "" : basePackage;
        final Set<Class<?>> mapperClasses = ReflectionUtils.scan(currentBasePackage, classLoader,
                                                                 Class::isInterface,
                                                                 c -> c.getAnnotation(MapStructMapper.class) != null);
        MAPPERS.addAll(mapperClasses);
        return this;
    }

    // =================================================================================================================
    // REGISTER
    // =================================================================================================================

    public void registerMapper() {
        if (MAPPERS.isEmpty()) {
            return;
        }

        for (final Class mapperClass : MAPPERS) {
            Object existingBean = null;
            try {
                existingBean = beanFactory.getBean(mapperClass);
            } catch (final Throwable e) {
                if (log.isDebugEnabled()) {
                    log.error(e.getMessage(), e);
                }
            }

            if (existingBean == null) {
                log.info("register MapStruct Mapper : {}", mapperClass);
                try {
                    beanFactory.registerSingleton(mapperClass.getName(), Mappers.getMapper(mapperClass));
                } catch (final Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
