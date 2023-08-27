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

import java.io.Serializable;

import javax.script.ScriptException;

/**
 * ScriptCheckingDto
 * 
 * @author patrick_guillerm
 * @since 2 févr. 2018
 */
public final class ScriptCheckingDto implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long     serialVersionUID = -2460034409919683864L;
    
    private final ScriptException error;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ScriptCheckingDto() {
        super();
        error = null;
    }
    
    public ScriptCheckingDto(final ScriptException error) {
        super();
        this.error = error;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ScriptCheckingDto [error=");
        builder.append(error == null ? null : error.getMessage());
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public ScriptException getError() {
        return error;
    }
    
}
