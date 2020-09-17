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
package io.inugami.commons.tools;

import java.io.File;

import io.inugami.commons.files.FilesUtils;

/**
 * TestUnitResources
 * 
 * @author patrickguillerm
 * @since 23 déc. 2017
 */
public interface TestUnitResources {
    default File initResourcesPath() {
        final File file = new File(".");
        
        return FilesUtils.buildFile(file.getAbsoluteFile().getParentFile(), "src", "test", "resources");
    }
}
