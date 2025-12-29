package ${package}.infrastructure.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @since 2025-12-28
 */
@ActiveProfiles("test")
@SpringBootTest(classes={SpringBootITConfiguration.class})
public class SpringBootIntegrationRunner {

}
