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
package org.inugami.commons.engine.js.objects;

import java.util.Optional;

/**
 * JsNamespaceFunction
 * 
 * @author patrick_guillerm
 * @since 26 déc. 2017
 */
public class JsNamespaceFunction {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String namespace;
    
    private final String function;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public JsNamespaceFunction(final String fullName) {
        if (fullName.contains(".")) {
            final String[] parts = fullName.split("[.]");
            function = parts[parts.length - 1];
            final StringBuilder namespacePackage = new StringBuilder();
            for (int i = 0; i < (parts.length - 1); i++) {
                if (i != 0) {
                    namespacePackage.append(".");
                }
                namespacePackage.append(parts[i]);
            }
            namespace = namespacePackage.toString();
        }
        else {
            function = fullName;
            namespace = null;
        }
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Optional<String> getNamespace() {
        return Optional.ofNullable(namespace);
    }
    
    public String getFunction() {
        return function;
    }
    
}
