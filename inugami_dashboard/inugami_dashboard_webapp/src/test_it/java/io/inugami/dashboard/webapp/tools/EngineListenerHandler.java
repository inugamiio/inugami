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
package io.inugami.dashboard.webapp.tools;

import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.api.domain.engine.dto.EngineResultDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


public class EngineListenerHandler implements EngineListener {
    private Map<String, CompletableFuture<EngineResultDTO>> handlers = new ConcurrentHashMap<>();

    public CompletableFuture<EngineResultDTO> registerOnDone() {
        String                                   uid    = UUID.randomUUID().toString();
        final CompletableFuture<EngineResultDTO> result = new CompletableFuture<EngineResultDTO>();
        handlers.put(uid, result);
        return result;
    }

    @Override
    public void onDone(final EngineResultDTO engineResult) {
        final List<String> uids    = new ArrayList<>();
        final var          entries = handlers.entrySet();
        for (var entry : entries) {
            final CompletableFuture<EngineResultDTO> future = entry.getValue();
            future.complete(engineResult);
            uids.add(entry.getKey());
        }
        for (String uid : uids) {
            handlers.remove(uid);
        }
    }

}
