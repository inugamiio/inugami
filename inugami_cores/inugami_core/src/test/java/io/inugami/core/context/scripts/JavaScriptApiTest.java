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
package io.inugami.core.context.scripts;

import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.core.context.ApplicationContext;
import io.inugami.core.context.Context;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JavaScriptDashboardTvApiTest
 *
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
class JavaScriptApiTest implements TestUnitResources {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testBuildSimpleEvent() throws Exception {
        final ApplicationContext ctx = Context.initializeStandalone();
        final String ref = FilesUtils.readContent(FilesUtils.buildFile(initResourcesPath(),
                                                                       "testBuildSimpleEvent-ref.js"));
        final File   file = FilesUtils.buildFile(initResourcesPath(), "testBuildSimpleEvent.js");
        final String js   = FilesUtils.readContent(file);

        final String data = ctx.getScriptEngine().execute(js, "checkSimpleEvent");
        assertNotNull(data);
        assertEquals(ref, data);
    }

    @Test
    void testBuildSimpleEventQuick() throws Exception {
        final ApplicationContext ctx  = Context.initializeStandalone();
        final String             js   = "function checkSimpleEventQuick() {return io.inugami.builders.buildSimpleEvent('name','provider','query','from','until')}";
        final String             data = ctx.getScriptEngine().execute(js, "checkSimpleEventQuick");
        assertNotNull(data);
        assertEquals(
                "{\"name\":\"name\",\"from\":\"from\",\"until\":\"until\",\"provider\":\"provider\",\"mapper\":null,\"query\":\"query\",\"scheduler\":null,\"parent\":null}",
                data);

    }

    @Test
    void testBuildEvent() throws Exception {
        final ApplicationContext ctx = Context.initializeStandalone();
        final String ref = FilesUtils
                .readContent(FilesUtils.buildFile(initResourcesPath(), "testBuildEvent-ref.js"));
        final File   file = FilesUtils.buildFile(initResourcesPath(), "testBuildEvent.js");
        final String js   = FilesUtils.readContent(file);

        final String data = ctx.getScriptEngine().execute(js, "checkEvent");
        assertNotNull(data);
        assertEquals(ref, data);
    }

    // =========================================================================
    // cleanTarget
    // =========================================================================
    @Test
    void testCleanTarget() throws Exception {
        final StringBuilder query = new StringBuilder();

        query.append(
                     "        aliasByNode(maxSeries(summarize(org.foo.serverA.cpu-*.cpu-user,     '5min', 'avg', true)), 3)")
             .append("\n");
        query.append(
                     " @target=aliasByNode(maxSeries(summarize(org.foo.serverB.cpu-*.cpu-user,     '5min', 'avg', true)), 3)         ")
             .append("\n");
        query.append(
                     "@target=aliasByNode(maxSeries(summarize(org.foo.serverC.cpu-*.cpu-user,   '5min', 'avg', true)), 3)   ")
             .append("\n");
        query.append(
                     "    @target=aliasByNode(maxSeries(summarize(org.foo.serverD.cpu-*.cpu-user,      '5min', 'avg', true)), 3) ")
             .append("\n");

        final String cleanQuery = JavaScriptApi.cleanTarget(query.toString());
        assertNotNull(cleanQuery);
    }

}
