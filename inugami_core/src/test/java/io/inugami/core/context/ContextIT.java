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
package io.inugami.core.context;

import io.inugami.api.listeners.EngineListener;
import io.inugami.api.providers.concurrent.ThreadSleep;
import io.inugami.core.services.cache.CacheTypes;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Integration TESTS
 *
 * @author patrick_guillerm
 * @since 16 janv. 2017
 */
public class ContextIT implements EngineListener {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger LOGGER = LoggerFactory.getLogger(ContextIT.class);

    private final static Logger LOGGER_READ = LoggerFactory.getLogger("reader-thread");

    // =========================================================================
    // INTEGRATION TEST CACHE
    // =========================================================================
    @Test
    public void testCacheTTL() throws Exception {
        final String key = "foobar";
        ctx().getCache().put(CacheTypes.IO_QUERIES, key, "hello the world");
        assertNotNull(ctx().getCache().get(CacheTypes.IO_QUERIES, key));
        final Thread thread = new testCacheTTLThreadReader();
        thread.start();
        new ThreadSleep(32500).sleep();
        assertNull(ctx().getCache().get(CacheTypes.IO_QUERIES, key));

    }

    private class testCacheTTLThreadReader extends Thread {

        @Override
        public void run() {
            long time = 32000;

            while (time >= 0) {
                final String value = ctx().getCache().get(CacheTypes.IO_QUERIES, "foobar");
                LOGGER_READ.info("[{}]read.... {}", time, value);
                new ThreadSleep(1000).sleep();
                time -= 1000;
            }

        }

    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private ApplicationContext ctx() {
        return Context.getInstance(this);
    }
}
