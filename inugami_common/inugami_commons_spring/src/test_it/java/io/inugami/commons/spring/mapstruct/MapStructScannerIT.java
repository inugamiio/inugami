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
package io.inugami.commons.spring.mapstruct;

import io.inugami.commons.spring.SpringBootIntegrationTest;
import io.inugami.commons.spring.mapstruct.mappers.OtherMapStructMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MapStructScannerIT extends SpringBootIntegrationTest {
    // annotated mapper with @MapStructMapper annotation
    @Autowired
    private SomeMapStructMapper someMapStructMapper;

    // declared mapper in spring boot configuration class
    @Autowired
    private OtherMapStructMapper otherMapStructMapper;


    @Test
    void test() {
        assertThat(someMapStructMapper).isNotNull();
        assertThat(otherMapStructMapper).isNotNull();
    }

}
