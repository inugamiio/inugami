package io.inugami.framework.commons.spring.mapstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MapStructScannerTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Mock
    private ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent;


    @Mock
    private DefaultListableBeanFactory beanFactory;
    @Mock
    private ConfigurableEnvironment    configurableEnvironment;

    @Captor
    private ArgumentCaptor<String> classNameCaptor;

    @Captor
    private ArgumentCaptor<Object> instanceCaptor;

    @BeforeEach
    void init() {
        MapStructScanner.clearMappers();
        lenient().when(applicationEnvironmentPreparedEvent.getEnvironment()).thenReturn(configurableEnvironment);
    }

    // =================================================================================================================
    // SCAN
    // =================================================================================================================

    @Test
    void processScan_nominal() {
        final MapStructScanner scanner = scanner();
        final Map<String, String> config = Map.ofEntries(
                Map.entry(MapStructScanner.PROPERTY, "true"),
                Map.entry(MapStructScanner.PROPERTY_BASE_PACKAGE, "io.inugami.commons.spring.mapstruct")
        );

        when(configurableEnvironment.getProperty(any())).thenAnswer(answer -> config.get(answer.getArgument(0)));


        scanner.processScan();
        assertTextRelative(scanner.getMappers(),
                           "common/spring/mapstruct/mapStructScanner/onEnvironmentPrepared_nominal.json");
    }


    @Test
    void processScan_withoutBasePackage() {
        final MapStructScanner scanner = scanner();
        final Map<String, String> config = Map.ofEntries(
                Map.entry(MapStructScanner.PROPERTY, "true")
        );

        when(configurableEnvironment.getProperty(any())).thenAnswer(answer -> config.get(answer.getArgument(0)));

        scanner.processScan();
        assertTextRelative(scanner.getMappers(),
                           "common/spring/mapstruct/mapStructScanner/onEnvironmentPrepared_nominal.json");
    }

    @Test
    void processScan_withoutProperty() {
        final MapStructScanner scanner = scanner();
        scanner.processScan();
        assertThat(scanner.getMappers()).isEmpty();
    }

    @Test
    void processScan_disabled() {
        final MapStructScanner scanner = scanner();
        when(configurableEnvironment.getProperty(MapStructScanner.PROPERTY)).thenReturn("false");

        scanner.processScan();
        assertThat(scanner.getMappers()).isEmpty();
    }

    // =================================================================================================================
    // REGISTER
    // =================================================================================================================
    @Test
    void registerMapper_nominal() {
        final MapStructScanner scanner = scanner();
        final Map<String, String> config = Map.ofEntries(
                Map.entry(MapStructScanner.PROPERTY, "true"),
                Map.entry(MapStructScanner.PROPERTY_BASE_PACKAGE, "io.inugami.commons.spring")
        );

        when(configurableEnvironment.getProperty(any())).thenAnswer(answer -> config.get(answer.getArgument(0)));


        scanner.processScan();
        scanner.registerMapper();

        verify(beanFactory).registerSingleton(classNameCaptor.capture(), instanceCaptor.capture());

        assertTextRelative(classNameCaptor.getAllValues(),
                           "common/spring/mapstruct/mapStructScanner/onApplicationContextInitialized_nominal.json");
    }


    @Test
    void registerMapper_withoutMapper() {
        final MapStructScanner scanner = scanner();
        scanner.registerMapper();
        verify(beanFactory, never()).registerSingleton(classNameCaptor.capture(), instanceCaptor.capture());
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    MapStructScanner scanner() {
        return MapStructScanner.builder()
                               .environment(configurableEnvironment)
                               .beanFactory(beanFactory)
                               .classLoader(this.getClass().getClassLoader())
                               .build();
    }

}