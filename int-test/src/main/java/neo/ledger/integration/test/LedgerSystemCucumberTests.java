package neo.ledger.integration.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/main/resources/features"}
)
public class LedgerSystemCucumberTests {
    public static void main(String[] args) {
        io.cucumber.core.cli.Main.main(args);
    }
}
