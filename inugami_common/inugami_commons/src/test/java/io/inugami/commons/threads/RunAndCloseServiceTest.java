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
package io.inugami.commons.threads;

import io.inugami.api.models.tools.Chrono;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RunAndCLoseServiceTest
 *
 * @author patrickguillerm
 * @since 24 mars 2018
 */
class RunAndCloseServiceTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger LOGGER = LoggerFactory.getLogger(RunAndCloseServiceTest.class.getSimpleName());

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testWithoutTimeout() {
        LOGGER.info("========== testWithoutTimeout ==========");
        //@formatter:off
        final Chrono chrono = Chrono.startChrono();
        final List<String> data = new RunAndCloseService<>("test",
                                                      5000L,
                                                      2, 
                                                      new SimpleTask(500,"1"),
                                                      new SimpleTask(100,"2"),
                                                      new SimpleTask(1000,"3"),
                                                      new SimpleTask(700,"4")
                                                     ).run();
        //@formatter:on
        chrono.stop();
        LOGGER.info("duration : {}", chrono.getDelaisInMillis());
        assertTrue(chrono.getDelaisInMillis() < 5000L);
        data.forEach(m -> LOGGER.info("number : {}", m));
        assertListEquals(data, "2", "1", "3", "4");
    }

    @Test
    void testWithTimeout() {
        LOGGER.info("========== testWithTimeout ==========");
        //@formatter:off
        final Chrono chrono = Chrono.startChrono();
        final List<String> data = new RunAndCloseService<>("test",
                                                      2000L,
                                                      2, 
                                                      new SimpleTask(500,"1"),
                                                      new SimpleTask(10000,"2"),
                                                      new SimpleTask(1000,"3"),
                                                      new SimpleTask(600,"4")
                                                     ).run();
        //@formatter:on
        chrono.stop();
        LOGGER.info("duration : {}", chrono.getDelaisInMillis());
        assertTrue(chrono.getDelaisInMillis() >= 2000L);
        assertTrue(chrono.getDelaisInMillis() < 2050L);
        data.forEach(m -> LOGGER.info("number : {}", m));

        assertEquals("1", data.get(0));
        assertEquals("3", data.get(1));
        if ("timeout - 2".equals(data.get(2))) {
            assertEquals("timeout - 4", data.get(3));
        } else {
            assertEquals("timeout - 2", data.get(3));
        }
    }

    @Test
    void testWithTimeoutAndErrorHandler() {
        LOGGER.info("========== testWithTimeoutAndErrorHandler ==========");
        final BiFunction<Exception, Callable<String>, String> onError = (error, task) -> {
            String result = "null";
            if (task instanceof CallableWithErrorResult) {
                result = ((CallableWithErrorResult<String>) task).getTimeoutResult();
            }
            return result;
        };
        //@formatter:off
        final Chrono chrono = Chrono.startChrono();
        final List<String> data = new RunAndCloseService<>("test",
                                                      2000L,
                                                      2, 
                                                      onError,
                                                      new SimpleTask(500,"1"),
                                                      new SimpleTask(10000,"2"),
                                                      new SimpleTask(1000,"3"),
                                                      new SimpleTask(700,"4")
                                                     ).run();
        //@formatter:on
        chrono.stop();
        LOGGER.info("duration : {}", chrono.getDelaisInMillis());
        assertTrue(chrono.getDelaisInMillis() >= 2000L);
        assertTrue(chrono.getDelaisInMillis() < 2050L);
        data.forEach(m -> LOGGER.info("number : {}", m));
        assertEquals("1", data.get(0));
        assertEquals("3", data.get(1));
        if ("timeout - 2".equals(data.get(2))) {
            assertEquals("timeout - 4", data.get(3));
        } else {
            assertEquals("timeout - 2", data.get(3));
        }
    }

    @Test
    void testWithSubTask() {
        LOGGER.info("========== testWithSubTask ==========");
        //@formatter:off
        final Chrono chrono = Chrono.startChrono();
        final List<String> data = new RunAndCloseService<>("test",
                                                      2000L,
                                                      2, 
                                                      new ComplexTask("1"),
                                                      new ComplexTask("2")
                                                     ).run();
        //@formatter:on
        chrono.stop();
        LOGGER.info("duration : {}", chrono.getDelaisInMillis());
        data.forEach(m -> LOGGER.info("number : {}", m));
        //@formatter:off
        assertListEquals(data, "1.2 | 1.1 | error - 1.4 | 1.3",
                                "2.1 | 2.2 | 2.4 | timeout - 2.3");
        //@formatter:on
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private void assertListEquals(final List<String> data, final String... ref) {
        assertNotNull(data);
        assertEquals(data.size(), ref.length);
        for (int i = 0; i < ref.length; i++) {
            assertEquals(ref[i], data.get(i));
        }
    }

}
