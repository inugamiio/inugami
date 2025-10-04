package io.inugami.dashboard.infrastructure.internal.schduler;

import io.inugami.dashboard.api.domain.engine.IEngineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SchedulerProducerTest {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Mock
    private ScheduledThreadPoolExecutor executor;
    @Mock
    private IEngineService              engineService;
    @InjectMocks
    private SchedulerProducer           schedulerProducer;

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    @Test
    void schedulerProducer_nominal() {
        assertThat(schedulerProducer.computeDelay()).isNotNull();
    }

    @Test
    void run_nominal(){
        schedulerProducer.run();
        verify(engineService).run();
    }
}