package org.inugami.core.alertings.senders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.alertings.AlertsSender;
import org.inugami.api.alertings.AlertsSenderException;
import org.junit.Test;

public class AlertSenderServiceTest {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testResolveSenders() {
        final List<AlertsSender> senders = Arrays.asList(new SenderCdi(), new SenderCdiWithName(), new BasicSender());
        final AlertSenderService service = new AlertSenderService(senders);
        
        final List<AlertsSender> cas1 = service.resolveSenders(buildAlert("senderCdi", "mySender"));
        assertEquals(2, cas1.size());
        final AlertsSender firstSender = cas1.get(0);
        if (firstSender instanceof SenderCdi) {
            assertTrue(cas1.get(1) instanceof SenderCdiWithName);
        }
        else {
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
        final AlertingResult result = new AlertingResult();
        for (final String provider : providers) {
            result.addProvider(provider);
        }
        return result;
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
