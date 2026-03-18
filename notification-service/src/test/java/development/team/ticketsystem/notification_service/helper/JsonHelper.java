package development.team.ticketsystem.notification_service.helper;

import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

public class JsonHelper {
    /**
     * Статический метод для считывания данных из JSON-а
     *
     * @param resourcePath путь до файла
     * @return данные из файла в виде строки
     * @throws Exception в случае, если нет такого ресурса
     */
    public static String loadResourceAsString(String resourcePath) throws Exception {
        try (var inputStream = JsonHelper.class.getClassLoader().getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Ресурс не найден по пути: " + resourcePath);
            }

            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
