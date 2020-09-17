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
package io.inugami.commons.engine;

import io.inugami.api.functionnals.FunctionMustThrow;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.models.tools.Chrono;
import io.inugami.commons.files.FilesUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JavaScriptEngineTest
 *
 * @author patrickguillerm
 * @since 20 déc. 2017
 */
public class JavaScriptEngineTest implements FunctionMustThrow {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaScriptEngineTest.class);

    private static final File RESOURCES = initResourcesPath();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private static File initResourcesPath() {
        final File   file       = new File(".");
        final String currentDir = file.getAbsoluteFile().getParentFile().getAbsolutePath();
        return new File(currentDir + "/src/test/resources");
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testCheckScriptInnerFunction() throws Exception {
        final JavaScriptEngine engine = new JavaScriptEngine();

        engine.checkScriptInnerFunction("return 2");
        engine.checkScriptInnerFunction("return {'a':1234}");

        engine.checkScriptInnerFunction(readFile("JavaScriptEngineA.js"));
        mustThrow(() -> engine.checkScriptInnerFunction("if("));
        mustThrow(() -> engine.checkScriptInnerFunction("0/0"));
    }

    @Test
    public void testCheckScript() throws Exception {
        final JavaScriptEngine engine = new JavaScriptEngine();
        engine.checkScript(readFile("JavaScriptEngineA.js"));
        mustThrow(() -> engine.checkScript("return 2"));

        mustThrow(() -> engine.checkScript("{\"foo\":2,\"titi\":}"));

    }

    @Test
    public void testBuildFunction() throws Exception {
        final JavaScriptEngine engine = new JavaScriptEngine();
        assertEquals("function foobar(){}", engine.buildFunction("foobar", ""));
        assertEquals("function foobar(a,b){}", engine.buildFunction("foobar", "", "a", "b"));
        assertEquals("function foobar(a,b){return a+b;}", engine.buildFunction("foobar", "return a+b;", "a", "b"));
    }

    @Test
    public void textExecute() throws Exception {
        final JavaScriptEngine engine = new JavaScriptEngine();
        final JsonBuilder      js     = new JsonBuilder();

        js.writeFunction("test");
        js.openObject();
        js.addReturnKeyword();
        js.write(2);
        js.addEndLine();
        js.closeObject();
        final String function = js.toString();

        final Chrono chrono1 = Chrono.startChrono();
        final String result1 = engine.execute(function, "test");
        chrono1.stop();
        LOGGER.info("[{}ms] : {}", chrono1.getDelaisInMillis(), result1);
        assertEquals("2", result1);

        final Chrono chrono2 = Chrono.startChrono();
        final String result2 = engine.execute(function, "test");
        chrono2.stop();
        assertEquals("2", result2);
        LOGGER.info("[{}ms] : {}", chrono2.getDelaisInMillis(), result2);

        final Chrono chrono3 = Chrono.startChrono();
        final String result3 = engine.execute(function, "test");
        chrono3.stop();
        assertEquals("2", result3);
        LOGGER.info("[{}ms] : {}", chrono3.getDelaisInMillis(), result3);
    }

    @Test
    public void testRegex() throws Exception {
        final JavaScriptEngine engine = new JavaScriptEngine();
        engine.checkScript(readFile("checkRegex.js"));
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private String readFile(final String path) throws IOException {
        return FilesUtils.readContent(FilesUtils.buildFile(RESOURCES, path));
    }
}
