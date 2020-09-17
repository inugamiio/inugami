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

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceException;

import io.inugami.api.dao.DaoEntityExistsException;
import io.inugami.api.dao.DaoEntityNotFoundException;
import io.inugami.api.dao.DaoException;
import io.inugami.api.exceptions.Asserts;

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
    private final EntityManager     entityManager;
    
    private final EntityTransaction transaction;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public TransactionRunner(EntityManager entityManager) {
        super();
        Asserts.notNull("entity manager must'nt be null!", entityManager);
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
        }
        catch (EntityExistsException e) {
            rollback();
            throw new DaoEntityExistsException(e.getMessage(),e);
        }
        catch (EntityNotFoundException e) {
            rollback();
            throw new DaoEntityNotFoundException(e.getMessage(),e);
        }
        catch (Exception e) {
            rollback();
            manageCauseError(e);
        }
        finally {
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
        }
        catch (Exception e) {
            rollback();
            manageCauseError(e);
        }
        finally {
            flush();
        }
    }
    
    private void manageCauseError(Exception e) throws DaoException {
        if (e.getCause() == null) {
            throw new DaoException(e.getMessage(), e);
        }
        else {
            if (e.getCause() instanceof PersistenceException) {
                throw new DaoException(e.getCause().getCause().getMessage(), e.getCause().getCause());
            }
            else {
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
            }
            catch (Exception e) {
                manageCauseError(e);
            }
        }
    }
    
    private void flush() throws DaoException {
        txBegin();
        entityManager.flush();
        try {
            txEnd();
        }
        catch (Exception e) {
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
