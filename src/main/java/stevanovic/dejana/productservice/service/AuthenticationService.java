package stevanovic.dejana.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import stevanovic.dejana.productservice.dto.AuthenticationResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String USER_SERVICE_URL = "http://user-service/api";

    private final WebClient.Builder webClientBuilder;

    public Mono<AuthenticationResponse> authenticateUser(String jwtToken) {
        WebClient webClient = webClientBuilder.baseUrl(USER_SERVICE_URL).build();

        return webClient.post()
                .uri("/validate-token")
                .header(AUTHORIZATION_HEADER, jwtToken)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                    if (isClientError(responseBody)) {
                        return Mono.error(new RuntimeException("Client error"));
                    } else if (isServerError(responseBody)) {
                        return Mono.error(new RuntimeException("Server error"));
                    } else {
                        return Mono.just(new AuthenticationResponse(true, responseBody));
                    }
                })
                .onErrorResume(error -> Mono.just(new AuthenticationResponse(false, error.getMessage())));
    }

    private boolean isClientError(String responseBody) {
        return responseBody != null && responseBody.startsWith("4");
    }

    private boolean isServerError(String responseBody) {
        return responseBody != null && responseBody.startsWith("5");
    }
}
