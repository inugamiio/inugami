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
package org.inugami.core.cdi.services.dao;

import org.inugami.api.dao.Identifiable;

/**
 * TestEntity
 * 
 * @author patrick_guillerm
 * @since 11 janv. 2018
 */
public class TestEntity implements Identifiable<Long> {
    
    private static final long serialVersionUID = -677360731645498181L;
    
    private Long              uid;
    
    @Override
    public Long getUid() {
        return uid;
    }
    
    @Override
    public void setUid(Long uid) {
        this.uid = uid;
    }
    
    @Override
    public boolean isUidSet() {
        return uid != null;
    }
    
}
