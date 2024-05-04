package neo.ledger.integration.test.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "neo.ledger.integration.test")
public class CucumberSpringConfig {
}
