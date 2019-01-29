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
package org.inugami.core.security.commons.models;

import java.io.Serializable;
import java.util.Collection;

import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.AttributedType;

/**
 * TokenUser
 * 
 * @author patrickguillerm
 * @since 11 déc. 2017
 */
public class TokenUser implements AttributedType {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 7310635906457250565L;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Override
    public void removeAttribute(String name) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public <T extends Serializable> Attribute<T> getAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Collection<Attribute<? extends Serializable>> getAttributes() {
        // TODO Auto-generated method stub
        return null;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void setAttribute(Attribute<? extends Serializable> attribute) {
        // TODO Auto-generated method stub
        
    }
}
