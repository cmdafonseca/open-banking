package github.com.cmdafonseca.openbanking.client;

import github.com.cmdafonseca.model.OBReadDataTransaction6;
import github.com.cmdafonseca.model.OBReadTransaction6;
import github.com.cmdafonseca.openbanking.entity.Transaction;
import github.com.cmdafonseca.openbanking.mappers.OBTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RESTTransactionsAPIClient implements TransactionApiClient {

    private final WebClient webClient;

    @Override
    public List<Transaction> getAccountTransactions(String accountNb) {
        return webClient
                .get()
                .uri("/accounts/{accountNb}/transactions", accountNb)
                .retrieve()
                .bodyToMono(OBReadTransaction6.class)
                .map(response -> Optional.ofNullable(response.getData())
                        .map(OBReadDataTransaction6::getTransaction)
                .orElse(Collections.emptyList()))
                .flatMapMany(Flux::fromIterable)
                .map(OBTransactionMapper::toTransaction)
                .collectList()
                .block();
    }

}
