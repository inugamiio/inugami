package io.inugami.framework.api.processors;

import io.inugami.framework.interfaces.tools.TemplateProviderSPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultConfigHandlerTest {


    private DefaultConfigHandler handler;

    @Mock
    private TemplateProviderSPI templateProvider;

    @BeforeEach
    void setUp() {
        handler = new DefaultConfigHandler();
        handler.clear();
        injectMockTemplate();
    }

    private void injectMockTemplate() {
        try {
            var field = DefaultConfigHandler.class.getDeclaredField("template");
            field.setAccessible(true);
            field.set(handler, templateProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_grab_string_properties_with_template() {
        // GIVEN
        handler.put("my.key", "raw_value");
        when(templateProvider.applyProperties(eq("raw_value"), any())).thenReturn("transformed_value");

        // WHEN & THEN
        assertThat(handler.grab("my.key")).isEqualTo("transformed_value");
    }

    @Test
    void should_handle_numeric_parsing_and_errors() {
        // GIVEN
        handler.put("key.int", "123");
        handler.put("key.bad", "not_a_number");
        when(templateProvider.applyProperties(any(), any())).thenAnswer(i -> i.getArgument(0));

        // THEN - Int
        assertThat(handler.grabInt("key.int")).isEqualTo(123);
        assertThat(handler.grabInt("key.bad")).isNull();
        assertThat(handler.grabInt("key.bad", 456)).isEqualTo(456);

        // THEN - Long / Double / Boolean
        handler.put("key.long", "1000");
        handler.put("key.bool", "true");
        handler.put("key.double", "10.5");

        assertThat(handler.grabLong("key.long", 0L)).isEqualTo(1000L);
        assertThat(handler.grabBoolean("key.bool")).isTrue();
        assertThat(handler.grabDouble("key.double")).isEqualTo(10.5);
    }

    @Test
    void should_grab_values_by_prefix_with_index() {
        // GIVEN
        handler.put("list.0", "val0");
        handler.put("list.1", "val1");
        handler.put("list.abc", "ignored"); // car pas numérique selon la regex
        handler.put("other.0", "ignored");

        when(templateProvider.applyProperties(any(), any())).thenAnswer(i -> i.getArgument(0));

        // WHEN
        List<String> results = handler.grabValues("list");

        // THEN
        assertThat(results).containsExactlyInAnyOrder("val0", "val1");
        assertThat(results).doesNotContain("ignored");
    }

    @Test
    void should_handle_map_operations() {
        handler.putAll(Map.of("k1", "v1", "k2", "v2"));

        assertThat(handler.size()).isGreaterThanOrEqualTo(2);
        assertThat(handler.containsKey("k1")).isTrue();
        assertThat(handler.get("k1")).isEqualTo("v1");

        handler.remove("k1");
        assertThat(handler.containsKey("k1")).isFalse();
    }

    @Test
    void should_refresh_context() {
        handler.onContextRefreshed(new Object());
        assertThat(handler).isNotNull();
    }
}