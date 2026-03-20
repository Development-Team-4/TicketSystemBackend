package development.team.ticketsystem.notificationservice.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    /**
     * Метод для удаления поля из JSON
     *
     * @param json строка JSON-а
     * @param field поле для удаления
     * @return строку JSON-а без поля
     */
    public static String corruptField(String json, String field) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = (ObjectNode) mapper.readTree(json);
            node.remove(field);
            return mapper.writeValueAsString(node);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке JSON: " + e.getMessage(), e);
        }
    }
}
