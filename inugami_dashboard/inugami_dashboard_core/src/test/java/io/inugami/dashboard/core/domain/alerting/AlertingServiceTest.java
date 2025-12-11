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
package io.inugami.dashboard.core.domain.alerting;

import io.inugami.commons.test.UnitTestData;
import io.inugami.dashboard.api.domain.alerting.IAlertingDao;
import io.inugami.framework.interfaces.models.event.AlertingModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertingServiceTest {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String NOMINAL_LIST = """
            [ {
              "condition" : "error > 5",
              "description" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
              "function" : "simpleFunction",
              "level" : "ERROR",
              "message" : "too many errors",
              "name" : "alert",
              "provider" : "alertProvider",
              "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
            } ]
            """;
    
    @Mock
    private IAlertingDao alertingDao;

    @InjectMocks
    private AlertingService service;

    @Captor
    private ArgumentCaptor<Collection<AlertingModel>> alertsCaptor;
    @Captor
    private ArgumentCaptor<Collection<String>>        idsCaptor;

    // =================================================================================================================
    // CREATE
    // =================================================================================================================
    @Test
    void create_nominal() {
        when(alertingDao.create(any())).thenReturn(List.of(buildAlertingModel()));
        assertText(service.create(List.of(buildAlertingModel().toBuilder().uid(null).build())),
                   NOMINAL_LIST);
    }

    // =================================================================================================================
    // READ
    // =================================================================================================================
    @Test
    void getById_nominal() {
        when(alertingDao.getById(UnitTestData.UID, false)).thenReturn(buildAlertingModel());
        assertText(service.getById(UnitTestData.UID, false),
                   """
                           {
                             "condition" : "error > 5",
                             "description" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                             "function" : "simpleFunction",
                             "level" : "ERROR",
                             "message" : "too many errors",
                             "name" : "alert",
                             "provider" : "alertProvider",
                             "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                           }
                           """);
    }

    @Test
    void getByIds_nominal() {
        when(alertingDao.getByIds(any())).thenReturn(List.of(buildAlertingModel()));
        assertText(service.getByIds(List.of(UnitTestData.UID)),
                   NOMINAL_LIST);
    }

    @Test
    void contains_nominal() {
        when(alertingDao.contains(any())).thenReturn(true);
        assertThat(service.contains(List.of(UnitTestData.UID))).isTrue();
    }

    // =================================================================================================================
    // UPDATE
    // =================================================================================================================
    @Test
    void update_nominal() {
        when(alertingDao.update(any())).thenReturn(List.of(buildAlertingModel()));

        service.update(List.of(buildAlertingModel()), false);

        verify(alertingDao).update(alertsCaptor.capture());
        assertText(alertsCaptor.getValue(),
                   NOMINAL_LIST);
    }

    @Test
    void update_force() {
        when(alertingDao.update(any())).thenReturn(List.of(buildAlertingModel()));

        service.update(List.of(buildAlertingModel()), true);

        verify(alertingDao).update(alertsCaptor.capture());
        assertText(alertsCaptor.getValue(),
                   NOMINAL_LIST);
    }

    // =================================================================================================================
    // DELETE
    // =================================================================================================================
    @Test
    void delete_force() {
        service.delete(List.of(UnitTestData.UID));

        verify(alertingDao).delete(idsCaptor.capture());
        assertText(idsCaptor.getValue(),
                   """
                           [ "bb895294-efe7-484b-b670-14d004eaf461" ]
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private AlertingModel buildAlertingModel() {
        return AlertingModel.builder()
                            .uid(UnitTestData.UID)
                            .name("alert")
                            .description(UnitTestData.LOREM_IPSUM)
                            .provider("alertProvider")
                            .message("too many errors")
                            .level("ERROR")
                            .condition("error > 5")
                            .function("simpleFunction")
                            .build();
    }


}