package development.team.ticketsystem.iftests.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication(
        scanBasePackages = {
                "development.team.ticketsystem.iftests"
        },
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                SqlInitializationAutoConfiguration.class
        }
)
public class TestsConfiguration {

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
