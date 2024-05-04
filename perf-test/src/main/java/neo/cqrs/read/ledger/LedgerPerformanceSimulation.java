package neo.cqrs.read.ledger;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class LedgerPerformanceSimulation extends Simulation {

    private static final String REQUEST_JSON = "static/LedgerEntryRequest.json";

    HttpProtocolBuilder httpProtocolBuilder;

    {
        httpProtocolBuilder =
                http.baseUrl("http://localhost:8080")
                        .contentTypeHeader("application/json")
                        .acceptHeader("application/json");

        ScenarioBuilder scn = scenario("Command")
                .during(Duration.ofMinutes(1))
                .on(
                        feed(listFeeder(ledgerEntryFeeder()).circular())
                                .exec(
                                        http("/ledger/account/{accountId}/entry")
                                                .put("/ledger/account/#{accountId}/entry")
                                                .body(ElFileBody(REQUEST_JSON))
                                                .check(status().is(200))
                                )
                );

        setUp(
                scn.injectOpen(atOnceUsers(1))
                        .protocols(httpProtocolBuilder)
        );
    }

    private List<Map<String, Object>> ledgerEntryFeeder() {
        List<Map<String, Object>> feeder = new ArrayList<>();

        IntStream.range(1, 1000).boxed()
                .forEachOrdered(a ->
                        feeder.add(
                                new HashMap<>() {
                                    {
                                        put("accountId", a);
                                        put("entryDate", ZonedDateTime.now().minusMinutes(a));
                                        put("amount", a * new Random().nextInt());
                                    }
                                }
                        )
                );

        return feeder;
    }

}
