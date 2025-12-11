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
package io.inugami.dashboard.api.domain.alerting.dto;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.dto.AssertDtoContext;
import io.inugami.framework.interfaces.models.search.SortOrder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertDto;

class AlertingSearchRequestDTOTest {
    @Test
    void alertingSearchRequestDTO() {
        assertDto(AssertDtoContext.<AlertingSearchRequestDTO>builder()
                                  .objectClass(AlertingSearchRequestDTO.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(AlertingSearchRequestDTO::new)
                                  .fullArgConstructor(this::buildAlertingSearchRequestDTO)
                                  .fullArgConstructorRefPath("io/inugami/dashboard/api/domain/alerting/dto/alertingSearchRequestDTO/model.json")
                                  .getterRefPath("io/inugami/dashboard/api/domain/alerting/dto/alertingSearchRequestDTO/getter.json")
                                  .toStringRefPath("io/inugami/dashboard/api/domain/alerting/dto/alertingSearchRequestDTO/toString.txt")
                                  .checkEquals(false)
                                  .checkSetters(true)
                                  .build());
    }


    private AlertingSearchRequestDTO buildAlertingSearchRequestDTO() {
        return AlertingSearchRequestDTO.builder()
                                       .page(0)
                                       .pageSize(10)
                                       .sortFields("name")
                                       .sortOrder(SortOrder.ASC)
                                       .createdBy("system")
                                       .clearCreatedBy()
                                       .createdBy(List.of("system"))
                                       .createdDate(UnitTestData.DATE_TIME.minusDays(5))
                                       .clearCreatedDate()
                                       .createdDate(List.of(UnitTestData.DATE_TIME.minusDays(5)))
                                       .lastModifiedBy("system2")
                                       .clearLastModifiedBy()
                                       .lastModifiedBy(List.of("system2"))
                                       .lastModifiedDate(UnitTestData.DATE_TIME)
                                       .clearLastModifiedDate()
                                       .lastModifiedDate(List.of(UnitTestData.DATE_TIME))
                                       //------------------------------------------------------------------------------
                                       .uid(UnitTestData.UID)
                                       .clearUid()
                                       .uid(List.of(UnitTestData.UID))

                                       .name("basic-alert")
                                       .clearName()
                                       .name(List.of("basic-alert"))

                                       .description(UnitTestData.LOREM_IPSUM)
                                       .clearDescription()
                                       .description(List.of(UnitTestData.LOREM_IPSUM))

                                       .provider("elastic-search")
                                       .clearProvider()
                                       .provider(List.of("elastic-search"))

                                       .message("too many XLLOG !")
                                       .clearMessage()
                                       .message(List.of("too many XLLOG !"))

                                       .level("ERROR")
                                       .clearLevel()
                                       .level(List.of("ERROR"))

                                       //------------------------------------------------------------------------------
                                       .build()
                                       .toBuilder()
                                       .build();
    }
}