package development.team.ticketsystem.notificationservice.service;

import development.team.ticketsystem.notificationservice.dto.NotificationCreationDto;
import development.team.ticketsystem.notificationservice.exceptions.NotificationFormatException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Component
public class NotificationValidator {
    private final Set<String> fieldNames = new HashSet<>();

    @PostConstruct
    private void postConstruct() {
        for(Field field : NotificationCreationDto.class.getDeclaredFields()) {
            fieldNames.add(field.getName());
        }
    }

    /**
     * Метод валидации DTO
     *
     * @param dto DTO создания уведомления
     */
    public void validate(NotificationCreationDto dto) throws NotificationFormatException {
        for(Field field : dto.getClass().getDeclaredFields()) {
            if(!fieldNames.contains(field.getName())) {
                throw new NotificationFormatException(field.getName() + " не может быть null");
            }
        }
    }
}
