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
package io.inugami.api.models.events;

import io.inugami.api.processors.ProcessorModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * SimpleEventTest
 *
 * @author patrick_guillerm
 * @since 10 oct. 2017
 */
public class SimpleEventTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testCloneObj() {

        final SimpleEvent event = new SimpleEvent("event-name", "-10min", "-5min", "provider", buildProcessors(),
                                                  "query", "parent", "scheduler", "mapper", buildAlertings());

        final SimpleEvent newEvent = (SimpleEvent) event.cloneObj();

        assertFalse(event == newEvent);
        assertEquals(event.getName(), newEvent.getName());
        assertEquals(event.getProvider().get(), newEvent.getProvider().get());
        assertEquals(event.getFrom().get(), newEvent.getFrom().get());
        assertEquals(event.getQuery(), newEvent.getQuery());
        assertEquals(1, newEvent.getProcessors().get().size());
        assertEquals(event.getMapper().get(), newEvent.getMapper().get());
        assertEquals(1, newEvent.getAlertings().get().size());
    }

    private List<ProcessorModel> buildProcessors() {
        final List<ProcessorModel> result = new ArrayList<>();
        result.add(new ProcessorModel("processor_name", "foo.bar.Processor"));
        return result;
    }

    private List<AlertingModel> buildAlertings() {
        final List<AlertingModel> result = new ArrayList<>();
        result.add(new AlertingModel());
        return result;
    }
}
