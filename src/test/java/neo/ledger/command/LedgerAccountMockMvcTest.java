package neo.ledger.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import neo.ledger.command.controller.dto.LedgerEntryDTO;
import neo.ledger.common.event.LedgerEntryEvent;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LedgerAccountMockMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EventStore eventStore;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void entryToAnAccount_balanceUpdated_OK() throws Exception {
        mockMvc.perform(put("/ledger/account/1/entry")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(buildLedgerEntryDTO()))
                .andExpect(status().isOk());

        BigDecimal balance
                = eventStore
                .readEvents("1")
                .asStream()
                .map(s -> {
                    if (s.getPayload() instanceof LedgerEntryEvent entry) {
                        return entry;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(f -> {
                    if ("DR".equalsIgnoreCase(f.getType())) {
                        return f.getAmount();
                    } else {
                        return f.getAmount().negate();
                    }
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        assertThat(balance, equalTo(BigDecimal.TEN));
    }

    private String buildLedgerEntryDTO() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new LedgerEntryDTO(ZonedDateTime.now(), "description",
                new BigDecimal(10), "DR"));
    }

}