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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.tools.Chrono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadsExecutor
 * 
 * @author patrickguillerm
 * @since 24 mars 2018
 */
public class RunAndCloseService<T> implements ThreadFactory {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger                         LOGGER      = LoggerFactory.getLogger(RunAndCloseService.class);
    
    private final String                                threadsName;
    
    private final List<Callable<T>>                     tasks;
    
    private final Map<Future<T>, Callable<T>>           tasksAndFutures;
    
    private final long                                  timeout;
    
    private final BiFunction<Exception, Callable<T>, T> onError;
    
    private final ExecutorService                       executor;
    
    private final CompletionService<T>                  completion;
    
    private final ThreadGroup                           threadGroup;
    
    private final AtomicInteger                         threadIndex = new AtomicInteger();
    
    private final List<T>                               data        = new ArrayList<>();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @SafeVarargs
    public RunAndCloseService(final String threadsName, final long timeout, final int nbThreads,
                              final BiFunction<Exception, Callable<T>, T> onError, final Callable<T>... tasks) {
        this(threadsName, timeout, nbThreads, Arrays.asList(tasks), onError);
    }
    
    @SafeVarargs
    public RunAndCloseService(final String threadsName, final long timeout, final int nbThreads,
                              final Callable<T>... tasks) {
        this(threadsName, timeout, nbThreads, Arrays.asList(tasks), null);
    }
    
    public RunAndCloseService(final String threadsName, final long timeout, final int nbThreads,
                              final List<Callable<T>> tasks) {
        this(threadsName, timeout, nbThreads, tasks, null);
    }
    
    public RunAndCloseService(final String threadsName, final long timeout, final int nbThreads,
                              final List<Callable<T>> tasks, final BiFunction<Exception, Callable<T>, T> onError) {
        super();
        Asserts.notNull(tasks);
        
        this.tasks = tasks;
        int howManyThreads = tasks.size() < nbThreads ? tasks.size() : nbThreads;
        if (howManyThreads <= 0) {
            howManyThreads = 1;
        }
        this.threadsName = threadsName;
        this.timeout = timeout;
        this.onError = onError;
        tasksAndFutures = new HashMap<>();
        threadGroup = Thread.currentThread().getThreadGroup();
        executor = Executors.newFixedThreadPool(howManyThreads, this);
        completion = new ExecutorCompletionService<>(executor);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public List<T> run() {
        final List<Future<T>> futures = sumitTask();
        int tasksLeft = futures.size();
        long timeLeft = timeout;
        final Chrono chrono = Chrono.startChrono();
        
        while ((tasksLeft > 0) && (chrono.snapshot().getDelaisInMillis() < timeout)) {
            timeLeft = computeTimeLeft(timeLeft, chrono);
            Future<T> itemFuture = null;
            T taskData = null;
            try {
                itemFuture = completion.poll(timeLeft, TimeUnit.MILLISECONDS);
                if (itemFuture != null) {
                    taskData = itemFuture.get();
                    tasksLeft = tasksLeft - 1;
                }
            }
            catch (ExecutionException | InterruptedException error) {
                tasksLeft = tasksLeft - 1;
            }
            
            if (taskData != null) {
                data.add(taskData);
            }
        }
        executor.shutdown();
        
        data.addAll(handlerTimeoutTask());
        return data;
    }
    
    private long computeTimeLeft(final long timeLeft, final Chrono chrono) {
        final long result = timeLeft - chrono.snapshot().getDelaisInMillis();
        return result < 0 ? 0 : result;
    }
    
    // =========================================================================
    // PRIVATE
    // =========================================================================
    private List<Future<T>> sumitTask() {
        final List<Future<T>> result = new ArrayList<>();
        for (final Callable<T> task : tasks) {
            final Future<T> future = completion.submit(new CallableTask(task, this));
            result.add(future);
            tasksAndFutures.put(future, task);
        }
        return result;
    }
    
    private class CallableTask<U> implements Callable<U> {
        private final Callable<U>           task;
        
        private final RunAndCloseService<U> runAndCloseService;
        
        public CallableTask(final Callable<U> task, final RunAndCloseService<U> runAndCloseService) {
            this.task = task;
            this.runAndCloseService = runAndCloseService;
        }
        
        @Override
        public U call() throws Exception {
            U result = null;
            try {
                result = task.call();
            }
            catch (final Exception e) {
                LOGGER.error(e.getMessage(), e);
                result = runAndCloseService.processHandlerError(e, task);
            }
            
            return result;
        }
        
    }
    
    // =========================================================================
    // new Thread
    // =========================================================================
    @Override
    public Thread newThread(final Runnable runnable) {
        final String name = String.join(".", threadsName, String.valueOf(threadIndex.getAndIncrement()));
        final Thread result = new Thread(threadGroup, runnable, name, 10);
        result.setDaemon(false);
        return result;
    }
    
    // =========================================================================
    // ERRORS
    // =========================================================================
    private List<T> handlerTimeoutTask() {
        final List<T> result = new ArrayList<>();
        for (final Map.Entry<Future<T>, Callable<T>> entry : tasksAndFutures.entrySet()) {
            if (!entry.getKey().isDone()) {
                final Callable<T> task = entry.getValue();
                final T taskData = processHandlerError(null, task);
                if (taskData != null) {
                    result.add(taskData);
                }
            }
        }
        return result;
    }
    
    private synchronized T processHandlerError(final Exception error, final Callable<T> task) {
        T result = null;
        if (onError == null) {
            result = handlerError(error, task);
        }
        else {
            result = onError.apply(error, task);
        }
        return result;
    }
    
    private T handlerError(final Exception error, final Callable<T> task) {
        T result = null;
        if (task instanceof CallableWithErrorResult) {
            if (error == null) {
                result = ((CallableWithErrorResult<T>) task).getTimeoutResult();
            }
            else {
                result = ((CallableWithErrorResult<T>) task).getErrorResult(error);
            }
        }
        
        return result;
    }
    
    public void forceShutdown() {
        if (!executor.isShutdown()) {
            if (!executor.isTerminated()) {
                try {
                    executor.awaitTermination(0, TimeUnit.MILLISECONDS);
                }
                catch (final InterruptedException e) {
                    Loggers.DEBUG.error(e.getMessage(), e);
                }
            }
            executor.shutdown();
        }
    }
}
