package development.team.ticketsystem.ticketservice.controllers;

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
                                    name = "Успешный ответ со списком тем",
                                    value = """
                                            [
                                              {
                                                "id": "660e8400-e29b-41d4-a716-446655440001",
                                                "name": "Техническая поддержка",
                                                "description": "Вопросы, связанные с работой программного обеспечения, ошибками, сбоями"
                                              },
                                              {
                                                "id": "660e8400-e29b-41d4-a716-446655440002",
                                                "name": "Бухгалтерия",
                                                "description": "Вопросы по оплате, счетам, возвратам, договорам"
                                              },
                                              {
                                                "id": "660e8400-e29b-41d4-a716-446655440003",
                                                "name": "Отдел кадров",
                                                "description": "Вопросы по трудоустройству, отпускам, больничным листам"
                                              },
                                              {
                                                "id": "660e8400-e29b-41d4-a716-446655440004",
                                                "name": "IT инфраструктура",
                                                "description": "Вопросы по оборудованию, сетям, доступу к ресурсам"
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 401,
                                              "error": "Unauthorized",
                                              "message": "Full authentication is required to access this resource",
                                              "path": "/topics"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
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
                                    name = "Успешный ответ",
                                    value = """
                                            {
                                              "id": "660e8400-e29b-41d4-a716-446655440005",
                                              "name": "Юридический отдел",
                                              "description": "Вопросы по договорам, правовым аспектам, юридическим консультациям"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Пустое название",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Topic name cannot be empty",
                                                      "path": "/topics"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Название слишком длинное",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Topic name cannot exceed 255 characters",
                                                      "path": "/topics"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Дубликат названия",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Topic with name 'Техническая поддержка' already exists",
                                                      "path": "/topics"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Превышение длины описания",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Description cannot exceed 2000 characters",
                                                      "path": "/topics"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 401,
                                              "error": "Unauthorized",
                                              "message": "Full authentication is required to access this resource",
                                              "path": "/topics"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. Только администраторы могут создавать темы.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 403,
                                              "error": "Forbidden",
                                              "message": "Access denied. Only ADMIN role can create topics",
                                              "path": "/topics"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
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
                                            value = """
                                                    {
                                                      "name": "Юридический отдел",
                                                      "description": "Вопросы по договорам, правовым аспектам, юридическим консультациям"
                                                    }
                                                    """
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
                                    name = "Успешный ответ",
                                    value = """
                                            {
                                              "id": "660e8400-e29b-41d4-a716-446655440001",
                                              "name": "Техническая поддержка (обновлено)",
                                              "description": "Вопросы, связанные с работой программного обеспечения, ошибками, сбоями системы"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Пустое название",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Topic name cannot be empty",
                                                      "path": "/topics/660e8400-e29b-41d4-a716-446655440001"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Дубликат названия",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Topic with name 'Бухгалтерия' already exists",
                                                      "path": "/topics/660e8400-e29b-41d4-a716-446655440001"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тема с указанным ID не найдена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Topic not found with id: 660e8400-e29b-41d4-a716-446655440999",
                                              "path": "/topics/660e8400-e29b-41d4-a716-446655440999"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 401,
                                              "error": "Unauthorized",
                                              "message": "Full authentication is required to access this resource",
                                              "path": "/topics/660e8400-e29b-41d4-a716-446655440001"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. Только администраторы могут обновлять темы.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 403,
                                              "error": "Forbidden",
                                              "message": "Access denied. Only ADMIN role can update topics",
                                              "path": "/topics/660e8400-e29b-41d4-a716-446655440001"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
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
                                        value = """
                                                {
                                                  "name": "Техническая поддержка и IT сервис",
                                                  "description": "Комплексная поддержка по всем IT вопросам, включая оборудование, ПО и сетевые ресурсы"
                                                }
                                                """
                                )
                        }
                )
        )
        @RequestBody CreateTopicRequest request
    ) {
        return service.update(role, id, request);
    }

}