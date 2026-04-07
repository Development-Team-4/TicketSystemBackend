package development.team.ticketsystem.iftests.positive;

import com.fasterxml.jackson.databind.ObjectMapper;
import development.team.ticketsystem.iftests.configuration.RestConfiguration;
import development.team.ticketsystem.iftests.configuration.UnifiedTestConfiguration;
import development.team.ticketsystem.iftests.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = UnifiedTestConfiguration.class)
@DisplayName("Позитивные интеграционные тесты (auth-service)")
public class PositiveAuthTests {
    @Autowired
    private RestConfiguration restConfiguration;

    @Autowired
    private RestClient.Builder restClientBuilder;

    private RestClient restClient;
    private String authToken;
    private String baseUrl;

    @BeforeEach
    void setUpBase() {
        this.baseUrl = this.restConfiguration.getUrls().getBaseUrl()
                + this.restConfiguration.getUrls().getGateway().getPort();
        this.restClient = RestClient.builder().build();
    }

    @Test
    @DisplayName("ТС-Auth-1 : Успешная регистрация нового пользователя")
    void succesfulUserRegistration() {
        String urlToRegister = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                + this.restConfiguration.getUrls().getAuth().getRegister();

        var request = RegisterRequest.builder()
                .email("newuser_" + System.currentTimeMillis() + "@example.com")
                .password("Password123!")
                .name("New User")
                .build();

        ResponseEntity<Void> response = restClient.post()
                .uri(urlToRegister)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("ТС-Auth-2 : Успешный вход и получение данных из tickets-service")
    void succesfulAuthGettingTickets() {
        String urlTickets = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getTicket().getCommonUrl();

        this.commonSuccesfulAuthorizationAndGettingData(urlTickets);
    }

    @Test
    @DisplayName("ТС-Auth-5 : Получение данных о себе без регистрации")
    void takingUserDataWithoutRegistration() {
        String url = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getAuth().getPort()
                + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                + this.restConfiguration.getUrls().getAuth().getMe();

        UserResponse response = this.restClient.get().uri(url)
                .retrieve()
                .body(UserResponse.class);

        assertEquals(null, response.getId());
        assertEquals(null, response.getRole());
        assertEquals(null, response.getCreatedAt());
        assertEquals(null, response.getName());
        assertEquals(null, response.getEmail());
        assertEquals(null, response.getAvatar());
    }

    private void commonSuccesfulAuthorizationAndGettingData(String url) {
        // Данные пользователя
        String password = "password123!";
        String userName = "New User" + System.currentTimeMillis();
        String email = "newuser_" + System.currentTimeMillis() + "@example.com";

        // Создание нового пользователя
        var request = RegisterRequest.builder()
                .email(email)
                .password(password)
                .name(userName)
                .build();

        System.out.println(request);

        String urlRegister = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                + this.restConfiguration.getUrls().getAuth().getRegister();

        ResponseEntity<AuthResponse> response = restClient.post()
                .uri(urlRegister)
                .body(request)
                .retrieve()
                .toEntity(AuthResponse.class);

        this.authToken = response.getBody().getAccessToken();

        // Получение данных
        authenticatedClient().get()
                .uri(url)
                .exchange((request1, response1) -> {
                    assertEquals(200, response1.getStatusCode().value(),
                            "Ожидался статус 200 при запросе с авторизацией");
                    return null;
                });
    }

    private void authenticate(String email, String password) {
        var loginRequest = new LoginRequest(email, password);

        var response = restClient.post()
                .uri(baseUrl
                        + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                        + this.restConfiguration.getUrls().getAuth().getLogin())
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginRequest)
                .retrieve()
                .toEntity(AuthResponse.class);

        this.authToken = response.getBody().getAccessToken();
    }

    private RestClient authenticatedClient() {
        return this.restClientBuilder
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.authToken)
                .build();
    }
}
