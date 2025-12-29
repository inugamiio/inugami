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
package io.inugami.monitoring.core.interceptable;

import io.inugami.framework.interfaces.monitoring.data.RequestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.inugami.monitoring.core.interceptable.DefaultInterceptableIdentifier.RESOURCES_EXT;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * DefaultResourceIdentifierTest
 *
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
@ExtendWith(MockitoExtension.class)
class DefaultInterceptableIdentifierTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final DefaultInterceptableIdentifier RESOLVER = new DefaultInterceptableIdentifier();

    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    @Test
    void isInterceptable_nominal() {
        assertThat(RESOLVER.isInterceptable(buildRequestData("http://foobar.org/js/myApp.js?v=1.2.3"))).isFalse();
        for (String extension : RESOURCES_EXT) {
            assertThat(RESOLVER.isInterceptable(buildRequestData(
                    "http://foobar.org/style/application" + extension))).isFalse();
        }
        assertThat(RESOLVER.isInterceptable(buildRequestData("http://foobar.org/rest/service/"))).isFalse();
        assertThat(RESOLVER.isInterceptable(buildRequestData("   "))).isFalse();

        assertThat(RESOLVER.isInterceptable(buildRequestData(null))).isTrue();
        assertThat(RESOLVER.isInterceptable(buildRequestData("http://foobar.org/rest/service"))).isTrue();
        assertThat(RESOLVER.isInterceptable(buildRequestData("http://foobar.org/rest/service?id=1"))).isTrue();
    }


    @Test
    void isResource_nominal() throws Exception {
        assertThat(RESOLVER.isResource("http://foobar.org/js/myApp.js?v=1.2.3")).isTrue();
        for (String extension : RESOURCES_EXT) {
            assertThat(RESOLVER.isResource("http://foobar.org/style/application" + extension)).isTrue();
        }
        assertThat(RESOLVER.isResource("http://foobar.org/rest/service/")).isTrue();
        assertThat(RESOLVER.isResource("   ")).isTrue();

        assertThat(RESOLVER.isResource(null)).isFalse();
        assertThat(RESOLVER.isResource("http://foobar.org/rest/service")).isFalse();
        assertThat(RESOLVER.isResource("http://foobar.org/rest/service?id=1")).isFalse();


    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private RequestData buildRequestData(final String url) {
        return RequestData.builder()
                          .uri(url)
                          .build();
    }
}
