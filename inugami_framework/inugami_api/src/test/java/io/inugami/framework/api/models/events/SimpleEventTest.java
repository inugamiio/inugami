/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.framework.api.models.events;

import io.inugami.framework.interfaces.models.event.AlertingModel;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * SimpleEventTest
 *
 * @author patrick_guillerm
 * @since 10 oct. 2017
 */
class SimpleEventTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testCloneObj() {

        final SimpleEvent event = SimpleEvent.builder()
                                             .name("event-name")
                                             .fromFirstTime("-10min")
                                             .from("-5min")
                                             .processors(buildProcessors())
                                             .query("query")
                                             .scheduler("scheduler")
                                             .mapper("mapper")
                                             .alertings(buildAlertings())
                                             .provider("provider")
                                             .build();

        final SimpleEvent newEvent = event.cloneObj();

        assertNotSame(event, newEvent);
        assertEquals(event.getName(), newEvent.getName());
        assertEquals(event.getProvider(), newEvent.getProvider());
        assertEquals(event.getFrom(), newEvent.getFrom());
        assertEquals(event.getQuery(), newEvent.getQuery());
        assertEquals(1, newEvent.getProcessors().size());
        assertEquals(event.getMapper(), newEvent.getMapper());
        assertEquals(1, newEvent.getAlertings().size());
    }

    private List<ProcessorModel> buildProcessors() {
        final List<ProcessorModel> result = new ArrayList<>();
        result.add(ProcessorModel.builder()
                                 .name("processor_name")
                                 .className("foo.bar.Processor")
                                 .build());
        return result;
    }

    private List<AlertingModel> buildAlertings() {
        final List<AlertingModel> result = new ArrayList<>();
        result.add(new AlertingModel());
        return result;
    }
}
