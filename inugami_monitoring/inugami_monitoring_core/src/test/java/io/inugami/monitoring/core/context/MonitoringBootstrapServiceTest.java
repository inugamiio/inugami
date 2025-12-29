package io.inugami.monitoring.core.context;

import io.inugami.framework.interfaces.monitoring.MonitoringLoaderSpi;
import io.inugami.framework.interfaces.monitoring.models.Monitoring;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonitoringBootstrapServiceTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Mock
    private MonitoringLoaderSpi loader;

    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void initialize_nominal() {
        when(loader.load()).thenReturn(Monitoring.builder().build());
        final MonitoringBootstrapService service = buildService();
        service.initialize();

        assertThat(MonitoringBootstrapService.getContext()).isNotNull();

        service.shutdown();
    }

    private MonitoringBootstrapService buildService() {
        return MonitoringBootstrapService.builder()
                                         .loader(loader)
                                         .build();
    }
}