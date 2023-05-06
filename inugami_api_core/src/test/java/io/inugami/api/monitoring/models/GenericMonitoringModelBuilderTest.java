package io.inugami.api.monitoring.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GenericMonitoringModelBuilderTest {


    @Test
    void build_withoutValue() {
        final GenericMonitoringModelBuilder value = new GenericMonitoringModelBuilder();
        assertThat(value.build()).isNotNull();
    }

}