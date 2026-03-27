package development.team.ticketsystem.iftests.configuration;

import lombok.Builder;

@Builder
public record TestScenarioSteps(
        boolean sendPostRequest,
        boolean sendPutRequest,
        boolean hasAttributes
) {
}
