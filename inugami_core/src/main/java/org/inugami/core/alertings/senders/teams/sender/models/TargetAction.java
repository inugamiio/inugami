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
package org.inugami.core.alertings.senders.teams.sender.models;

/**
 * TargetAction
 * 
 * @author patrick_guillerm
 * @since 21 août 2018
 */
public class TargetAction {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String os;
    
    private String uri;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public TargetAction() {
    }
    
    public TargetAction(final String uri) {
        this.os = "default";
        this.uri = uri;
    }
    
    public TargetAction(final String os, final String uri) {
        this.os = os;
        this.uri = uri;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((os == null) ? 0 : os.hashCode());
        result = (prime * result) + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof TargetAction)) {
            final TargetAction other = (TargetAction) obj;
            //@formatter:off
            result =   ( os  == null?other.getOs() ==null:os.equals(other.getOs()))
                    && ( uri == null?other.getUri()==null:uri.equals(other.getUri()));
            //@formatter:on            
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TargetAction [os=");
        builder.append(os);
        builder.append(", uri=");
        builder.append(uri);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getOs() {
        return os;
    }
    
    public void setOs(final String os) {
        this.os = os;
    }
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(final String uri) {
        this.uri = uri;
    }
    
}
