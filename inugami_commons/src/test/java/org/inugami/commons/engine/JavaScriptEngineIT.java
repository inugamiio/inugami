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
package org.inugami.commons.engine;

import java.io.File;
import java.io.IOException;

import org.inugami.commons.files.FilesUtils;

/**
 * JavaScriptEngineIT
 * 
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
public class JavaScriptEngineIT {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final File RESOURCES = initResourcesPath();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private static File initResourcesPath() {
        final File file = new File(".");
        final String currentDir = file.getAbsoluteFile().getParentFile().getAbsolutePath();
        return new File(currentDir + "/src/test/resources");
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static void main(final String[] args) throws Exception {
        final JavaScriptEngine engine = new JavaScriptEngine();
        engine.checkScript(readFile("JavaScriptEngineIT.js"));
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    private static String readFile(final String path) throws IOException {
        return FilesUtils.readContent(FilesUtils.buildFile(RESOURCES, path));
    }
}
