package ${package}.webapp.spring;

import io.inugami.framework.api.tools.PortGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @since 2025-12-29
 */
@Slf4j
@Configuration
public class SpringBootInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static final int SERVER_PORT = PortGenerator.generateFor("server.port");

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        System.setProperty("server.port", String.valueOf(SERVER_PORT));
        log.info("--------------------------------------------------------------");
        log.info(">>> use spring boot port : {}",SERVER_PORT);
        log.info("--------------------------------------------------------------");
    }
}
