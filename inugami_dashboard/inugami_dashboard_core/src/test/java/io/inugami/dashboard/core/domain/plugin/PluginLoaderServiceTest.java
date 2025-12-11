package io.inugami.dashboard.core.domain.plugin;

import io.inugami.framework.configuration.models.HandlerConfig;
import io.inugami.framework.configuration.models.ListenerModel;
import io.inugami.framework.configuration.models.ProviderConfig;
import io.inugami.framework.interfaces.alertings.AlertingProvider;
import io.inugami.framework.interfaces.alertings.AlertingProviderModel;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.exceptions.services.ProcessorException;
import io.inugami.framework.interfaces.handlers.Handler;
import io.inugami.framework.interfaces.listeners.EngineListener;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.spi.SpiLoaderServiceSPI;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import io.inugami.framework.interfaces.tools.NamedComponent;
import io.inugami.framework.interfaces.tools.PostConstructConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PluginLoaderServiceTest {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final Map<String, String> GLOBAL_PROPERTIES = Map.ofEntries(Map.entry("config", "global"));
    public static final String              MY_COMPONENT      = "myComponent";
    @Mock
    private             SpiLoaderServiceSPI spiLoaderService;
    @Mock
    private             NamedComponent      namedComponent;
    @Mock
    private             AlertingProvider    alertingProvider;
    @Mock
    private             EngineListener      engineListener;
    @Mock
    private             Processor           processor;
    @Mock
    private             Provider            provider;
    @Mock
    private             Handler             handler;
    @Mock
    private             ManifestInfo        manifest;
    @InjectMocks
    private             PluginLoaderService service;


    //==================================================================================================================
    // LOADERS
    //==================================================================================================================
    @Test
    void loadAlertings_nominal() {
        when(alertingProvider.getName()).thenReturn(MY_COMPONENT);
        when(spiLoaderService.loadServices(AlertingProvider.class)).thenReturn(List.of(alertingProvider));
        assertThat(service.loadAlertings(List.of(AlertingProviderModel.builder()
                                                                      .name(MY_COMPONENT)
                                                                      .build()), GLOBAL_PROPERTIES, manifest))
                .isNotEmpty();
    }

    @Test
    void loadListeners_nominal() {
        when(engineListener.getName()).thenReturn(MY_COMPONENT);
        when(spiLoaderService.loadServices(EngineListener.class)).thenReturn(List.of(engineListener));
        assertThat(service.loadListeners(List.of(ListenerModel.builder()
                                                              .name(MY_COMPONENT)
                                                              .build()), GLOBAL_PROPERTIES, manifest))
                .isNotEmpty();
    }

    @Test
    void loadProcessors_nominal() {
        when(spiLoaderService.loadServices(Processor.class)).thenReturn(List.of(new TestProcessor()));
        assertThat(service.loadProcessors(List.of(ProcessorModel.builder()
                                                               .name(MY_COMPONENT)
                                                               .build()), GLOBAL_PROPERTIES, manifest))
                .isNotEmpty();
    }
    @Test
    void loadProcessors_withNotPostConstructConfig() {
        when(spiLoaderService.loadServices(Processor.class)).thenReturn(List.of(processor));
        assertThat(service.loadProcessors(List.of(ProcessorModel.builder()
                                                                .name(MY_COMPONENT)
                                                                .build()), GLOBAL_PROPERTIES, manifest))
                .isNotEmpty();
    }


    @Test
    void loadProviders_nominal() {
        when(provider.getName()).thenReturn(MY_COMPONENT);
        when(spiLoaderService.loadServices(Provider.class)).thenReturn(List.of(provider));
        assertThat(service.loadProviders(List.of(ProviderConfig.builder()
                                                                .name(MY_COMPONENT)
                                                                .build()), GLOBAL_PROPERTIES, manifest))
                .isNotEmpty();
    }

    @Test
    void loadHandlers_nominal() {
        when(spiLoaderService.loadServices(Handler.class)).thenReturn(List.of(handler));
        assertThat(service.loadHandlers(List.of(HandlerConfig.builder()
                                                             .name(MY_COMPONENT)
                                                             .build()), GLOBAL_PROPERTIES, manifest))
                .isNotEmpty();
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    @Test
    void getLoadServices_nominal() {
        when(spiLoaderService.loadServices(MyComponent.class)).thenReturn(List.of(new MyComponent()));
        assertThat(service.getLoadServices(MyComponent.class)).isNotEmpty();
    }

    @Test
    void getLoadServices_withoutFound() {
        when(spiLoaderService.loadServices(MyComponent.class)).thenThrow(new UncheckedException());
        assertThat(service.getLoadServices(MyComponent.class)).isEmpty();
    }

    @Test
    void chooseProvider_nominal() {
        when(namedComponent.getName()).thenReturn(MY_COMPONENT);
        assertThat(service.chooseProvider(List.of(namedComponent), MY_COMPONENT, null)).isNotNull();
    }

    @Test
    void chooseProvider_withClass() {
        assertThat(service.chooseProvider(List.of(new MyComponent()), null, MyComponent.class.getName())).isNotNull();
    }

    @Test
    void chooseProvider_withoutFound() {
        assertThat(service.chooseProvider(List.of(new MyComponent()), null, null)).isNull();
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    private static class MyComponent implements NamedComponent {

    }

    private static class TestProcessor implements Processor, PostConstructConfig{
        @Override
        public String getName() {
            return MY_COMPONENT;
        }

        @Override
        public ProviderFutureResult process(final GenericEvent event,
                                            final ProviderFutureResult data) throws ProcessorException {
            return null;
        }
    }
}