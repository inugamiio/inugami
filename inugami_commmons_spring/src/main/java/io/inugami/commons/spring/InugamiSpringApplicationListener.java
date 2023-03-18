package io.inugami.commons.spring;

import io.inugami.api.listeners.ApplicationLifecycleSPI;
import io.inugami.api.spi.SpiLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.context.event.*;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class InugamiSpringApplicationListener implements ApplicationListener<ApplicationEvent> {


    private static final AtomicReference<ConfigurableEnvironment> SPRING_ENV = new AtomicReference<>();

    private List<ApplicationLifecycleSPI> listeners = null;

    public static ConfigurableEnvironment getConfigurableEnvironment() {
        return SPRING_ENV.get();
    }

    @Override
    public void onApplicationEvent(final ApplicationEvent event) {
        log.debug("event:{}", event.getClass());

        if (listeners == null) {
            loadListeners();
        }


        if (event instanceof ApplicationStartingEvent) {
            onApplicationStarting(((ApplicationStartingEvent) event));
        } else if (event instanceof ApplicationEnvironmentPreparedEvent) {
            onEnvironmentPrepared((ApplicationEnvironmentPreparedEvent) event);
        } else if (event instanceof ApplicationContextInitializedEvent) {
            onApplicationContextInitialized(((ApplicationContextInitializedEvent) event));
        } else if (event instanceof ApplicationPreparedEvent) {
            onApplicationPrepared(((ApplicationPreparedEvent) event));
        } else if (event instanceof WebServerInitializedEvent) {
            onbWebServerInitialized(((WebServerInitializedEvent) event));
        } else if (event instanceof ApplicationStartedEvent) {
            onApplicationStarted(((ApplicationStartedEvent) event));
        } else if (event instanceof ContextRefreshedEvent) {
            onContextRefreshed(((ContextRefreshedEvent) event));
        } else if (event instanceof AvailabilityChangeEvent) {
            onAvailabilityChange(((AvailabilityChangeEvent) event));
        } else if (event instanceof ApplicationReadyEvent) {
            onApplicationReady(((ApplicationReadyEvent) event));
        } else if (event instanceof ApplicationFailedEvent) {
            onApplicationFail(((ApplicationFailedEvent) event));
        }
    }


    private void onApplicationStarting(final ApplicationStartingEvent event) {
        for (ApplicationLifecycleSPI listener : listeners) {
            listener.onApplicationStarting(event);
        }
    }

    private void onEnvironmentPrepared(final ApplicationEnvironmentPreparedEvent event) {
        SPRING_ENV.set(event.getEnvironment());
        for (ApplicationLifecycleSPI listener : listeners) {
            listener.onEnvironmentPrepared(event);
        }
    }

    private void onApplicationContextInitialized(final ApplicationContextInitializedEvent event) {
        SpringSpiLoaderServiceSPI.initSpringContext(event.getApplicationContext().getBeanFactory());
        loadListeners();
        for (ApplicationLifecycleSPI listener : listeners) {
            listener.onApplicationContextInitialized(event);
        }
    }


    private void onApplicationPrepared(final ApplicationPreparedEvent event) {
        for (ApplicationLifecycleSPI listener : listeners) {
            listener.onApplicationPrepared(event);
        }
    }

    private void onbWebServerInitialized(final WebServerInitializedEvent event) {
        for (ApplicationLifecycleSPI listener : listeners) {
            listener.onbWebServerInitialized(event);
        }
    }

    private void onContextRefreshed(Object event) {
        for (ApplicationLifecycleSPI listener : listeners) {
            listener.onContextRefreshed(event);
        }
    }

    private void onApplicationStarted(Object event) {
        for (ApplicationLifecycleSPI listener : listeners) {
            listener.onApplicationStarted(event);
        }
    }

    private void onAvailabilityChange(Object event) {
        for (ApplicationLifecycleSPI listener : listeners) {
            listener.onAvailabilityChange(event);
        }
    }

    private void onApplicationReady(Object event) {
        for (ApplicationLifecycleSPI listener : listeners) {
            listener.onApplicationReady(event);
        }
    }

    private void onApplicationFail(final ApplicationFailedEvent event) {
        for (ApplicationLifecycleSPI listener : listeners) {
            listener.onApplicationFail(event);
        }
    }


    private void loadListeners() {
        final List<ApplicationLifecycleSPI> instances = SpiLoader.INSTANCE.loadSpiServicesByPriority(ApplicationLifecycleSPI.class);

        if (instances == null) {
            listeners = new ArrayList<>();
        } else {
            listeners = instances;
        }
    }

}
