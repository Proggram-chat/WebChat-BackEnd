package sanity.nil.webchat.infrastructure.channels.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoBroadcastPayload;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoResponseDTO;
import sanity.nil.webchat.application.exceptions.CentrifugoException;

import java.time.Duration;

@Component
public class CentrifugoHelper {

    @Value("${application.centrifugo.base-url}")
    private String baseURL;
    @Value("${application.centrifugo.api-key}")
    private String apiKey;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .baseUrl(baseURL)
                .build();
    }

    public Flux<CentrifugoResponseDTO> broadcast(CentrifugoBroadcastPayload payload) {
        return webClient.post()
                .uri("/api/broadcast")
                .headers(e -> e.addAll(getHeaders()))
                .body(BodyInserters.fromValue(payload))
                .exchangeToFlux(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) {
                        return resp.bodyToFlux(CentrifugoResponseDTO.class);
                    } else {
                        return Flux.error(new CentrifugoException("Got an error from centrifugo", resp.statusCode().value()));
                    }
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(3)).jitter(0.50)
                        .filter(ex -> ex instanceof CentrifugoException));
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-Key", apiKey);
        headers.add("X-Centrifugo-Error-Mode", "transport");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }
}
