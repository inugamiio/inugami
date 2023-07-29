package io.inugami.commons.spring.mapstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
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
    private ApplicationContextInitializedEvent applicationContextInitializedEvent;

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
    void onEnvironmentPrepared_nominal() {
        final MapStructScanner scanner = scanner();
        final Map<String, String> config = Map.ofEntries(
                Map.entry(MapStructScanner.PROPERTY, "true"),
                Map.entry(MapStructScanner.PROPERTY_BASE_PACKAGE, "io.inugami.commons.spring.mapstruct")
        );

        when(configurableEnvironment.getProperty(any())).thenAnswer(answer -> config.get(answer.getArgument(0)));


        scanner.onEnvironmentPrepared(applicationEnvironmentPreparedEvent);
        assertTextRelative(scanner.getMappers(),
                           "common/spring/mapstruct/mapStructScanner/onEnvironmentPrepared_nominal.json");
    }


    @Test
    void onEnvironmentPrepared_withoutBasePackage() {
        final MapStructScanner scanner = scanner();
        final Map<String, String> config = Map.ofEntries(
                Map.entry(MapStructScanner.PROPERTY, "true")
        );

        when(configurableEnvironment.getProperty(any())).thenAnswer(answer -> config.get(answer.getArgument(0)));


        scanner.onEnvironmentPrepared(applicationEnvironmentPreparedEvent);
        assertTextRelative(scanner.getMappers(),
                           "common/spring/mapstruct/mapStructScanner/onEnvironmentPrepared_nominal.json");
    }

    @Test
    void onEnvironmentPrepared_withoutProperty() {
        final MapStructScanner scanner = scanner();
        scanner.onEnvironmentPrepared(applicationEnvironmentPreparedEvent);
        assertThat(scanner.getMappers()).isEmpty();
    }

    @Test
    void onEnvironmentPrepared_disabled() {
        final MapStructScanner scanner = scanner();
        when(configurableEnvironment.getProperty(MapStructScanner.PROPERTY)).thenReturn("false");

        scanner.onEnvironmentPrepared(applicationEnvironmentPreparedEvent);
        assertThat(scanner.getMappers()).isEmpty();
    }

    @Test
    void onEnvironmentPrepared_withoutEvent() {
        final MapStructScanner scanner = scanner();
        scanner.onEnvironmentPrepared(null);
        assertThat(scanner.getMappers()).isEmpty();
        verify(configurableEnvironment, never()).getProperty(any());
    }

    @Test
    void onEnvironmentPrepared_eventOtherType() {
        final MapStructScanner scanner = scanner();
        scanner.onEnvironmentPrepared("event");
        assertThat(scanner.getMappers()).isEmpty();
        verify(configurableEnvironment, never()).getProperty(any());
    }

    // =================================================================================================================
    // REGISTER
    // =================================================================================================================
    @Test
    void onApplicationContextInitialized_nominal() {
        final MapStructScanner scanner = scanner();
        final Map<String, String> config = Map.ofEntries(
                Map.entry(MapStructScanner.PROPERTY, "true"),
                Map.entry(MapStructScanner.PROPERTY_BASE_PACKAGE, "io.inugami.commons.spring")
        );

        when(configurableEnvironment.getProperty(any())).thenAnswer(answer -> config.get(answer.getArgument(0)));


        scanner.onEnvironmentPrepared(applicationEnvironmentPreparedEvent);
        scanner.onApplicationContextInitialized(applicationContextInitializedEvent);

        verify(beanFactory).registerSingleton(classNameCaptor.capture(), instanceCaptor.capture());

        assertTextRelative(classNameCaptor.getAllValues(),
                           "common/spring/mapstruct/mapStructScanner/onApplicationContextInitialized_nominal.json");
    }


    @Test
    void onApplicationContextInitialized_withoutMapper() {
        final MapStructScanner scanner = scanner();
        scanner.onApplicationContextInitialized(applicationContextInitializedEvent);
        verify(beanFactory, never()).registerSingleton(classNameCaptor.capture(), instanceCaptor.capture());
    }
    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    MapStructScanner scanner() {
        final MapStructScanner result = new MapStructScanner();
        result.setBeanFactoryExtractor(event -> beanFactory);
        return result;
    }
}