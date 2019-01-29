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

import java.util.List;

/**
 * PostCrudHandler
 * 
 * @author patrickguillerm
 * @since 21 janv. 2018
 */
public interface PostCrudHandler<E> {
    
    default void onFindAll(List<E> result) {
        // nothing to do
    }
    
    default void onFind(List<E> result) {
        // nothing to do
    }
    
    default void onCount(int result) {
        // nothing to do
    }
    
    default void onGet(E result) {
        // nothing to do
    }
    
    default void onSave(List<E> listEntity) {
        // nothing to do
    }
    
    default void onMerge(List<E> listEntity) {
        // nothing to do
    }
    
    default void onRegister(List<E> listEntity) {
        // nothing to do
    }
    
    default void onDeleteItem(String uid) {
        // nothing to do
    }
    
    default void onDelete(List<String> uids) {
        // nothing to do
    }
    
}
