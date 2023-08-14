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
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserDAO {
    private final Map<Long, UserDataDTO> CACHE = new ConcurrentHashMap<>();

    public UserDataDTO create(final UserDataDTO user) {

        final UserDataDTO value = user.toBuilder().id(CACHE.size() + 1).build();
        CACHE.put(value.getId(), value);

        return value;
    }

    public UserDataDTO getById(final long id) {
        return CACHE.get(id);
    }
}
