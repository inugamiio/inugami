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
package io.inugami.interfaces.models.maven;

/**
 * MavenData
 * 
 * @author patrick_guillerm
 * @since 12 oct. 2016
 */
public class MavenData {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String groupId;
    
    private final String artifactId;
    
    private final String version;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MavenData(final String groupId, final String artifactId, final String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getGroupId() {
        return groupId;
    }
    
    public String getArtifactId() {
        return artifactId;
    }
    
    public String getVersion() {
        return version;
    }
}
