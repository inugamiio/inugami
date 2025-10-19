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
package io.inugami.framework.commons.threads.runner;

import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.providers.ProviderRunner;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import io.inugami.framework.interfaces.task.ProviderTask;
import lombok.Builder;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * MultiThreadedProviderRunner
 *
 * @author patrick_guillerm
 * @see io.inugami.framework.interfaces.providers.ProviderRunner
 * @since 5 janv. 2017
 */
@SuppressWarnings({"java:S3014"})
public class MultiThreadedProviderRunner implements ProviderRunner, ThreadFactory {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String CONFIG_THREADS = "max_thread";
    private ThreadGroup threadGroup;

    private String namePrefix;

    private ThreadsExecutorService threadsExecutor;

    private int nbMaxThreads;

    private int threadNumber = 0;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Builder
    public MultiThreadedProviderRunner(final String componentName, final ConfigHandler<String, String> config) {
        threadGroup = Thread.currentThread().getThreadGroup();
        nbMaxThreads = Integer.parseInt(config.getOrDefault(CONFIG_THREADS, "1"));
        threadsExecutor = new ThreadsExecutorService(
                "MultiThreadedProviderRunner." + componentName, nbMaxThreads, false);
        namePrefix = componentName + "-";

    }


    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Future<ProviderFutureResult> run(final String name, final ProviderTask task) {
        return threadsExecutor.submit(name == null ? namePrefix : name, task,
                                      new ProviderTaskStopListener(task.getEvent(), task.getPluginGav()),
                                      new ProviderTaskStartListener(task.getEvent(), task.getPluginGav()));
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        return new Thread(threadGroup, runnable, namePrefix + incrementThreadNumber(), 0);
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private synchronized int incrementThreadNumber() {
        threadNumber++;
        return threadNumber;
    }
}
