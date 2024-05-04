package neo.cqrs.read.ledger;


import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;

public class LedgerSystemPerformanceTestRunner {

    public static void main(String[] args) {
        var props = new GatlingPropertiesBuilder();
        props.simulationClass(LedgerPerformanceSimulation.class.getName());
        Gatling.fromMap(props.build());
    }

}
