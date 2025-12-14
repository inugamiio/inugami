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
package io.inugami.monitoring.springboot.app;

import io.inugami.commons.test.dto.UserDataDTO;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RequestMapping(path = "user")
@RequiredArgsConstructor
@Builder
@RestController
public class UserRestController {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private static final Map<Long, UserDataDTO> USERS  = new ConcurrentHashMap<>();
    private static final AtomicLong             CURSOR = new AtomicLong();

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<UserDataDTO> create(@RequestBody final Collection<UserDataDTO> values) {

        final List<UserDataDTO> result = Optional.ofNullable(values).orElse(List.of())
                                                 .stream()
                                                 .map(UserDataDTO::toBuilder)
                                                 .map(u -> u.id(CURSOR.incrementAndGet()))
                                                 .map(UserDataDTO.UserDataDTOBuilder::build)
                                                 .toList();
        result.forEach(u -> USERS.put(u.getId(), u));
        return result;
    }

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchResponse<UserDataDTO> search() {
        final List<Long> keys = new ArrayList<>(USERS.keySet());
        Collections.sort(keys);

        final List<UserDataDTO> data = new ArrayList<>();
        for (Long key : keys) {
            data.add(USERS.get(key));
        }

        return SearchResponse.<UserDataDTO>builder()
                             .nbFoundItems(data.size())
                             .previous(false)
                             .next(false)
                             .page(0)
                             .pageSize(data.size())
                             .data(data)
                             .build();
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDataDTO getById(@PathVariable(required = true) final Long id) {
        return USERS.get(id);
    }

    //==================================================================================================================
    // UPDATE
    //==================================================================================================================
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<UserDataDTO> update(@RequestBody final Collection<UserDataDTO> values) {
        for (UserDataDTO value : Optional.ofNullable(values).orElse(List.of())) {
            USERS.put(value.getId(), value);
        }
        return values;
    }


    //==================================================================================================================
    // DELETE
    //==================================================================================================================
    @DeleteMapping()
    public void delete(@RequestParam(name = "ids") final Collection<Long> ids) {
        for (Long id : Optional.ofNullable(ids).orElse(List.of())) {
            USERS.remove(id);
        }
    }
}
