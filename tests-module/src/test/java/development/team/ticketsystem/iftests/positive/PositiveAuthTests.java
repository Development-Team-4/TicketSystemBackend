package development.team.ticketsystem.iftests.positive;

import com.fasterxml.jackson.databind.ObjectMapper;
import development.team.ticketsystem.iftests.configuration.TestsConfiguration;
import development.team.ticketsystem.iftests.dto.AuthResponse;
import development.team.ticketsystem.iftests.dto.LoginRequest;
import development.team.ticketsystem.iftests.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = TestsConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayName("Позитивные интеграционные тесты (auth-service)")
public class PositiveAuthTests {
    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    private RestClient restClient;
    private String authToken;
    private String baseUrl;

    @BeforeEach
    void setUpBase() {
        baseUrl = "http://localhost:" + port;
        restClient = restClientBuilder.build();
    }

    protected void authenticate(String email, String password) {
        var loginRequest = new LoginRequest(email, password);

        var response = restClient.post()
                .uri(baseUrl + "/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginRequest)
                .retrieve()
                .toEntity(AuthResponse.class);

        this.authToken = response.getBody().getAccessToken();
    }

    protected RestClient authenticatedClient() {
        return restClientBuilder
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .build();
    }

    @Test
    @DisplayName("ТС-Auth-1 : Успешная регистрация нового пользователя")
    void succesfulUserRegistration() {
        var request = RegisterRequest.builder()
                .email("newuser_" + System.currentTimeMillis() + "@example.com")
                .password("Password123!")
                .name("New User")
                .build();

        ResponseEntity<Void> response = restClient.post()
                .uri(baseUrl + "/auth/register")
                .body(request)
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("ТС-Auth-2 : Успешный вход с уже существующим email")
    void succesfulAuthByEmail() {
        // Создание нового пользователя

        // Вход по email
    }


}
