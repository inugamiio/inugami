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
package io.inugami.webapp.services;

import io.inugami.api.models.Gav;

/**
 * CallPluginEventDto
 * 
 * @author patrick_guillerm
 * @since 29 janv. 2018
 */
public class CallPluginEventDto {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Gav    gav;
    
    private final String excludeRegex;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CallPluginEventDto(Gav gav, String excludeRegex) {
        super();
        this.gav = gav;
        this.excludeRegex = excludeRegex;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((excludeRegex == null) ? 0 : excludeRegex.hashCode());
        result = prime * result + ((gav == null) ? 0 : gav.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        
        if (!result && obj != null && obj instanceof CallPluginEventDto) {
            final CallPluginEventDto other = (CallPluginEventDto) obj;
            
            boolean sameGav = gav == null ? other.getGav() == null : gav.buildHash().equals(other.getGav().buildHash());
            boolean sameExclude = excludeRegex == null ? other.getExcludeRegex() == null
                                                       : excludeRegex.equals(other.getExcludeRegex());
            
            result = sameGav && sameExclude;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CallPluginEventDto [gav=");
        builder.append(gav);
        builder.append(", excludeRegex=");
        builder.append(excludeRegex);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Gav getGav() {
        return gav;
    }
    
    public String getExcludeRegex() {
        return excludeRegex;
    }
}
