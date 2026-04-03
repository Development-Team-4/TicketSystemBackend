package development.team.ticketsystem.ticketservice.controllers;


import development.team.ticketsystem.ticketservice.TicketStatus;
import development.team.ticketsystem.ticketservice.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = """
        Статистика по тикетам в системе поддержки.
        
        Позволяет получать агрегированные данные по тикетам:
        - Распределение по статусам
        - Распределение по категориям
        - Распределение по темам
        """)
public class StatisticsController {

    private final StatisticsService service;

    @Operation(
            summary = "Статистика по статусам тикетов",
            description = """
                    Возвращает количество тикетов для каждого статуса.
                    
                    Включает все возможные статусы:
                    - OPEN
                    - ASSIGNED
                    - IN_PROGRESS
                    - RESOLVED
                    - CLOSED
                    
                    Результат представляет собой отображение:
                    статус -> количество тикетов
                    
                    Этот эндпоинт доступен только администратору.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статистика успешно получена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Успешный ответ"
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/statuses")
    public Map<TicketStatus, Long> getStatusStats() {
        return service.getStatusStats();
    }

    @Operation(
            summary = "Статистика по категориям",
            description = """
                    Возвращает количество тикетов в каждой категории.
                    
                    Результат:
                    categoryId -> количество тикетов
                    
                    Этот эндпоинт доступен только администратору.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статистика успешно получена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Успешный ответ"
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/categories")
    public Map<UUID, Long> getCategoryStats() {
        return service.getCategoryStats();
    }

    @Operation(
            summary = "Статистика по топикам",
            description = """
                Возвращает количество тикетов в каждом топикае.
                
                Результат:
                topic -> количество тикетов
                
                Этот эндпоинт доступен только администратору.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статистика успешно получена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Успешный ответ"
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/topics")
    public Map<UUID, Long> getTopicStats() {
        return service.getTopicStats();
    }
}