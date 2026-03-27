package development.team.ticketsystem.iftests.configuration;

import org.springframework.http.HttpMethod;

public interface TestScenarioPositive {
    String getJsonMessageType();

    String getUrlFieldName();

    boolean isRequiresId();

    HttpMethod getHttpMethod();
}