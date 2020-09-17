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
package io.inugami.core.cdi.services.dao;

import java.util.List;

import org.picketlink.Identity;

/**
 * CrudSecurityHandler
 * 
 * @author patrick_guillerm
 * @since 9 janv. 2018
 */
public interface CrudSecurityHandler<E> {
    
    default void onFindAll(Identity identity) {
        // nothing to do
    }
    
    default void onFind(Identity identity) {
        // nothing to do
    }
    
    default void onCount(Identity identity) {
        // nothing to do
    }
    
    default void onGet(Identity identity, String uid) {
        // nothing to do
    }
    
    default void onSave(Identity identity, List<E> listEntity) {
        // nothing to do
    }
    
    default void onMerge(Identity identity, List<E> listEntity) {
        // nothing to do
    }
    
    default void onRegister(Identity identity, List<E> listEntity) {
        // nothing to do
    }
    
    default void onDelete(Identity identity, String uid) {
        // nothing to do
    }
    
    default void onDelete(Identity identity, List<String> uids) {
        // nothing to do
    }
    
}
