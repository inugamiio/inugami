package ${package}.infrastructure.spring;

import io.inugami.framework.commons.spring.configuration.Inugami;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @since 2025-12-28
 */
@ComponentScan({
        Inugami.BASE_PACKAGE,
        "${package}.infrastructure"
})
@EnableAutoConfiguration
@SpringBootApplication
public class SpringBootITConfiguration {

}
