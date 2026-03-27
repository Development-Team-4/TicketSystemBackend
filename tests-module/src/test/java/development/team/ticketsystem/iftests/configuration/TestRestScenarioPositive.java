package development.team.ticketsystem.iftests.configuration;

import development.team.ticketsystem.iftests.constants.JsonFiles;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TestRestScenarioPositive implements TestScenarioPositive {
    TEST_CREATE_COMMENT(
            JsonFiles.TicketFiles.CREATE_COMMENT_DTO,
            "",
            ""
    ),

    TEST_CHANGE_STATUS(
            JsonFiles.TicketFiles.CHANGE_STATUS_DTO,
            "",
            ""
    ),

    TEST_ASSIGNEE(
            JsonFiles.TicketFiles.ASSIGNEE_DTO,
            "",
            ""
    ),

    TEST_CREATE_TICKET(
            JsonFiles.TicketFiles.CREATE_TICKET_DTO,
            "",
            ""
    ),

    TEST_UPDATE_TICKET(
            JsonFiles.TicketFiles.UPDATE_TICKET_DTO,
            "",
            ""
    );

    private final String jsonMessageType;
    private final String method;
    private final String attribute;
}
