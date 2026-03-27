package development.team.ticketsystem.iftests.configuration;

import lombok.Builder;

@Builder
public record TestScenarioSteps(
        boolean hasAttributes
) {

    public static TestScenarioSteps defaultSteps() {
        return TestScenarioSteps.builder()
                .hasAttributes(false)
                .build();
    }

    public static TestScenarioSteps withAttributes() {
        return TestScenarioSteps.builder()
                .hasAttributes(true)
                .build();
    }
}