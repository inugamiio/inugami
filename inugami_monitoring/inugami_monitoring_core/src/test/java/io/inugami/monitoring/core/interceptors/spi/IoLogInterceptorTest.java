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
package io.inugami.monitoring.core.interceptors.spi;

import io.inugami.commons.test.dto.AssertLogContext;
import io.inugami.framework.api.exceptions.WarningContext;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.exceptions.DefaultWarning;
import io.inugami.framework.interfaces.monitoring.ErrorResult;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.data.ResponseData;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.monitoring.core.spi.IoLogInterceptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static io.inugami.commons.test.UnitTestHelper.assertLogs;

class IoLogInterceptorTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final Long   DATE_TIME = LocalDateTime.of(2023, 7, 1, 14, 54, 0).toEpochSecond(ZoneOffset.UTC);
    private static final String UID       = "fd508efe-9b7e-403f-b0bc-44e379d0171d";

    // =================================================================================================================
    // INIT
    // =================================================================================================================
    @AfterEach
    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
        WarningContext.clean();
    }

    // =================================================================================================================
    // onBegin
    // =================================================================================================================
    @Test
    void onBegin_nominal() {
        final RequestData request = RequestData.builder()
                                               .method("POST")
                                               .uri("/my/service")
                                               .contextPath("/application")
                                               .addHeader("app", "myApplication")
                                               .content("{\"name\":\"John Smith\"}")
                                               .build();
        processOnBegin(request, "io/inugami/monitoring/core/interceptors/spi/ioLogInterceptor/onBegin_nominal.json");
    }


    @Test
    void onBegin_withoutContextPath() {
        final RequestData request = RequestData.builder()
                                                 .method("POST")
                                                 .uri("/my/service")
                                                 .addHeader("app", "myApplication")
                                                 .content("{\"name\":\"John Smith\"}")
                                                 .build();

        processOnBegin(request, "io/inugami/monitoring/core/interceptors/spi/ioLogInterceptor/onBegin_withoutContextPath.json");
    }

    @Test
    void onBegin_withContextPathInUri() {
        final RequestData request = RequestData.builder()
                                                 .method("POST")
                                                 .uri("/application/my/service")
                                                 .contextPath("/application")
                                                 .addHeader("app", "myApplication")
                                                 .content("{\"name\":\"John Smith\"}")
                                                 .build();

        processOnBegin(request, "io/inugami/monitoring/core/interceptors/spi/ioLogInterceptor/onBegin_withContextPathInUri.json");
    }

    // =================================================================================================================
    // onDone
    // =================================================================================================================
    @Test
    void onDone_nominal() {
        final RequestData request = RequestData.builder()
                                                 .method("POST")
                                                 .uri("/my/service")
                                                 .contextPath("/application")
                                                 .addHeader("app", "myApplication")
                                                 .content("{\"name\":\"John Smith\"}")
                                                 .build();
        final ResponseData responseData = ResponseData.builder()
                                                      .code(200)
                                                      .datetime(DATE_TIME)
                                                      .duration(42L)
                                                      .contentType("UTF-8")
                                                      .content("{\"id\":1,\"name\":\"John Smith\"}")
                                                      .addHeader("x-correlation-id", UID)
                                                      .build();
        WarningContext.getInstance().addWarnings(DefaultWarning.builder()
                                                               .warningType("functional")
                                                               .warningCode("WARN-1")
                                                               .message("account need to be confirmed ")
                                                               .messageDetail("some detail")
                                                               .build());
        processOnDone(request, responseData, null, "io/inugami/monitoring/core/interceptors/spi/ioLogInterceptor/onDone_nominal.json");
    }

    @Test
    void onDone_withError() {
        final RequestData request = RequestData.builder()
                                                 .method("POST")
                                                 .uri("/my/service")
                                                 .addHeader("app", "myApplication")
                                                 .content("{\"name\":\"John Smith\"}")
                                                 .build();
        final ResponseData responseData = ResponseData.builder()
                                                      .code(500)
                                                      .datetime(DATE_TIME)
                                                      .duration(42L)
                                                      .contentType("UTF-8")
                                                      .addHeader("x-correlation-id", UID)
                                                      .build();

        processOnDone(request, responseData, ErrorResult.builder()
                                                        .exception(new NullPointerException("oups"))
                                                        .message("Null pointer occurs")
                                                        .errorType("technic")
                                                        .errorCode("ERR-0")
                                                        .cause("NPE")
                                                        .fallBack("{}")
                                                        .stack("io.inugami....")
                                                        .build(),
                      "io/inugami/monitoring/core/interceptors/spi/ioLogInterceptor/onDone_withError.json");
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    IoLogInterceptor buildInterceptor() {
        return new IoLogInterceptor();
    }

    private void processOnBegin(final RequestData request, final String path) {
        assertLogs(AssertLogContext.builder()
                                   .process(() -> buildInterceptor().onBegin(request))
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .path(path)
                                   .build());

    }

    private void processOnDone(final RequestData request,
                               final ResponseData responseData,
                               final ErrorResult error,
                               final String path) {

        assertLogs(AssertLogContext.builder()
                                   .process(() -> buildInterceptor().onDone(request, responseData, error))
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .path(path)
                                   .build());
    }

}