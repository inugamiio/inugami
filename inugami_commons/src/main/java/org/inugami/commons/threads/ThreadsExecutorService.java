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
package org.inugami.commons.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.listeners.TaskFinishListener;
import org.inugami.api.listeners.TaskStartListener;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.tools.Chrono;
import org.inugami.api.providers.concurrent.LifecycleBootstrap;

/**
 * ThreadsExecutorService
 * 
 * @author patrick_guillerm
 * @since 13 janv. 2017
 */
public class ThreadsExecutorService implements LifecycleBootstrap {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    private final long            timeout;
    
    private final String          name;
    
    private final ExecutorService executor;
    
    private final ExecutorService executorCompletable;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ThreadsExecutorService(final String name, final int maxThreads) {
        this(name, maxThreads, false, null);
    }
    
    public ThreadsExecutorService(final String name, final int maxThreads, final boolean deamon) {
        this(name, maxThreads, deamon, null);
    }
    
    public ThreadsExecutorService(final String name, final int maxThreads, final boolean deamon, final Long timeout) {
        this.name = name == null ? "ThreadsExecutor" : name;
        final MonitoredThreadFactory threadFactory = new MonitoredThreadFactory(this.name, deamon);
        executor = Executors.newFixedThreadPool(maxThreads, threadFactory);
        executorCompletable = Executors.newFixedThreadPool(maxThreads, threadFactory);
        
        this.timeout = timeout == null ? 30000L : timeout.longValue();
        
    }
    
    // =========================================================================
    // RUN
    // =========================================================================
    public <T> List<CompletableFuture<T>> run(final List<Callable<T>> tasks) {
        return run(tasks, null, null);
    }
    
    public <T> List<CompletableFuture<T>> run(final List<Callable<T>> tasks, final BiConsumer<T, Callable<T>> onDone) {
        return run(tasks, onDone, null);
    }
    
    public <T> List<CompletableFuture<T>> run(final List<Callable<T>> tasks, final BiConsumer<T, Callable<T>> onDone,
                                              final BiConsumer<Exception, Callable<T>> onError) {
        final List<CompletableFuture<T>> result = new ArrayList<>();
        
        if ((tasks != null) && !tasks.isEmpty()) {
            for (int i = 0; i < tasks.size(); i++) {
                final CompletableFuture<T> future = buildFuture(tasks.get(i), onDone, onError);
                result.add(future);
            }
        }
        
        return result;
    }
    
    // =========================================================================
    // RUN AND GRAB
    // =========================================================================
    public <T> List<T> runAndGrab(final List<Callable<T>> tasks, final long timeout) throws TechnicalException {
        return runAndGrab(tasks, null, null, timeout);
    }
    
    public <T> List<T> runAndGrab(final List<Callable<T>> tasks, final BiConsumer<T, Callable<T>> onDone,
                                  final long timeout) throws TechnicalException {
        return runAndGrab(tasks, onDone, null, timeout);
    }
    
    /**
     * Allow to run and waitting for multi threading processing. TODO doc ...
     * 
     * @param <T> the generic type
     * @param threadName the thread name
     * @param tasks the tasks
     * @param maxThreads the max threads
     * @param timeout the timeout
     * @return the list
     * @throws TechnicalException the technical exception
     */
    public <T> List<T> runAndGrab(final List<Callable<T>> tasks, final BiConsumer<T, Callable<T>> onDone,
                                  final BiConsumer<Exception, Callable<T>> onError,
                                  final long timeout) throws TechnicalException {
        final List<T> result = new ArrayList<>();
        
        final List<CompletableFuture<T>> futures = run(tasks, (data, task) -> {
            result.add(data);
            onDone.accept(data, task);
        }, onError);
        waitting(futures, timeout);
        
        return result;
    }
    
    // =========================================================================
    // WAIT
    // =========================================================================
    public <T> void waitting(final List<CompletableFuture<T>> futures, final long timeout) throws TechnicalException {
        if ((futures != null) && !futures.isEmpty()) {
            
            final Chrono chrono = Chrono.startChrono();
            long localTimeout = timeout;
            
            for (final CompletableFuture<T> future : futures) {
                chrono.snapshot();
                localTimeout = timeout - chrono.getDelaisInMillis();
                
                if (localTimeout > 0) {
                    try {
                        future.get(localTimeout, TimeUnit.MILLISECONDS);
                    }
                    catch (InterruptedException | ExecutionException | TimeoutException e) {
                        Loggers.PLUGINS.error(e.getMessage());
                        throw new TechnicalException(e.getMessage(), e);
                    }
                }
            }
            
        }
    }
    
    // =========================================================================
    // BUILDER
    // =========================================================================
    public <T> CompletableFuture<T> buildFuture(final Callable<T> taskToProcess) {
        return buildFuture(taskToProcess, null, null);
    }
    
    public <T> CompletableFuture<T> buildFuture(final Callable<T> taskToProcess,
                                                final BiConsumer<T, Callable<T>> onDone) {
        return buildFuture(taskToProcess, onDone, null);
    }
    
    public <T> CompletableFuture<T> buildFuture(final Callable<T> taskToProcess,
                                                final BiConsumer<T, Callable<T>> onDone,
                                                final BiConsumer<Exception, Callable<T>> onError) {
        // @formatter:off
		final BiConsumer<T, Callable<T>> functionOnDone = onDone != null ? onDone : (data, task) -> {};
		final BiConsumer<Exception, Callable<T>> functionOnError = onError != null ? onError : (error, task) -> {};
		// @formatter:on
        final Future<T> future = run(taskToProcess);
        
        final CompletableFuture<T> result = CompletableFuture.supplyAsync(() -> {
            T futureResult = null;
            try {
                futureResult = future.get(timeout, TimeUnit.MILLISECONDS);
                
            }
            catch (InterruptedException | ExecutionException | TimeoutException e) {
                if ((e instanceof ExecutionException) && (e.getCause() instanceof NoSuchElementException)) {
                    futureResult = null;
                }
                else {
                    Loggers.DEBUG.error(e.getMessage(), e);
                    functionOnError.accept(e, taskToProcess);
                    future.cancel(true);
                }
            }
            
            return futureResult;
        }, executorCompletable);
        
        result.thenAcceptAsync((data) -> {
            functionOnDone.accept(data, taskToProcess);
        });
        
        return result;
    }
    
    private <T> Future<T> run(final Callable<T> taskToProcess) {
        return executor.submit(taskToProcess);
    }
    
    public <T> Future<T> submit(final String name, final Callable<T> task) {
        return submit(name, task, null, null);
    }
    
    public <T> Future<T> submit(final String name, final Callable<T> task, final TaskFinishListener listener) {
        return submit(name, task, listener, null);
    }
    
    public <T> Future<T> submit(final String name, final Callable<T> task, final TaskFinishListener finishListner,
                                final TaskStartListener startListner) {
        final Future<T> result = executor.submit(new ThreadsExecutorTask<T>(name, task, startListner, finishListner));
        return result;
    }
    
    // =========================================================================
    // LifecycleBootstrap
    // =========================================================================
    @Override
    public void start() {
    }
    
    @Override
    public void shutdown() {
        shutdownExecutor(executor);
        shutdownExecutor(executorCompletable);
    }
    
    private void shutdownExecutor(final ExecutorService localExecutor) {
        localExecutor.shutdown();
        
        try {
            localExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
        }
        catch (final InterruptedException e) {
            Loggers.DEBUG.debug(e.getMessage(), e);
            localExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    // =========================================================================
    // THREAD
    // =========================================================================
    private class ThreadsExecutorTask<T> implements Callable<T> {
        private final String             name;
        
        private final Callable<T>        task;
        
        private final TaskStartListener  startListner;
        
        private final TaskFinishListener finishListner;
        
        public ThreadsExecutorTask(final String name, final Callable<T> task, final TaskStartListener startListner,
                                   final TaskFinishListener finishListner) {
            Asserts.notNull("name mustn't be null!", name);
            Asserts.notNull("task mustn't be null!", task);
            this.name = name;
            this.task = task;
            this.startListner = startListner;
            this.finishListner = finishListner;
            
        }
        
        @Override
        public T call() throws Exception {
            final long startTime = System.currentTimeMillis();
            
            if (startListner != null) {
                startListner.onStart(startTime, name);
            }
            
            final Exception error = null;
            T result = null;
            try {
                result = task.call();
            }
            catch (final Exception e) {
                Loggers.DEBUG.error(e.getMessage(), e);
                Loggers.XLLOG.error(e.getMessage());
                throw e;
            }
            finally {
                if (finishListner != null) {
                    final long endTime = System.currentTimeMillis();
                    final long delais = startTime - endTime;
                    finishListner.onFinish(endTime, delais, name, result, error);
                }
            }
            
            return result;
        }
        
    }
    
    public ExecutorService getExecutor() {
        return executor;
    }
    
}
