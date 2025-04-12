package io.inugami.framework.commons.spring.mapstruct;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MapStructContextInitializerTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Mock
    private ConfigurableApplicationContext context;
    @Mock
    private DefaultListableBeanFactory     beanFactory;
    @Mock
    private ConfigurableEnvironment        configurableEnvironment;

    @Captor
    private ArgumentCaptor<String> classNameCaptor;

    @Captor
    private ArgumentCaptor<Object> instanceCaptor;

    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void initialize_nominal() {
        final Map<String, String> config = Map.ofEntries(
                Map.entry(MapStructScanner.PROPERTY, "true"),
                Map.entry(MapStructScanner.PROPERTY_BASE_PACKAGE, "io.inugami.framework.commons.spring.mapstruct")
        );

        when(configurableEnvironment.getProperty(any())).thenAnswer(answer -> config.get(answer.getArgument(0)));
        when(context.getEnvironment()).thenReturn(configurableEnvironment);
        when(context.getBeanFactory()).thenReturn(beanFactory);
        when(context.getClassLoader()).thenReturn(MapStructContextInitializerTest.class.getClassLoader());

        new MapStructContextInitializer().initialize(context);

        verify(beanFactory).registerSingleton(classNameCaptor.capture(), instanceCaptor.capture());
        assertText(classNameCaptor.getAllValues(),
                   """
                           [ "io.inugami.framework.commons.spring.mapstruct.SomeMapStructMapper" ]
                           """);
    }

}