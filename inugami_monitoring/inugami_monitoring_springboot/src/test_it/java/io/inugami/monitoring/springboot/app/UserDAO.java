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

import static io.inugami.api.exceptions.Asserts.assertHigher;
import static io.inugami.api.exceptions.Asserts.assertNotNull;
import static io.inugami.monitoring.springboot.app.UserErrors.READ_USER_ID_INVALID;
import static io.inugami.monitoring.springboot.app.UserErrors.READ_USER_NOT_FOUND;

@Service
public class UserDAO {
    private final Map<Long, UserDataDTO> CACHE = new ConcurrentHashMap<>();

    public UserDataDTO create(final UserDataDTO user) {

        final UserDataDTO value = user.toBuilder().id(CACHE.size() + 1).build();
        CACHE.put(value.getId(), value);

        return value;
    }

    public UserDataDTO getById(final long id) {
        assertHigher(READ_USER_ID_INVALID.addDetail("with id {0}", id), 0, id);
        final UserDataDTO result = CACHE.get(id);

        if (id == 42) {
            throw new CacheConnectionException("unable to connect to cache");
        }

        assertNotNull(READ_USER_NOT_FOUND, result);
        return result;
    }


    private final class CacheConnectionException extends RuntimeException {
        private static final long serialVersionUID = 6232779694014145940L;

        public CacheConnectionException(final String message) {
            super(message);
        }
    }
}
