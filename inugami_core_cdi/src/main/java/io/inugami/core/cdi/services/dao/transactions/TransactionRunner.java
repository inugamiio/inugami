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
package io.inugami.core.cdi.services.dao.transactions;

import io.inugami.api.dao.DaoEntityExistsException;
import io.inugami.api.dao.DaoEntityNotFoundException;
import io.inugami.api.dao.DaoException;
import io.inugami.api.exceptions.Asserts;

import javax.persistence.*;

/**
 * TransactionRunner
 *
 * @author patrick_guillerm
 * @since 26 juil. 2016
 */
public class TransactionRunner {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final EntityManager entityManager;

    private final EntityTransaction transaction;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public TransactionRunner(final EntityManager entityManager) {
        super();
        Asserts.assertNotNull("entity manager must'nt be null!", entityManager);
        this.entityManager = entityManager;
        this.transaction = entityManager.getTransaction();
        entityManager.setFlushMode(FlushModeType.COMMIT);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public void process(final ActionWithTransaction action) throws DaoException {
        processWithReturn(() -> {
            action.process();
            return null;
        });
    }

    public synchronized <T> T processWithReturn(final ActionWithTransactionAndReturn<T> action) throws DaoException {
        T result = null;

        txBegin();
        if (entityManager.getTransaction().getRollbackOnly()) {
            entityManager.getTransaction().rollback();
        }
        try {
            result = action.process();
        } catch (final EntityExistsException e) {
            rollback();
            throw new DaoEntityExistsException(e.getMessage(), e);
        } catch (final EntityNotFoundException e) {
            rollback();
            throw new DaoEntityNotFoundException(e.getMessage(), e);
        } catch (final Exception e) {
            rollback();
            manageCauseError(e);
        } finally {
            commit();
        }

        return result;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private synchronized void commit() throws DaoException {
        try {
            txEnd();
        } catch (final Exception e) {
            rollback();
            manageCauseError(e);
        } finally {
            flush();
        }
    }

    private void manageCauseError(final Exception e) throws DaoException {
        if (e.getCause() == null) {
            throw new DaoException(e.getMessage(), e);
        } else {
            if (e.getCause() instanceof PersistenceException) {
                throw new DaoException(e.getCause().getCause().getMessage(), e.getCause().getCause());
            } else {
                throw new DaoException(e.getCause().getMessage(), e.getCause());
            }
        }
    }

    private void rollback() throws DaoException {
        final boolean isTransactionActive = entityManager.getTransaction().isActive();
        if (!isTransactionActive) {
            txBegin();
        }
        entityManager.getTransaction().rollback();

        if (!isTransactionActive) {
            try {
                txEnd();
            } catch (final Exception e) {
                manageCauseError(e);
            }
        }
    }

    private void flush() throws DaoException {
        txBegin();
        entityManager.flush();
        try {
            txEnd();
        } catch (final Exception e) {
            manageCauseError(e);
        }
    }

    /**
     * Allow to start transaction.
     *
     * @throws DaoException the dao exception
     */
    private void txBegin() throws DaoException {
        if (transaction != null && !entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
    }

    /**
     * Allow to commit current transaction.
     *
     * @throws DaoException the dao exception
     */
    private void txEnd() throws DaoException, Exception {
        if (transaction != null && transaction.isActive()) {
            entityManager.getTransaction().commit();
        }
    }

}
