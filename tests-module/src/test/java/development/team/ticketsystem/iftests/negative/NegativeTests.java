package development.team.ticketsystem.iftests.negative;

import development.team.ticketsystem.iftests.configuration.RestConfiguration;
import development.team.ticketsystem.iftests.dto.AuthResponse;
import development.team.ticketsystem.iftests.dto.LoginRequest;
import development.team.ticketsystem.iftests.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {RestConfiguration.class})
@DisplayName("Негативные интеграционные тесты")
@RequiredArgsConstructor
public class NegativeTests {
    @Autowired
    private RestConfiguration restConfiguration;

    private RestClient restClient = RestClient.builder().build();

    @DisplayName("ТС-Gateway-1 : Попытка получить данные ticket-service без авторизации")
    @Test
    void noAuthorizedAttemptTicketService() {
        // Почему-то не получаю данные из RestConfiguration
        //String url = this.restConfiguration.getUrls().getBaseUrl()
        //        + this.restConfiguration.getUrls().getGateway().getPort()
        //        + "/auth/users";
        this.noAuthorizedCommonTest("http://localhost:8081/tickets");
    }

    @DisplayName("ТС-Gateway-2 : Попытка получить данные auth-service без авторизации")
    @Test
    void noAuthorizedAttemptAuthService() {
        // Почему-то не получаю данные из RestConfiguration
        //String url = this.restConfiguration.getUrls().getBaseUrl()
        //        + this.restConfiguration.getUrls().getGateway().getPort()
        //        + "/auth/users";
        this.noAuthorizedCommonTest("http://localhost:8081/users");
    }

    @DisplayName("ТС-Gateway-3 : Попытка получить данные notification-service без авторизации")
    @Test
    void noAuthorizedAttemptNotificationService() {
        // Почему-то не получаю данные из RestConfiguration
        //String url = this.restConfiguration.getUrls().getBaseUrl()
        //        + this.restConfiguration.getUrls().getGateway().getPort()
        //        + "/auth/users";
        this.noAuthorizedCommonTest("http://localhost:8081/notifications");
    }

    @Test
    @DisplayName("ТС-Gateway-4 : Успешный вход и неудачная попытка получения из notification-service")
    void succesfulAuthGettingNotifications() {
        this.commonSuccesfulAuthorizationAndGettingData("http://localhost:8081/notifications");
    }

    @Test
    @DisplayName("ТС-Gateway-5 : Успешный вход и неудачная попытка получения данных из auth-service")
    void succesfulAuthGettingsUsers() {
        this.commonSuccesfulAuthorizationAndGettingData("http://localhost:8081/users");
    }

    @Test
    @DisplayName("ТС-Gateway-6 : Попытка зайти по несуществующим данным")
    void tryToLoginByNonExistedData() {
        // Получить рандомного пользователя (в обход gateway)
        ResponseEntity<List> users =
                this.restClient.get()
                        .uri("http://localhost:8082/users")
                        .retrieve()
                        .toEntity(List.class);

        System.out.println(users);

        Map<String, Object> user = (LinkedHashMap) users.getBody().getFirst();

        // Изменение данных
        String newEmail = corruptEmail(user.get("userEmail").toString());
        String newPassword = "corruptPassword(user.getName());";

        // Отправка
        this.restClient.post()
                .uri("http://localhost:8081/auth/login")
                .body(new LoginRequest(
                        newEmail, newPassword
                ))
                .exchange((request1, response1) -> {
                    assertEquals(401, response1.getStatusCode().value(),
                            "Ожидался статус 401 при запросе с авторизацией");
                    return null;
                });
    }

    @Test
    @DisplayName("ТС-Gateway-7 : Попытка зайти по существующему имени и не по тому паролю")
    void tryLoginInSystemBySameUsernameAndOtherPassword() {
        // Регистрируем первого пользователя
        String username = "";
        String email = "";
        String password = "";

        this.restClient.post()
                .uri("http://localhost:8081/auth/register")
                .body(RegisterRequest.builder()
                        .email(email)
                        .password(password)
                        .name(username)
                        .build());

        // Пытаемся войти по тому же email-у, но с другим паролем
        this.restClient.post()
                .uri("http://localhost:8081/auth/login")
                .body(new LoginRequest(
                        email, password + UUID.randomUUID()
                ))
                .exchange((request1, response1) -> {
                    assertEquals(401, response1.getStatusCode().value(),
                            "Ожидался статус 401 при запросе на вход в систему");
                    return null;
                });
    }

    private String corruptEmail(String email) {
        String[] parts = email.split("@");
        String beforeAtSymbol = parts[0];
        String afterAtSymbol = parts[1];

        beforeAtSymbol += UUID.randomUUID().toString();

        return beforeAtSymbol + afterAtSymbol;
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

        ResponseEntity<AuthResponse> response = restClient.post()
                .uri("http://localhost:8081/auth/register")
                .body(request)
                .retrieve()
                .toEntity(AuthResponse.class);

        String accessToken = response.getBody().getAccessToken();

        // Получение данных
        this.restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .exchange((request1, response1) -> {
                    assertEquals(403, response1.getStatusCode().value(),
                            "Ожидался статус 403 при запросе с авторизацией");
                    return null;
                });
    }

    private void noAuthorizedCommonTest(String url) {
        this.restClient.get()
                .uri(url)
                .exchange((request, response) -> {
                    assertEquals(401, response.getStatusCode().value(),
                            "Ожидался статус 401 при запросе без авторизации");
                    return null;
                });
    }
}
