package io.inugami.commons.spring;

import io.inugami.api.spi.JavaSpiLoaderServiceSPI;
import io.inugami.api.spi.SpiLoaderServiceSPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class SpringSpiLoaderServiceSPI implements SpiLoaderServiceSPI {
    private static final AtomicReference<ConfigurableListableBeanFactory> SPRING_CONTEXT = new AtomicReference<>();
    private final        JavaSpiLoaderServiceSPI                          javaSpiLoader  = new JavaSpiLoaderServiceSPI();

    static synchronized void initSpringContext(ConfigurableListableBeanFactory context) {
        SPRING_CONTEXT.set(context);
    }

    @Override
    public <T> List<T> loadServices(final Class<?> type) {
        List<T> result = new ArrayList<>();
        result.addAll(searchSpringBeans(type));
        result.addAll(javaSpiLoader.loadServices(type));
        return result;
    }


    private <T> List<T> searchSpringBeans(final Class<?> type) {
        final ConfigurableListableBeanFactory beanFactory = SPRING_CONTEXT.get();
        if (beanFactory == null) {
            return new ArrayList<>();
        }

        final List<T>  result = new ArrayList<>();
        Map<String, ?> beans  = null;

        try {
            beans = beanFactory.getBeansOfType(type);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        if (beans != null) {
            for (Map.Entry<String, ?> entry : beans.entrySet()) {
                result.add((T) entry.getValue());
            }
        }
        return result;
    }

}
