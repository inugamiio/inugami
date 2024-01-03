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
package io.inugami.api.providers;

import io.inugami.interfaces.concurrent.FutureData;
import io.inugami.interfaces.concurrent.FutureDataModel;
import io.inugami.interfaces.models.Config;
import io.inugami.interfaces.models.event.SimpleEvent;
import io.inugami.interfaces.processors.ClassBehavior;
import io.inugami.interfaces.processors.ConfigHandler;
import io.inugami.interfaces.providers.ProviderRunner;
import io.inugami.interfaces.task.ProviderFutureResult;
import io.inugami.interfaces.task.ProviderTask;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * AbstractProvider allow to helper to run providers
 *
 * @author patrick_guillerm
 * @since 3 janv. 2017
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractProvider {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ClassBehavior                 classBehavior;
    private final ConfigHandler<String, String> config;
    private final ProviderRunner                providerRunner;


    // =========================================================================
    // RUN FUTURE TASK
    // =========================================================================
    protected List<Future<ProviderFutureResult>> runFutureTask(final List<ProviderTask> tasks) {
        final List<Future<ProviderFutureResult>> result = new ArrayList<>();

        if (tasks != null) {
            //@formatter:off
            tasks.stream()
                 .map(task->providerRunner.run(getName(), task))
                 .forEach(result::add);
            //@formatter:on
        }

        return result;
    }

    protected Future<ProviderFutureResult> runFutureTask(final ProviderTask task) {
        return providerRunner.run(getName(), task);
    }

    protected Future<ProviderFutureResult> runFutureTask(final String name, final ProviderTask task) {
        return providerRunner.run(name, task);
    }

    // =========================================================================
    // RUN TASK
    // =========================================================================
    protected FutureData<ProviderFutureResult> runTask(final ProviderTask task, final SimpleEvent event,
                                                       final FutureData<ProviderFutureResult> future) {
        return FutureDataModel.<ProviderFutureResult>builder().
                              task(task)
                              .event(event)
                              .future(providerRunner.run(getName(), task))
                              .build();

    }

    // =========================================================================
    // GETTERS
    // =========================================================================
    public Optional<String> getConfig(final String key) {
        return classBehavior.getConfig(key);
    }

    public String getName() {
        return classBehavior.getName();
    }

    public List<Config> getConfigs() {
        return classBehavior.getConfigs();
    }

}
