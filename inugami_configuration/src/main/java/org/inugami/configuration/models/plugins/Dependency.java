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
package org.inugami.configuration.models.plugins;

import org.inugami.api.models.Gav;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Dependency
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2016
 */
@XStreamAlias("dependency")
public class Dependency extends Gav {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 196708674385895250L;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public Dependency(final String groupId, final String artifactId, final String version) {
        super(groupId, artifactId, version, null);
    }
}
