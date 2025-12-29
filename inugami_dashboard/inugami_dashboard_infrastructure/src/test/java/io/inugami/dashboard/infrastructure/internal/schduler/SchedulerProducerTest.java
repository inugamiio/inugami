package io.inugami.dashboard.infrastructure.internal.schduler;

import io.inugami.dashboard.api.domain.engine.IEngineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.support.GenericApplicationContext;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SchedulerProducerTest {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static Clock                       CLOCK =
            Clock.fixed(Instant.parse("2025-12-02T20:53:42.00Z"), ZoneOffset.UTC);
    @Mock
    private       ScheduledThreadPoolExecutor executor;
    @Mock
    private       IEngineService              engineService;
    @Captor
    private       ArgumentCaptor<Long>        delayCaptor;

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    @Test
    void onApplicationEvent_nominal() {
        final var producer = producer();
        producer.onApplicationEvent(new ApplicationStartedEvent(new SpringApplication(SchedulerProducer.class),
                                                                new String[]{},
                                                                new GenericApplicationContext(),
                                                                Duration.of(100, ChronoUnit.MILLIS)));

        verify(executor).scheduleAtFixedRate(any(), delayCaptor.capture(), eq(1000L), any());
        assertThat(delayCaptor.getValue()).isGreaterThan(0L).isLessThanOrEqualTo(1000L);
    }

    @Test
    void schedulerProducer_nominal() {
        assertThat(producer().computeDelay()).isGreaterThan(0);
    }

    @Test
    void run_nominal() {
        producer().run();
        verify(engineService).run();
    }


    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    SchedulerProducer producer() {
        return SchedulerProducer.builder()
                                .executor(executor)
                                .engineService(engineService)
                                .clock(CLOCK)
                                .build();
    }
}