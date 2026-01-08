package io.inugami.monitoring.sensors.defaults.system;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
@SuppressWarnings({"java:S6068"})
@ExtendWith(MockitoExtension.class)
class ThreadsSensorTest {
    private ThreadsSensor sensor;

    @Mock
    private ConfigHandler<String, String> configHandler;

    @BeforeEach
    void setUp() {
        when(configHandler.grabOrDefault(eq("timeUnit"), eq(""))).thenReturn("SECONDS");
        when(configHandler.grabBoolean(eq("enableThreadsDump"), anyBoolean())).thenReturn(true);
        when(configHandler.grabInt(eq("maxDepth"), anyInt())).thenReturn(5);

        sensor = new ThreadsSensor(1000L, "query", configHandler);
    }

    @Test
    void should_initialize_with_default_constructor() {
        ThreadsSensor defaultSensor = new ThreadsSensor();
        assertThat(defaultSensor.getName()).isEqualTo("threads");
        assertThat(defaultSensor.getInterval()).isEqualTo(-1);
    }

    @Test
    void should_build_instance_via_factory_method() {
        var newInstance = sensor.buildInstance(2000L, "query", configHandler);
        assertThat(newInstance).isInstanceOf(ThreadsSensor.class);
        assertThat(newInstance.getInterval()).isEqualTo(60000L);
    }

    @Test
    void process_should_return_all_thread_states_models() {
        // WHEN
        List<GenericMonitoringModel> results = sensor.process();

        // THEN
        assertThat(results).hasSize(7);

        assertThat(results).extracting("subService")
                           .containsExactlyInAnyOrder(
                                   "all",
                                   "newThreads",
                                   "runable",
                                   "blocked",
                                   "waiting",
                                   "timedWaiting",
                                   "terminated"
                                                     );

        results.forEach(model -> {
            assertThat(model.getService()).isEqualTo("threads");
            assertThat(model.getCounterType()).isEqualTo("system");
            assertThat(model.getTimeUnit()).isEqualTo("SECONDS");
            if ("all".equals(model.getSubService())) {
                assertThat(model.getData()).isNotEmpty();
                assertThat(model.getData().toString()).contains("RUNNABLE");
            }
        });
    }

    @Test
    void process_without_dump_should_have_empty_data() {
        // GIVEN
        when(configHandler.grabBoolean(eq("enableThreadsDump"), anyBoolean())).thenReturn(false);
        ThreadsSensor noDumpSensor = new ThreadsSensor(1000L, "query", configHandler);

        // WHEN
        List<GenericMonitoringModel> results = noDumpSensor.process();

        // THEN
        results.forEach(model -> assertThat(model.getData()).isNull());
    }

    @Test
    void should_handle_default_timeunit_when_empty() {
        // GIVEN
        when(configHandler.grabOrDefault(eq("timeUnit"), eq(""))).thenReturn("");
        ThreadsSensor defaultTimeSensor = new ThreadsSensor(5000L, "query", configHandler);

        // WHEN
        List<GenericMonitoringModel> results = defaultTimeSensor.process();

        // THEN
        assertThat(results.get(0).getTimeUnit()).isEqualTo("60000ms");
    }
}