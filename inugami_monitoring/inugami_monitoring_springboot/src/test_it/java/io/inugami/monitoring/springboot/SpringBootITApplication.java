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
package io.inugami.monitoring.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.api.marshalling.JsonMarshaller;
import io.inugami.monitoring.springboot.config.InugamiMonitoringConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@ComponentScan(basePackages = {
        InugamiMonitoringConfig.INUGAMI
})
@SpringBootApplication
public class SpringBootITApplication {

    @Primary
    @Bean
    public ObjectMapper buildObjectMapper() {
        return JsonMarshaller.getInstance().getIndentedObjectMapper();
    }


}

