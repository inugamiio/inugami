package io.inugami.core.alertings.senders;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.alertings.AlertsSender;
import io.inugami.api.alertings.AlertsSenderException;
import org.junit.jupiter.api.Test;

import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlertSenderServiceTest {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testResolveSenders() {
        final List<AlertsSender> senders = Arrays.asList(new SenderCdi(), new SenderCdiWithName(), new BasicSender());
        final AlertSenderService service = new AlertSenderService(senders);

        final List<AlertsSender> cas1 = service.resolveSenders(buildAlert("senderCdi", "mySender"));
        assertEquals(2, cas1.size());
        final AlertsSender firstSender = cas1.get(0);
        if (firstSender instanceof SenderCdi) {
            assertTrue(cas1.get(1) instanceof SenderCdiWithName);
        } else {
            assertTrue(firstSender instanceof SenderCdiWithName);
            assertTrue(cas1.get(1) instanceof SenderCdi);
        }

        final List<AlertsSender> cas2 = service.resolveSenders(buildAlert("basicSender"));
        assertEquals(1, cas2.size());
        assertTrue(cas2.get(0) instanceof BasicSender);

        final List<AlertsSender> cas3 = service.resolveSenders(new AlertingResult());
        assertEquals(3, cas3.size());

    }

    private AlertingResult buildAlert(final String... providers) {
        return AlertingResult.builder()
                             .addProviders(providers)
                             .build();
    }

    // =========================================================================
    // PRIVATE CLASSES
    // =========================================================================
    private class MockSender implements AlertsSender {
        @Override
        public void sendNewAlert(final AlertingResult alert, final List<String> channels) throws AlertsSenderException {
        }

        @Override
        public void delete(final List<String> uids, final List<String> channels) throws AlertsSenderException {
        }

        @Override
        public boolean enable() {
            return false;
        }
    }

    @Named
    private class SenderCdi extends MockSender implements AlertsSender {
    }

    @Named("mySender")
    private class SenderCdiWithName extends MockSender implements AlertsSender {
    }

    private class BasicSender extends MockSender implements AlertsSender {
    }
}
