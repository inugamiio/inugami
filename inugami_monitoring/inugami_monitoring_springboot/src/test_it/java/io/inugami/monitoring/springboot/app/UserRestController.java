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

import io.inugami.api.feature.Feature;
import io.inugami.commons.test.dto.UserDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static io.inugami.api.exceptions.Asserts.assertNotEmpty;
import static io.inugami.monitoring.springboot.app.UserErrors.READ_USER_ID_INVALID;

@RequiredArgsConstructor
@RestController
public class UserRestController implements UserRestClient {

    private final UserDAO userDAO;

    @Feature("user.create")
    @Override
    public UserDataDTO createUser(final UserDataDTO user) {
        assertNotEmpty(READ_USER_ID_INVALID, user.getEmail());
        return userDAO.create(user);
    }

    @Override
    public UserDataDTO getUserById(final long id) {
        return userDAO.getById(id);
    }
}
