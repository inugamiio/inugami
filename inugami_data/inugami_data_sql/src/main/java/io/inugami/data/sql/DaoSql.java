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
package io.inugami.data.sql;

import io.inugami.api.dao.*;
import io.inugami.api.dao.event.BeforeMerge;
import io.inugami.api.dao.event.BeforeSave;
import io.inugami.api.dao.event.BeforeSaveOrMerge;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.JsonBuilder;
import io.inugami.commons.security.SecurityTools;
import io.inugami.core.cdi.services.dao.transactions.TransactionRunner;
import io.inugami.data.commons.exceptions.DaoValidatorException;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DaoSql
 *
 * @author patrick_guillerm
 * @since 11 janv. 2018
 */
@SuppressWarnings({"java:S2077"})
@Named
public class DaoSql implements Dao {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -79030053099256219L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Dao.class);

    protected static final String SQL_COUNT_BEGIN = "SELECT COUNT(e.id) ";

    protected static final String SQL_COUNT = SQL_COUNT_BEGIN + " FROM %s e";

    private final int limiteMaxResult = 5000;

    @Inject
    private transient EntityManager entityManager;

    @Inject
    private transient Validator validator;

    private transient TransactionRunner transactionRunner;

    // =========================================================================
    // INITIALIZE
    // =========================================================================

    @PostConstruct
    public void initialize() {
        transactionRunner = new TransactionRunner(entityManager);
    }

    // =========================================================================
    // METHODS
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    E getByUid(final Class<? extends E> type, final PK uid) throws DaoException {
        //@formatter:on
        LOGGER.debug("getByUid");
        final E entity = getEntityInstance(type);
        entity.setUid(uid);
        return get(entity);
    }

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    List<E> getByUids(final Class<? extends E> type, final List<PK> uids) throws DaoException {
        //@formatter:on
        final E entity = getEntityInstance(type);

        final CriteriaBuilder            criteria = entityManager.getCriteriaBuilder();
        final CriteriaQuery<? extends E> query    = criteria.createQuery(type);
        final Root<? extends E>          root     = query.from(type);
        query.where(root.get(entity.uidFieldName()).in(uids));

        List<E> result = null;
        try {
            result = (List<E>) entityManager.createQuery(query).getResultList();
        } catch (final IllegalStateException | PersistenceException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.DEBUG.error("can't find uids : {}", uids);
        }

        return Optional.ofNullable(result).orElse(new ArrayList<>());
    }

    /*
     * @throws IllegalStateException if called for a Java Persistence query
     * language UPDATE or DELETE statement
     *
     * @throws QueryTimeoutException if the query execution exceeds the query
     * timeout value set and only the statement is rolled back
     *
     * @throws TransactionRequiredException if a lock mode other than
     * <code>NONE</code> has been set and there is no transaction or the
     * persistence context has not been joined to the transaction
     *
     * @throws PessimisticLockException if pessimistic locking fails and the
     * transaction is rolled back
     *
     * @throws LockTimeoutException if pessimistic locking fails and only the
     * statement is rolled back
     *
     * @throws PersistenceException if the query execution exceeds the query
     * timeout value set and the transaction is rolled back
     */

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    E get(final E entity) throws DaoException {
        //@formatter:on
        LOGGER.debug("get entity");
        E result = null;

        Serializable id = null;
        if (entity != null) {
            id = entity.getUid();
        }

        if (id != null) {
            result = (E) entityManager.find(entity.getClass(), id);
            if (result == null) {
                throw new DaoEntityNotFoundException("can't get entity with uid : {0}", id);
            }
        }
        return result;
    }

    // =====================================================================
    // REFRESH
    // =====================================================================

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    void refresh(final E entity) throws DaoException {
        //@formatter:on
        LOGGER.debug("refresh");
        if (entityManager.contains(entity)) {
            transactionRunner.process(() -> entityManager.refresh(entity));
        }
    }

    // =====================================================================
    // SAVE
    // =====================================================================

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    void save(final E entity, final Class<? extends E> type) throws DaoException {
        //@formatter:on
        LOGGER.debug("save entity");
        checkIfObjectNull(entity);
        onBeforeSave(entity);
        validateEntity(entity);
        transactionRunner.process(() -> entityManager.persist(entity));
        flush();
    }

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    void save(final List<E> listEntity, final Class<? extends E> type) throws DaoException {
        //@formatter:on
        LOGGER.debug("save list entities");
        checkIfObjectNull(listEntity);
        for (final E entity : listEntity) {
            this.onBeforeSave(entity);
        }
        validateEntities(listEntity);

        transactionRunner.process(() -> {
            for (final E entity : listEntity) {
                checkIfObjectNull(entity);
                entityManager.persist(entity);
            }
        });
        flush();
    }

    private <E extends Identifiable<PK>, PK extends Serializable> void onBeforeSave(final E entity) {
        if (entity instanceof BeforeSaveOrMerge) {
            ((BeforeSaveOrMerge) entity).onBeforeSaveOrMerge();
        }
        if (entity instanceof BeforeSave) {
            ((BeforeSave) entity).onBeforeSave();
        }
    }

    // =====================================================================
    // MERGE
    // =====================================================================

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    E merge(final E entity, final Class<? extends E> type) throws DaoException {
        //@formatter:on
        LOGGER.debug("merge entity");
        checkIfObjectNull(entity);
        onBeforeMerge(entity);
        validateEntity(entity);

        final E result = transactionRunner.processWithReturn(() -> entityManager.merge(entity));
        flush();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    //@formatter:off    
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    void merge(final List<E> listEntity, final Class<? extends E> type) throws DaoException {
        //@formatter:on
        LOGGER.debug("merge list entities");
        checkIfObjectNull(listEntity);
        for (final E entity : listEntity) {
            this.onBeforeMerge(entity);
        }
        validateEntities(listEntity);
        transactionRunner.process(() -> {
            for (final E entity : listEntity) {
                entityManager.merge(entity);
            }
        });
        flush();
    }

    private <E extends Identifiable<PK>, PK extends Serializable> void onBeforeMerge(final E entity) {
        if (entity instanceof BeforeSaveOrMerge) {
            ((BeforeSaveOrMerge) entity).onBeforeSaveOrMerge();
        }
        if (entity instanceof BeforeMerge) {
            ((BeforeMerge) entity).onBeforeMerge();
        }
    }

    // =====================================================================
    // REGISTER
    // =====================================================================
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable> SaveEntitiesResult<E> register(final List<E> entities,
                                                                                                final Class<? extends E> type) throws DaoException {

        final List<PK> uids          = extractUids(entities);
        final List<E>  savedEntities = getByUids(type, uids);

        final List<E> entitiesToMerge = new ArrayList<>();
        final List<E> entitiesToSave  = new ArrayList<>();

        for (final E entity : entities) {
            if (savedEntities.contains(entity)) {
                entitiesToMerge.add(entity);
            } else {
                entitiesToSave.add(entity);
            }
        }
        save(entitiesToSave, type);
        merge(entitiesToMerge, type);

        final SaveEntitiesResult<E> result = new SaveEntitiesResult<>();
        result.addAllNewEntities(entitiesToSave);
        result.addAllMergeEntities(entitiesToMerge);
        return result;
    }

    protected <E extends Identifiable<PK>, PK extends Serializable> List<PK> extractUids(final List<E> listEntity) {
        //@formatter:off
        return Optional.ofNullable(listEntity).orElse(new ArrayList<>())
                       .stream()
                       .map(Identifiable::getUid)
                       .collect(Collectors.toList());
        //@formatter:on
    }

    // =====================================================================
    // FIND
    // =====================================================================

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    List<E> findAll(final Class<? extends E> type) throws DaoException {
        //@formatter:on
        LOGGER.debug("find all");

        final Query query = entityManager.createQuery("from " + StringEscapeUtils.escapeSql(type.getName()));

        List<E> result = null;
        if (query != null) {
            result = query.getResultList();
            query.setMaxResults(limiteMaxResult);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    List<E> find(final Class<? extends E> type, final int first, final int pageSize, final String field, final String sortOrder, final Map<String, String> filters) throws DaoException {
        //@formatter:on
        final Object[] params = {type, first, pageSize, field, sortOrder, filters};
        LOGGER.debug("find (type:{}|first:{}|pageSize:{}|field:{}|sortOrder:{}|filters:{})", params);

        final String sortField = getSortField(field);
        final Query  query     = entityManager.createQuery(queryFind(sortField, sortOrder, filters, type));
        query.setFirstResult(first);

        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    // =====================================================================
    // COUNT
    // =====================================================================

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    int count(final Class<? extends E> type) throws DaoException {
        //@formatter:on
        LOGGER.debug("count");
        return count(null, type);
    }

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    int count(final Map<String, String> filters, final Class<? extends E> type) throws DaoException {
        //@formatter:on
        LOGGER.debug("count(filters:{})", filters);
        int result = 0;

        Query query = null;
        if (filters == null) {
            query = entityManager.createQuery(String.format(SQL_COUNT, type.getName()));
        } else {
            query = entityManager.createQuery(buildCountWithCriteria(filters, type));
        }
        final Number countResult = (Number) query.getSingleResult();
        result = countResult.intValue();

        return result;
    }

    private String buildCountWithCriteria(final Map<String, String> filters, final Class<?> type) throws DaoException {
        return String.join(" ", SQL_COUNT_BEGIN, queryFindCriteria(filters, type));
    }

    // =====================================================================
    // DELETE
    // =====================================================================

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    void delete(final E entity, final Class<? extends E> type) throws DaoEntityNotFoundException, DaoException {
        //@formatter:on
        LOGGER.debug("delete");
        if (entityManager.contains(entity)) {
            transactionRunner.process(() -> {
                entityManager.remove(entity);
            });

        } else {
            // could be a delete on a transient instance
            final E entityRef = (E) entityManager.getReference(entity.getClass(), entity.getUid());

            if (entityRef != null) {
                transactionRunner.process(() -> entityManager.remove(entityRef));

            } else {
                LOGGER.warn("Attempt to delete an instance that is not present in the database: {}", entity);
            }
        }
        flush();
    }

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    void delete(final PK uid, final Class<? extends E> type) throws DaoEntityNotFoundException, DaoException {
        //@formatter:on
        LOGGER.debug("delete(uid:{}", uid);
        final E entity = getByUid(type, uid);
        if (entity != null) {
            delete(entity, type);
        }
    }

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    void delete(final List<E> entities, final Class<? extends E> type) throws DaoEntityNotFoundException, DaoException {
        //@formatter:on     
        checkIfObjectNull(entities);

        transactionRunner.process(() -> {
            entities.forEach(entityManager::remove);
        });
    }

    /**
     * {@inheritDoc}
     */
    //@formatter:off
    @Override
    public <E extends Identifiable<PK>, PK extends Serializable>
    void deleteByIds(final List<PK> uids, final Class<? extends E> type) throws DaoEntityNotFoundException, DaoException {
        //@formatter:on
        checkIfObjectNull(uids);

        final List<E> entities = new ArrayList<>();

        for (final PK uid : uids) {
            final E entity = getByUid(type, uid);
            if (entity == null) {
                throw new DaoEntityNotFoundException("can't found entity with uid : {0}", uid);
            }
            entities.add(entity);
        }

        delete(entities, type);
    }

    // =========================================================================
    // UTILS
    // =========================================================================
    private <E extends Identifiable<PK>, PK extends Serializable> void validateEntity(final E entity) throws DaoException {
        final Set<ConstraintViolation<E>> errors = validator.validate(entity);
        if (!errors.isEmpty()) {
            throw new DaoValidatorException(errors);
        }
    }

    private <E extends Identifiable<PK>, PK extends Serializable> void validateEntities(final List<E> entities) throws DaoValidatorException {
        JsonBuilder error = null;

        for (final E entity : entities) {
            final Set<ConstraintViolation<E>> errors = validator.validate(entity);
            if (!errors.isEmpty()) {
                if (error == null) {
                    error = new JsonBuilder();
                    error.openList();
                } else {
                    error.addSeparator();
                }
                error.write(DaoValidatorException.buildMessage(errors));
            }
        }
        if (error != null) {
            error.closeList();
            throw new DaoValidatorException(error.toString());
        }
    }

    public void flush() throws DaoException {
        transactionRunner.process(entityManager::flush);
    }

    /**
     * Allow to get a new entity instance.
     *
     * @param <E>   the entity type
     * @param clazz the entity clazz
     * @return instance of entity
     * @throws DaoException if clazz parameter isn't same than type E
     */
    @SuppressWarnings("unchecked")
    protected <E extends Identifiable<?>> E getEntityInstance(final Class<?> clazz) throws DaoException {
        E result = null;

        try {
            result = (E) clazz.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());

            }
            throw new DaoException(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Check if object null.
     *
     * @param object the object
     * @throws DaoEntityNullException the dao entity null exception
     */
    protected void checkIfObjectNull(final Object object) throws DaoEntityNullException {
        if (object == null) {
            throw new DaoEntityNullException();
        }
    }

    /**
     * Secure query.
     *
     * @param value the value
     * @return the string
     * @throws DaoException the dao exception
     */
    protected String secureQuery(final String value) throws DaoException {
        if (value == null) {
            return "";
        }
        return SecurityTools.checkInjection(value);
    }

    protected String getSortField(final String field) {
        String sortField = "uid";
        if (field != null) {
            sortField = field;
        }
        return sortField;
    }

    /**
     * Query find.
     *
     * @param sortField the sort field
     * @param sortOrder the sort order
     * @param filters   the filters
     * @param type      the type
     * @return the string
     * @throws DaoException the dao exception
     */
    protected String queryFind(final String sortField, final String sortOrder, final Map<String, String> filters,
                               final Class<?> type) throws DaoException {
        final StringBuilder sql = new StringBuilder();
        sql.append(queryFindCriteria(filters, type));

        sql.append(" ORDER BY  ");
        sql.append("e.").append(secureQuery(sortField)).append(' ');
        sql.append(secureQuery(sortOrder));
        return SecurityTools.escapeSql(sql.toString());
    }

    private String queryFindCriteria(final Map<String, String> filters, final Class<?> type) throws DaoException {
        final StringBuilder sql = new StringBuilder();
        sql.append("FROM ");
        sql.append(type.getName());
        sql.append(" e ");

        if ((filters != null) && !filters.isEmpty()) {
            sql.append(" WHERE ");

            final Iterator<String> iterator = filters.keySet().iterator();

            while (iterator.hasNext()) {
                final String key = iterator.next();
                sql.append(" e." + key);
                sql.append(" like ");
                sql.append(" '%");
                sql.append(secureQuery(filters.get(key)));
                sql.append("%'");

                if (iterator.hasNext()) {
                    sql.append(" AND ");
                }
            }
        }
        return SecurityTools.escapeSql(sql.toString());
    }

}
