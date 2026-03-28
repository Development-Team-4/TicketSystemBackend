package development.team.ticketsystem.ticketservice.controllers;

import development.team.ticketsystem.ticketservice.dto.error.ErrorResponse;
import development.team.ticketsystem.ticketservice.dto.topics.CreateTopicRequest;
import development.team.ticketsystem.ticketservice.dto.topics.TopicResponse;
import development.team.ticketsystem.ticketservice.service.TopicService;
import development.team.ticketsystem.ticketservice.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/topics")
@Tag(name = "Topics", description = """
Управление темами в системе поддержки.
 Темы представляют собой верхнеуровневые категории, объединяющие связанные тикеты.
 Например: 'Техническая поддержка', 'IT отдел'.
""")
public class TopicController {

    private final TopicService service;

    @Operation(
            summary = "Получить все темы",
            description = """
                    Возвращает список всех доступных тем в системе поддержки.
                    
                    Темы используются для:
                    - Группировки тикетов по крупным направлениям деятельности
                    - Организации структуры поддержки
                    - Навигации пользователей при создании тикетов
                    
                    Каждая тема содержит:
                    - Уникальный идентификатор
                    - Название темы (например, "Техническая поддержка")
                    - Описание темы (пояснение, какие вопросы относятся к этой теме)
                    
                    Эндпоинт доступен всем авторизованным пользователям системы.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список тем успешно получен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TopicResponse.class)),
                            examples = @ExampleObject(
                                    name = "Успешный ответ со списком тем"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping
    public List<TopicResponse> getTopics() {
        return service.getAll();
    }

    @Operation(
            summary = "Создать тему",
            description = """
                    Создает новую тему в системе поддержки.
                    
                    Тема является корневым элементом иерархии классификации тикетов.
                    После создания темы, в ней можно будет создавать категории, а затем и тикеты.
                    
                    Требования к данным:
                    - Название темы обязательно для заполнения
                    - Название должно быть уникальным в системе
                    - Длина названия: не более 255 символов
                    - Длина описания: не более 2000 символов
                    
                    Эндпоинт доступен только администраторам системы.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Тема успешно создана",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TopicResponse.class),
                            examples = @ExampleObject(
                                    name = "Успешный ответ"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Пустое название"
                                    ),
                                    @ExampleObject(
                                            name = "Название слишком длинное"
                                    ),
                                    @ExampleObject(
                                            name = "Превышение длины описания"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Пользователь не авторизован"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. Только администраторы могут создавать темы.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Доступ запрещен"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TopicResponse createTopic(
            @RequestHeader("X-User-Role") UserRole role,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания темы. Поле 'name' обязательно. Поле 'description' - опционально.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateTopicRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Пример тела запроса"
                                    )
                            }
                    )
            )
            @RequestBody CreateTopicRequest request
    ) {
        return service.create(role, request);
    }

    @Operation(
            summary = "Обновить тему",
            description = """
                    Обновляет данные существующей темы.
                    
                    Можно изменить название и/или описание темы. При обновлении проверяется,
                    что новое название не конфликтует с другими темами в системе.
                    
                    Особенности обновления:
                    - Поле name обязательно, поле description опционально
                    - Нельзя изменить идентификатор темы (ID)
                    - После обновления все связанные с темой категории и тикеты сохраняют привязку к теме
                    
                    Эндпоинт доступен только администраторам системы.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Тема успешно обновлена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TopicResponse.class),
                            examples = @ExampleObject(
                                    name = "Успешный ответ"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Пустое название"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тема с указанным ID не найдена",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Нет указанной темы"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. Только администраторы могут обновлять темы.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Недостаточно прав на обновление темы"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public TopicResponse updateTopic(
        @RequestHeader("X-User-Role") UserRole role,

        @Parameter(
                description = "Уникальный идентификатор темы, которую необходимо обновить",
                example = "660e8400-e29b-41d4-a716-446655440001",
                required = true
        )
        @PathVariable UUID id,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Данные для обновления темы. Поле name обязательно, поле description опционально",
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = CreateTopicRequest.class),
                        examples = {
                                @ExampleObject(
                                        name = "Пример тела запроса"
                                )
                        }
                )
        )
        @RequestBody CreateTopicRequest request
    ) {
        return service.update(role, id, request);
    }

}