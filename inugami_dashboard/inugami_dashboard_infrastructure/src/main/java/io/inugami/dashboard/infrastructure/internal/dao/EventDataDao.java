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
package io.inugami.dashboard.infrastructure.internal.dao;

import com.hazelcast.core.HazelcastInstance;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EventDoneDTO;
import io.inugami.dashboard.api.domain.event.IEventDataDao;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.maven.Gav;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventDataDao implements IEventDataDao {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private final HazelcastInstance hazelcast;

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @Override
    public void updateEventsData(@NonNull final Collection<EventDoneDTO> eventsResults) {
        if (eventsResults.isEmpty()) {
            return;
        }

        for (EventDoneDTO event : eventsResults) {
            if (isNotSuccess(event.getData().getStatus())) {
                continue;
            }
            final String eventName = event.getEvent().getName();
            final String pluginGav = Gav.builder()
                                        .groupId(event.getPlugin().getGav().getGroupId())
                                        .artifactId(event.getPlugin().getGav().getArtifactId())
                                        .build()
                                        .getHash();
            final var                        cache = hazelcast.getMap(pluginGav);
            final EnginePluginEventResultDTO data  = event.getData();
            applyIfNotNull(data, d -> cache.put(eventName, d));
        }
    }

    private boolean isNotSuccess(final Status status) {
        if (status == null) {
            return true;
        }
        return switch (status) {
            case RUNNING, ERROR, FATAL -> true;
            default -> false;
        };
    }


    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Override
    public Map<String, EnginePluginEventResultDTO> findPluginDataByGav(@NonNull final Gav gav) {
        final Map<String, EnginePluginEventResultDTO> result = new LinkedHashMap<>();
        final var                                     cache  = hazelcast.getMap(gav.getHash());

        final List<String> keys = new ArrayList<>();
        cache.keySet().forEach(item -> keys.add(String.valueOf(item)));
        Collections.sort(keys);

        for (final String key : keys) {
            final Object value = cache.get(key);
            if (value instanceof EnginePluginEventResultDTO d) {
                result.put(key, d);
            }
        }
        return result;
    }
}
