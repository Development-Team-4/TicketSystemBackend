package development.team.ticketsystem.iftests.positive;

import development.team.ticketsystem.iftests.configuration.RestConfiguration;
import development.team.ticketsystem.iftests.configuration.TestsConfiguration;
import development.team.ticketsystem.iftests.dto.AuthResponse;
import development.team.ticketsystem.iftests.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = TestsConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayName("Позитивные интеграционные тесты (TicketService)")
public class PositiveAuthAndTicketTests {
    private RestClient adminRestClient;

    private RestClient authClient;

    @Autowired
    private RestConfiguration restConfiguration;

    @BeforeEach
    void setUp() {
        String baseUrl = restConfiguration.getBaseUrl() + restConfiguration.getAuthPort();

        // RestClient для админа
        adminRestClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor((request, body, execution) -> {
                    request.getHeaders().setBearerAuth(getAdminToken());
                    return execution.execute(request, body);
                })
                .build();
    }

    @Test
    void createTicket() {
        String response = adminRestClient.post()
                .uri("")
                .header("Authorization", "Bearer " + getAdminToken())
                .retrieve()
                .body(String.class);

        assertThat(response).isNotNull();
    }

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest("user@example.com", "password123");

        AuthResponse response = authClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(AuthResponse.class);

        assertThat(response.getAccessToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();
    }

    private String getAdminToken() {
        LoginRequest loginRequest = new LoginRequest("admin@example.com", "$2a$10$gQKePkU.DA4vpUKj9dbdduXbVcp5ujpocgdqQP/nd4hhsvMUcxgam");

        AuthResponse response = authClient.post()
                .uri("http://localhost:8082/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginRequest)
                .retrieve()
                .body(AuthResponse.class);

        return response.getAccessToken();
    }
}
