package neo.ledger.integration.test.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CommandSteps {

    @Given("a call to ledger entry endpoint with:")
    public void given(DataTable table) {

        Map<String, String> dataMap = table.asMap(String.class, String.class);

        WebTestClient webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080").build();
        webTestClient
                .put()
                .uri("/ledger/account/{accountId}/entry"
                        , Map.of("accountId", dataMap.get("account")))
                .bodyValue(new LedgerEntryDTO(ZonedDateTime.now()
                        , dataMap.get("description")
                        , new BigDecimal(dataMap.get("amount"))
                        , dataMap.get("type")))

                .exchange().expectStatus().is2xxSuccessful();

    }

    @Then("account {string} balance query returns {string}")
    public void balanceQueryReturns(String account, String balance) {

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080").build();

        await().atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(getAccountBalance(account, webClient),
                        hasJsonPath("$.balance", equalTo(Double.parseDouble(balance))))
                );
    }

    private String getAccountBalance(String account, WebClient webClient) {
        return webClient
                .get()
                .uri("/ledger/account/{accountId}/balance"
                        , Map.of("accountId", account))
                .retrieve().bodyToMono(String.class).block();
    }
}
