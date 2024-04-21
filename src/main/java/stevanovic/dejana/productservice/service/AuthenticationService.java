package stevanovic.dejana.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String USER_SERVICE_URL = "http://user-service/api/user";

    private final RestTemplate restTemplate;

    public boolean validateToken(String jwtToken) {
        String url = USER_SERVICE_URL + "/validate-token";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getStatusCode().is2xxSuccessful();
    }

    public boolean validateAdminToken(String jwtToken) {
        String url = USER_SERVICE_URL + "/admin/validate-token";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getStatusCode().is2xxSuccessful();
    }
}
