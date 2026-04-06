package development.team.ticketsystem.iftests.positive;

import com.fasterxml.jackson.databind.ObjectMapper;
import development.team.ticketsystem.iftests.configuration.RestConfiguration;
import development.team.ticketsystem.iftests.configuration.TestRestScenarioPositive;
import development.team.ticketsystem.iftests.configuration.TestsConfiguration;
import development.team.ticketsystem.iftests.dto.NotificationDto;
import development.team.ticketsystem.iftests.mapper.TestJsonMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestsConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application.yaml")
@DisplayName("Позитивные интеграционные тесты")
public class PositiveTicketNotificationTests {


}