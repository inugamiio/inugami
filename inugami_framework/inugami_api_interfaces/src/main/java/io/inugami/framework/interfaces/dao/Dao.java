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
package io.inugami.framework.interfaces.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author patrick_guillerm
 * @since 9 janv. 2018
 */
@SuppressWarnings({"java:S119", "java:S1130"})
public interface Dao extends Serializable {
    //@formatter:off
    
    /**
     * Allow to get a entity from uid .
     *
     * @param <E> Entity type
     * @param <PK> Entity UID type
     * @param type entity type class
     * @param uid entity uid
     * @return the entity
     * @throws DaoEntityNotFoundException if entity isn't found in datasource
     * @throws DaoException if exception is occurs.
     */
     <E extends Identifiable<PK>, PK extends Serializable>
     E getByUid(final Class<? extends E> type,final PK uid) throws DaoEntityNotFoundException, DaoException;
    
     /**
      * Allow to get entities by uid
      * @param type Entities type
      * @param uids  entities uids
      * @return empty or list of entities
      * @throws DaoEntityNotFoundException if entity isn't found in datasource
      * @throws DaoException if exception is occurs.
      */
     <E extends Identifiable<PK>, PK extends Serializable> 
     List<E> getByUids(final Class<? extends E> type,final List<PK> uids) throws DaoEntityNotFoundException, DaoException;
     
    /**
     * Allow to get a entity.
     *
     * @param <E> Entity type
     * @param entity reference
     * @return the entity
     * @throws DaoEntityNotFoundException if entity isn't found in datasource
     * @throws DaoException if exception is occur.
     */
    <E extends Identifiable<PK>, PK extends Serializable> 
    E get(final E entity) throws DaoEntityNotFoundException,DaoException;
    
    // =====================================================================
    // REFRESH
    // =====================================================================
    /**
     * Refresh.
     * 
     * @param <E> the element type
     * @param entity the entity
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    void refresh(final E entity) throws DaoEntityNotFoundException,DaoException;
    
    // =====================================================================
    // SAVE
    // =====================================================================
    /**
     * Save.
     * 
     * @param <E> the element type
     * @param entity the entity
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    void save(final E entity, final Class<? extends E> type) throws DaoException;
    
    /**
     * Save.
     * 
     * @param <E> the element type
     * @param listEntity the list entity
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    void save(final List<E> listEntity,final Class<? extends E> type) throws DaoException;
    
    // =====================================================================
    // MERGE
    // =====================================================================
    
    /**
     * Merge.
     * 
     * @param <E> the element type
     * @param entity the entity
     * @return the e
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable> 
    E merge(final E entity, final Class<? extends E> type) throws DaoException;
    
    /**
     * Merge.
     * 
     * @param <E> the element type
     * @param listEntity the list entity
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    void merge(final List<E> listEntity, final Class<? extends E> type) throws DaoException;
    
    
    // =====================================================================
    // REGISTER
    // =====================================================================
    /**
     * Allow to register and save non existing entities or merge existing. 
     * @param entities  all entities to register
     * @param type entity class type
     * @throws DaoException if excpetion is occur
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    SaveEntitiesResult<E> register(final List<E> entities, final Class<? extends E> type) throws DaoException;
    
    
    // =====================================================================
    // FIND
    // =====================================================================
    
    /**
     * Find all.
     * 
     * @param <E> the element type
     * @param type the type
     * @return the list
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable> List<E> 
    findAll(Class<? extends E> type) throws DaoException;
    
    /**
     * Find.
     * 
     * @param <E> the element type
     * @param type the type
     * @param first the first
     * @param pageSize the page size
     * @param field the field
     * @param sortOrder the sort order
     * @param filters the filters
     * @return the list
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    List<E> find(Class<? extends E> type, final int first, final int pageSize, final String field,final String sortOrder,Map<String, String> filters) throws DaoException;
    
    // =====================================================================
    // COUNT
    // =====================================================================
    /**
     * Count.
     * 
     * @param type the type
     * @return the int
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable> 
    int count(final Class<? extends E> type) throws DaoException;
    
    /**
     * Count.
     * 
     * @param filters the filters
     * @param type the type
     * @return the int
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    int count(final Map<String, String> filters, final Class<? extends E> type) throws DaoException;
    
    // =====================================================================
    // DELETE
    // =====================================================================
    /**
     * Delete.
     * 
     * @param <E> the element type
     * @param <PK> the generic type
     * @param entity the entity
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    void delete(E entity, final Class<? extends E> type) throws DaoEntityNotFoundException, DaoException;
    
    /**
     * Delete.
     * 
     * @param <E> the element type
     * @param <PK> the generic type
     * @param uid the uid
     * @param type the type
     * @throws DaoException the dao exception
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    void delete(final PK uid,final Class<? extends E> type) throws DaoEntityNotFoundException,DaoException;
    
    /**
     * Allow to delete entities
     * 
     * @param entity
     * @param type
     * @throws DaoEntityNotFoundException
     * @throws DaoException
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    void delete(final List<E> entity, final Class<? extends E> type) throws DaoEntityNotFoundException,DaoException;
    
    /**
     * Allow to delete entities by uids
     * 
     * @param uids
     * @param type
     * @throws DaoEntityNotFoundException
     * @throws DaoException
     */
    <E extends Identifiable<PK>, PK extends Serializable>
    void deleteByIds(final List<PK> uids, final Class<? extends E> type) throws DaoEntityNotFoundException,DaoException;
    
    //@formatter:on
}
