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

import io.inugami.api.listeners.ApplicationLifecycleSPI;
import io.inugami.api.mapping.MapStructMapper;
import io.inugami.api.tools.ReflectionUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings({"java:S3740"})
@Setter(AccessLevel.PACKAGE)
@Slf4j
public class MapStructScanner implements ApplicationLifecycleSPI {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final  String                                                                        PROPERTY              = "io.inugami.spring.mapstruct.scanner.enabled";
    public static final  String                                                                        PROPERTY_BASE_PACKAGE = "io.inugami.spring.mapstruct.scanner.basePackage";
    private static final Set<Class<?>>                                                                 MAPPERS               = new LinkedHashSet<>();
    private              Function<ApplicationContextInitializedEvent, ConfigurableListableBeanFactory> beanFactoryExtractor;


    public static Set<Class<?>> getMappers(){
        return MAPPERS;
    }
    public static void clearMappers(){
        MAPPERS.clear();
    }

    // =================================================================================================================
    // SCAN
    // =================================================================================================================
    @Override
    public void onEnvironmentPrepared(final Object event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            processScan((ApplicationEnvironmentPreparedEvent) event);
        }
    }

    private void processScan(final ApplicationEnvironmentPreparedEvent event) {
        final String property = event.getEnvironment().getProperty(PROPERTY);
        if (!Boolean.parseBoolean(property)) {
            log.info("Mapstruct scan disabled (to enable please define {} as true)", PROPERTY);
            return;
        }
        final String basePackage = event.getEnvironment().getProperty(PROPERTY_BASE_PACKAGE);
        final Set<Class<?>> mapperClasses = ReflectionUtils.scan(basePackage == null ? "" : basePackage,
                                                                 Class::isInterface,
                                                                 c -> c.getAnnotation(MapStructMapper.class) != null);
        MAPPERS.addAll(mapperClasses);
    }

    // =================================================================================================================
    // REGISTER
    // =================================================================================================================
    @Override
    public void onApplicationContextInitialized(final Object event) {
        if (event instanceof ApplicationContextInitializedEvent) {
            registerMapper((ApplicationContextInitializedEvent) event);
        }
    }

    private void registerMapper(final ApplicationContextInitializedEvent event) {
        if (MAPPERS.isEmpty()) {
            return;
        }
        final ConfigurableListableBeanFactory beanFactory = beanFactoryExtractor == null
                ? event.getApplicationContext().getBeanFactory()
                : beanFactoryExtractor.apply(event);

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
