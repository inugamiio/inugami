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
package io.inugami.dashboard.core.domain.engine.events;

import io.inugami.dashboard.api.domain.event.EventErrors;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ExceptionWithErrorCode;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class EventRunnerUtils {
    public static @NonNull Collection<Processor> selectProcessor(@Nullable final List<ProcessorModel> eventProcessors,
                                                                 @Nullable final List<Processor> processors) {
        final List<Processor> result = new ArrayList<>();
        if (processors == null || processors.isEmpty() || processors == null || processors.isEmpty()) {
            return result;
        }

        for (ProcessorModel processModel : eventProcessors) {
            chooseProcessor(processModel, processors).ifPresent(result::add);
        }

        return result;
    }

    private static @NonNull Optional<Processor> chooseProcessor(final @NonNull ProcessorModel processModel,
                                                                final @NonNull List<Processor> processors) {
        return processors.stream()
                         .filter(p -> p.getName().equalsIgnoreCase(processModel.getName()) ||
                                      p.getClass().getName().equalsIgnoreCase(processModel.getName()) ||
                                      p.getClass().getName().equalsIgnoreCase(processModel.getClassName()))
                         .findFirst();
    }

    public static @NonNull ErrorCode resolveErrorCode(@NonNull final Throwable error) {
        ErrorCode result = null;
        if (error instanceof ExceptionWithErrorCode e) {
            result = e.getErrorCode();
        }
        return result == null ? EventErrors.UNDEFINED : result;
    }
}
