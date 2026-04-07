package development.team.ticketsystem.iftests.negative;

import development.team.ticketsystem.iftests.configuration.RestConfiguration;
import development.team.ticketsystem.iftests.configuration.UnifiedTestConfiguration;
import development.team.ticketsystem.iftests.dto.AuthResponse;
import development.team.ticketsystem.iftests.dto.LoginRequest;
import development.team.ticketsystem.iftests.dto.RegisterRequest;
import development.team.ticketsystem.iftests.dto.UserResponse;
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

@SpringBootTest(classes = UnifiedTestConfiguration.class)
@DisplayName("Негативные интеграционные тесты")
@RequiredArgsConstructor
public class NegativeTests {
    @Autowired
    private RestConfiguration restConfiguration;

    private RestClient restClient = RestClient.builder().build();

    @DisplayName("ТС-Gateway-1 : Попытка получить данные ticket-service без авторизации")
    @Test
    void noAuthorizedAttemptTicketService() {
        String url = this.restConfiguration.getUrls().getBaseUrl() + ":"
               + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getTicket().getCommonUrl();
        System.out.println(url);

        this.noAuthorizedCommonTest(url);
    }

    @DisplayName("ТС-Gateway-2 : Попытка получить данные auth-service без авторизации")
    @Test
    void noAuthorizedAttemptAuthService() {
        String url = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getUsersUrl();
        this.noAuthorizedCommonTest(url);
    }

    @DisplayName("ТС-Gateway-3 : Попытка получить данные notification-service без авторизации")
    @Test
    void noAuthorizedAttemptNotificationService() {
        String url = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getNotification().getCommonUrl();
        this.noAuthorizedCommonTest(url);
    }

    @Test
    @DisplayName("ТС-Gateway-4 : Успешный вход и неудачная попытка получения из notification-service")
    void succesfulAuthGettingNotifications() {
        String url = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getNotification().getCommonUrl();

        this.commonSuccesfulAuthorizationAndGettingData(url);
    }

    @Test
    @DisplayName("ТС-Gateway-5 : Успешный вход и неудачная попытка получения данных из auth-service")
    void succesfulAuthGettingsUsers() {
        String url = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getUsersUrl();

        this.commonSuccesfulAuthorizationAndGettingData(url);
    }

    @Test
    @DisplayName("ТС-Gateway-6 : Попытка зайти по несуществующим данным")
    void tryToLoginByNonExistedData() {
        // Получить рандомного пользователя (в обход gateway)
        String bypassGatewayUrl = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getAuth().getPort()
                + this.restConfiguration.getUrls().getAuth().getUsersUrl();

        ResponseEntity<List> users =
                this.restClient.get()
                        .uri(bypassGatewayUrl)
                        .retrieve()
                        .toEntity(List.class);

        System.out.println(users);

        Map<String, Object> user = (LinkedHashMap) users.getBody().getFirst();

        // Изменение данных
        String url = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                + this.restConfiguration.getUrls().getAuth().getLogin();

        String newEmail = corruptEmail(user.get("userEmail").toString());
        String newPassword = "corruptPassword(user.getName());";

        // Отправка
        this.restClient.post()
                .uri(url)
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
        String urlToRegister = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                + this.restConfiguration.getUrls().getAuth().getRegister();

        String urlToLogin = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                + this.restConfiguration.getUrls().getAuth().getLogin();

        String username = "";
        String email = "";
        String password = "";

        this.restClient.post()
                .uri(urlToRegister)
                .body(RegisterRequest.builder()
                        .email(email)
                        .password(password)
                        .name(username)
                        .build());

        // Пытаемся войти по тому же email-у, но с другим паролем
        this.restClient.post()
                .uri(urlToLogin)
                .body(new LoginRequest(
                        email, password + UUID.randomUUID()
                ))
                .exchange((request1, response1) -> {
                    assertEquals(401, response1.getStatusCode().value(),
                            "Ожидался статус 401 при запросе на вход в систему");
                    return null;
                });
    }

    @Test
    @DisplayName("ТС-Gateway-8 : Попытка зарегистрироваться по некорректному email")
    void registerViaIncorrectEmail() {
        // Регистрируем пользователя
        String urlToRegister = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                + this.restConfiguration.getUrls().getAuth().getRegister();

        String username = "userName" + System.currentTimeMillis();
        String email = "98odjkdgoi4gjkdl"; // некорректное поле
        String password = "qwerty";

        this.restClient.post()
                .uri(urlToRegister)
                .body(RegisterRequest.builder()
                        .email(email)
                        .password(password)
                        .name(username)
                        .build())
                .exchange((request1, response1) -> {
                    assertEquals(403, response1.getStatusCode().value(),
                            "Ожидался статус 403 при попытке регистрации");
                    return null;
                });
    }

    @Test
    @DisplayName("ТС-Gateway-9 : Попытка получить все тикеты через обычного пользователя")
    void tryGetAllTicketsViaSimpleUserRole() {
        // Добавление новых тикетов (не для пользователя)

        // Регистрация обычного пользователя
        String urlToRegister = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                + this.restConfiguration.getUrls().getAuth().getRegister();

        String username = "userName" + System.currentTimeMillis();
        String email = "example@example.com";
        String password = "qwerty";

        AuthResponse response = this.restClient.post()
                .uri(urlToRegister)
                .body(RegisterRequest.builder()
                        .email(email)
                        .password(password)
                        .name(username)
                        .build()
                )
                .retrieve()
                .body(AuthResponse.class);
        String accessToken = response.getAccessToken();

        // Попытка получить все токены
        this.restClient.get()
                .uri(this.restConfiguration.getUrls().getBaseUrl() + ":"
                        + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getTicket().getCommonUrl())
                .header("Authorization", "Bearer " + accessToken)
                .exchange((request1, response1) -> {
                    assertEquals(403, response1.getStatusCode().value(),
                            "Ожидался статус 403 при попытке регистрации");
                    return null;
                });
    }

    @Test
    @DisplayName("ТС-Gateway-10 : Попытка зарегистрировать пользователя с таким же email")
    void tryRegisterUserWithExistedEmail() {
        // Регистрация обычного пользователя
        String urlToRegister = this.restConfiguration.getUrls().getBaseUrl() + ":"
                + this.restConfiguration.getUrls().getGateway().getPort()
                + this.restConfiguration.getUrls().getAuth().getAuthUrl()
                + this.restConfiguration.getUrls().getAuth().getRegister();

        String username = "userName" + System.currentTimeMillis();
        String email = "example111@example.com";
        String password = "qwerty";

        this.restClient.post()
                .uri(urlToRegister)
                .body(RegisterRequest.builder()
                        .email(email)
                        .password(password)
                        .name(username)
                        .build()
                );

        // Повторная регистрация
        this.restClient.post()
                .uri(urlToRegister)
                .body(RegisterRequest.builder()
                        .email(email)
                        .password(password)
                        .name(username + "89758478")
                        .build()
                )
                .exchange((request1, response1) -> {
                    assertEquals(409, response1.getStatusCode().value(),
                            "Ожидался статус 409 при попытке регистрации");
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
