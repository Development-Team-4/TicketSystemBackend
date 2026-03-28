package development.team.ticketsystem.iftests.mapper;

import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

public class TestJsonMapper {
    public static String readDataFromJson(String resourcePath) throws Exception {
        try (var inputStream = TestJsonMapper.class.getClassLoader().getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Ресурс не найден по пути: " + resourcePath);
            }

            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
