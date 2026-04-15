package development.team.ticketsystem.ticketservice.controllers;

import development.team.ticketsystem.ticketservice.TicketStatus;
import development.team.ticketsystem.ticketservice.dto.error.ErrorResponse;
import development.team.ticketsystem.ticketservice.dto.statistics.CategoryStatisticResponse;
import development.team.ticketsystem.ticketservice.dto.statistics.StatusStatisticResponse;
import development.team.ticketsystem.ticketservice.dto.statistics.TopicStatisticResponse;
import development.team.ticketsystem.ticketservice.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
@SecurityRequirement(name = "bearerAuth")
public class StatisticsController {

    private final StatisticsService statisticsService;

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
                    
                    Результат возвращается в виде массива объектов, каждый из которых содержит
                    статус и соответствующее количество тикетов.
    
                    Эндпоинт доступен только администратору.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статистика успешно получена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = StatusStatisticResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ),
                    description = "Внутренняя ошибка сервера"
            )
    })
    @GetMapping("/statuses")
    public  List<StatusStatisticResponse>  getStatusStats() {
        return statisticsService.getStatusStats();
    }

    @Operation(
            summary = "Статистика по категориям",
            description = """
                    Возвращает количество тикетов в каждой категории.
                    
                    Результат возвращается в виде массива объектов, каждый из которых содержит
                    идентификатор категории и количество тикетов в ней.
    
                    Категории, в которых нет тикетов, не включаются в результат.
    
                    Эндпоинт доступен только администратору.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статистика успешно получена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CategoryStatisticResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ),
                    description = "Внутренняя ошибка сервера"
            )
    })
    @GetMapping("/categories")
    public  List<CategoryStatisticResponse>  getCategoryStats() {
        return statisticsService.getCategoryStats();
    }

    @Operation(
            summary = "Статистика по топикам",
            description = """
                    Возвращает количество тикетов в каждой теме.
                    
                    Результат возвращается в виде массива объектов, каждый из которых содержит
                    идентификатор темы и количество тикетов в ней.
    
                    Темы, в которых нет тикетов, не включаются в результат.
    
                    Эндпоинт доступен только администратору.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статистика успешно получена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TopicStatisticResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ),
                    description = "Внутренняя ошибка сервера"
            )
    })
    @GetMapping("/topics")
    public List<TopicStatisticResponse> getTopicStats() {
        return statisticsService.getTopicStats();
    }
}