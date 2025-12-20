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
package io.inugami.dashboard.infrastructure.spring;


import io.inugami.dashboard.api.domain.engine.IEngineService;
import io.inugami.framework.commons.spring.configuration.ConfigConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ActiveProfiles("test")
@ContextConfiguration(classes = {SpringBootITConfiguration.class, ConfigConfiguration.class})
@SpringBootTest
public abstract class SpringBootIntegrationTest {

    @MockitoBean
    protected IEngineService engineService;
}
