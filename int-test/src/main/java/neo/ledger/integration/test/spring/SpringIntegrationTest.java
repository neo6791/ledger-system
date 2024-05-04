package neo.ledger.integration.test.spring;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;

@EnableAutoConfiguration
@CucumberContextConfiguration
@SpringBootTest(classes = CucumberSpringConfig.class)
@AutoConfigureWebClient
public class SpringIntegrationTest {
}
