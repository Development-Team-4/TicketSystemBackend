package development.team.ticketsystem.iftests.configuration;

import development.team.ticketsystem.iftests.constants.JsonFiles;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

@Getter
@RequiredArgsConstructor
public enum TestRestScenarioPositive implements TestScenarioPositive {

    TEST_CREATE_COMMENT(
            JsonFiles.TicketFiles.CREATE_COMMENT_DTO,
            "http://localhost:8081/tickets/%s/comments",
            true,
            HttpMethod.POST
    ),

    TEST_CHANGE_STATUS(
            JsonFiles.TicketFiles.CHANGE_STATUS_DTO,
            "http://localhost:8081/tickets/%s/status",
            true,
            HttpMethod.PUT
    ),

    TEST_ASSIGNEE(
            JsonFiles.TicketFiles.ASSIGNEE_DTO,
            "http://localhost:8081/tickets/%s/assignee",
            true,
            HttpMethod.PUT
    ),

    TEST_CREATE_TICKET(
            JsonFiles.TicketFiles.CREATE_TICKET_DTO,
            "http://localhost:8081/tickets",
            false,
            HttpMethod.POST
    ),

    TEST_UPDATE_TICKET(
            JsonFiles.TicketFiles.UPDATE_TICKET_DTO,
            "http://localhost:8081/tickets/%s",
            true,
            HttpMethod.PUT
    );

    private final String jsonMessageType;
    private final String urlFieldName;
    private final boolean requiresId;
    private final HttpMethod httpMethod;
}