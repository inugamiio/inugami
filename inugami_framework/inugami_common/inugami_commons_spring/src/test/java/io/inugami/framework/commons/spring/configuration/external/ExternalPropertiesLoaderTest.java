package io.inugami.framework.commons.spring.configuration.external;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalPropertiesLoaderTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Mock
    private ConfigurableApplicationContext    applicationContext;
    @Mock
    private ConfigurableEnvironment           environment;
    @Mock
    private MutablePropertySources            propertySources;
    @Captor
    private ArgumentCaptor<MapPropertySource> propertyCaptor;

    // =================================================================================================================
    // INIT
    // =================================================================================================================
    @Test
    void initialize_nominal() {
        System.setProperty(ExternalPropertiesLoader.EXTERNAL_CONFIGURATION, "./src/test/resources/common/spring/configuration/external");
        when(applicationContext.getEnvironment()).thenReturn(environment);
        when(environment.getPropertySources()).thenReturn(propertySources);
        loader().initialize(applicationContext);

        verify(propertySources).addLast(propertyCaptor.capture());
        assertText(propertyCaptor.getValue(),
                   """
                           {
                             "name" : "fake.properties",
                             "source" : {
                               "some.value" : "hello"
                             },
                             "propertyNames" : [ "some.value" ]
                           }
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================

    ExternalPropertiesLoader loader() {
        return new ExternalPropertiesLoader();
    }
}