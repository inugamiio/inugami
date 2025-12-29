package io.inugami.monitoring.core.interceptors;

import io.inugami.framework.api.exceptions.WarningContext;
import io.inugami.framework.interfaces.exceptions.DefaultWarning;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.inugami.monitoring.core.interceptors.WarningResponseListener.X_WARNINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WarningResponseListenerTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Mock
    private HttpServletResponse    response;
    @Captor
    private ArgumentCaptor<String> headerValueCaptor;

    @BeforeEach
    public void init() {
        WarningContext.getInstance().clear();
    }

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Test
    void beforeWriting_withoutWarning() {
        new WarningResponseListener().beforeWriting(response);
        verify(response, never()).setHeader(any(), any());
    }

    @Test
    void beforeWriting_nominal() {
        WarningContext.getInstance().addWarnings(DefaultWarning.builder()
                                                               .warningCode("WARN-0_0")
                                                               .message("some user can't be found")
                                                               .messageDetail("some source doesn't sent users")
                                                               .typeFunctional()
                                                               .category("USER")
                                                               .domain("user")
                                                               .subDomain("roles")
                                                               .build(),
                                                 DefaultWarning.builder()
                                                               .warningCode("WARN-0_1")
                                                               .message("other warning")
                                                               .typeFunctional()
                                                               .category("USER")
                                                               .domain("user")
                                                               .subDomain("roles")
                                                               .build());

        new WarningResponseListener().beforeWriting(response);
        verify(response).setHeader(eq(X_WARNINGS), headerValueCaptor.capture());
        assertThat(headerValueCaptor.getValue()).isEqualTo("WARN-0_0,WARN-0_1");
    }
}