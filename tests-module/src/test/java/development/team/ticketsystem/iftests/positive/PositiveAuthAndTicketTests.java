package development.team.ticketsystem.iftests.positive;

import development.team.ticketsystem.iftests.configuration.RestConfiguration;
import development.team.ticketsystem.iftests.configuration.UnifiedTestConfiguration;
import development.team.ticketsystem.iftests.dto.AuthResponse;
import development.team.ticketsystem.iftests.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = UnifiedTestConfiguration.class)
@DisplayName("Позитивные интеграционные тесты (TicketService)")
@RequiredArgsConstructor
public class PositiveAuthAndTicketTests {
    private final RestClient adminRestClient = RestClient.builder().build();

    private final RestClient authClient = RestClient.builder().build();

    @Autowired
    private RestConfiguration restConfiguration;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        this.baseUrl = restConfiguration.getUrls().getBaseUrl() + restConfiguration.getUrls().getAuth().getPort();

        // RestClient для админа
        //adminRestClient = RestClient.builder()
        //        .baseUrl(this.baseUrl)
        //        .requestInterceptor((request, body, execution) -> {
        //            request.getHeaders().setBearerAuth(getAdminToken());
        //            return execution.execute(request, body);
        //        })
        //        .build();
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

    private String getAdminToken() {
        LoginRequest loginRequest = new LoginRequest(
                "admin@example.com",
                "$2a$10$gQKePkU.DA4vpUKj9dbdduXbVcp5ujpocgdqQP/nd4hhsvMUcxgam"
        );

        AuthResponse response = authClient.post()
                .uri("http://localhost:8081/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginRequest)
                .retrieve()
                .body(AuthResponse.class);

        return response.getAccessToken();
    }
}
