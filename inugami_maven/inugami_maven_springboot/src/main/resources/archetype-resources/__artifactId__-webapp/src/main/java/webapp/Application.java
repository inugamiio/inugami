package ${package}.webapp;

import io.inugami.framework.commons.spring.EnabledInugami;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @since 2025-12-29
 */
@ComponentScan(basePackages={
        ConfigConfiguration.INUGAMI,
        "${package}"
})
@EnableAutoConfiguration
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
