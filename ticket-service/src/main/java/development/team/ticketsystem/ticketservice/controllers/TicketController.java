package development.team.ticketsystem.ticketservice.controllers;

import development.team.ticketsystem.ticketservice.dto.tickets.*;
import development.team.ticketsystem.ticketservice.service.TicketService;
import development.team.ticketsystem.ticketservice.TicketStatus;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tickets")
@Tag(name = "Tickets", description = """
        Управление тикетами в системе поддержки. Тикеты являются основными единицами работы системы.
        
        Тикет представляет собой запрос пользователя о проблеме, вопросе или задаче, требующей решения.
        
        Основные сущности, связанные с тикетом:
        - Категория: определяет тематику тикета (например, "Проблемы с авторизацией")
        - Автор: пользователь, создавший тикет
        - Исполнитель: сотрудник, назначенный на решение тикета
        - Комментарии: обсуждение тикета между участниками
        - Статус: текущее состояние тикета (OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED)
        
        Права доступа к тикетам:
        - Администратор: полный доступ ко всем тикетам
        - Сотрудник поддержки: доступ к назначенным тикетам и тикетам своей категории
        - Пользователь: доступ только к своим тикетам
        """)
public class TicketController {

    private final TicketService service;


    @Operation(
            summary = "Создать тикет",
            description = """
                    Создает новый тикет в системе поддержки.
                    
                    При создании тикета необходимо указать:
                    - Тему (subject): краткое описание проблемы (обязательно)
                    - Описание (description): детальное описание проблемы (обязательно)
                    - Категорию (categoryId): ID категории, к которой относится тикет (обязательно)
                    
                    Система автоматически:
                    - Присваивает тикету уникальный идентификатор
                    - Устанавливает статус OPEN (открыт)
                    - Фиксирует автора из JWT
                    - Устанавливает временные метки создания и обновления
                    
                    После создания тикета:
                    - Автор получает уведомление о создании
                    - Администраторы видят новый тикет в списке нераспределенных
                    - Тикет берется в работу одним из сотрудников категории
                    
                    Эндпоинт доступен всем авторизованным пользователям.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Тикет успешно создан",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TicketResponse.class),
                            examples = @ExampleObject(
                                    name = "Успешный ответ",
                                    value = """
                                            {
                                              "id": "550e8400-e29b-41d4-a716-446655440001",
                                              "subject": "Не работает авторизация в личном кабинете",
                                              "description": "При попытке войти в личный кабинет после ввода логина и пароля выдается ошибка 500 Internal Server Error",
                                              "status": "OPEN",
                                              "categoryId": "770e8400-e29b-41d4-a716-446655440001",
                                              "createdBy": "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
                                              "assigneeId": null,
                                              "createdAt": "2024-01-15T10:30:00Z",
                                              "updatedAt": "2024-01-15T10:30:00Z"
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
                                            name = "Пустая тема",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Ticket subject cannot be empty",
                                                      "path": "/tickets"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Пустое описание",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Ticket description cannot be empty",
                                                      "path": "/tickets"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Категория не найдена",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Category not found with id: 770e8400-e29b-41d4-a716-446655440999",
                                                      "path": "/tickets"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(
            @RequestHeader("X-User-Id") UUID userId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания тикета. Все поля обязательны для заполнения.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateTicketRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "subject": "Не работает авторизация в личном кабинете",
                                              "description": "При попытке войти в личный кабинет после ввода логина и пароля выдается ошибка 500 Internal Server Error",
                                              "categoryId": "770e8400-e29b-41d4-a716-446655440001"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody CreateTicketRequest request
    ) {
        return service.create(userId, request);
    }

    @Operation(
            summary = "Получить список тикетов с фильтрацией",
            description = """
                    Возвращает список тикетов с возможностью фильтрации по различным параметрам.
                    
                    Доступные фильтры (все параметры опциональны):
                    - categoryId: фильтр по категории тикета
                    - assignedTo: фильтр по назначенному сотруднику
                    - createdBy: фильтр по автору тикета
                    - status: фильтр по статусу (OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED)
                    - createdAfter: фильтр по дате создания (тикеты созданные после указанной даты)
                    - createdBefore: фильтр по дате создания (тикеты созданные до указанной даты)
                    
                    Правила применения фильтров в зависимости от роли пользователя:
                    - Администратор: может использовать все фильтры в любых комбинациях
                    - Сотрудник поддержки:
                      * автоматически получает фильтр по assignedTo (только свои тикеты)
                      * может использовать фильтры по статусу, категории и датам
                      * не может фильтровать по createdBy
                    - Пользователь:
                      * автоматически получает фильтр по createdBy (только свои тикеты)
                      * может использовать фильтры по статусу, категории и датам
    
                    При множественной фильтрации применяется логическое И (AND).
                    Сортировка: по дате создания (от новых к старым).
                    
                    Эндпоинт доступен всем авторизованным пользователям с разными правами доступа.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список тикетов успешно получен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TicketResponse.class)),
                            examples = @ExampleObject(
                                    name = "Успешный ответ со списком тикетов",
                                    value = """
                                            [
                                              {
                                                "id": "550e8400-e29b-41d4-a716-446655440001",
                                                "subject": "Не работает авторизация",
                                                "description": "Ошибка при входе в систему",
                                                "status": "OPEN",
                                                "categoryId": "770e8400-e29b-41d4-a716-446655440001",
                                                "createdBy": "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
                                                "assigneeId": null,
                                                "createdAt": "2024-01-15T10:30:00Z",
                                                "updatedAt": "2024-01-15T10:30:00Z"
                                              },
                                              {
                                                "id": "550e8400-e29b-41d4-a716-446655440002",
                                                "subject": "Медленная работа приложения",
                                                "description": "Приложение работает медленно после обновления",
                                                "status": "IN_PROGRESS",
                                                "categoryId": "770e8400-e29b-41d4-a716-446655440002",
                                                "createdBy": "9f2e8e6e-3497-4690-bfc2-c7292e7438f2",
                                                "assigneeId": "a3e8e6e-3497-4690-bfc2-c7292e7438f3",
                                                "createdAt": "2024-01-14T15:20:00Z",
                                                "updatedAt": "2024-01-15T09:15:00Z"
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры фильтрации",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Некорректный формат даты",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Failed to convert 'createdAfter' with value: 'invalid-date'",
                                                      "path": "/tickets"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Некорректный статус",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Invalid status value. Allowed values: OPEN, IN_PROGRESS, RESOLVED, CLOSED",
                                                      "path": "/tickets"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. Недостаточно прав для использования выбранных фильтров.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 403,
                                              "error": "Forbidden",
                                              "message": "Access denied. User does not have permission to filter by 'createdBy'",
                                              "path": "/tickets?createdBy=8f1e8e6e-3497-4690-bfc2-c7292e7438f1"
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
    public List<TicketResponse> getTickets(
            @RequestHeader("X-User-Role") UserRole role,
            @RequestHeader("X-User-Id") UUID userId,

            @Parameter(
                    description = "Фильтр по ID категории. Возвращает тикеты, принадлежащие указанной категории.",
                    example = "770e8400-e29b-41d4-a716-446655440001",
                    required = false
            )
            @RequestParam(required = false) UUID categoryId,

            @Parameter(
                    description = "Фильтр по ID назначенного сотрудника. Возвращает тикеты, назначенные на указанного сотрудника.",
                    example = "a3e8e6e-3497-4690-bfc2-c7292e7438f3",
                    required = false
            )
            @RequestParam(required = false) UUID assignedTo,

            @Parameter(
                    description = "Фильтр по ID автора. Возвращает тикеты, созданные указанным пользователем.",
                    example = "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
                    required = false
            )
            @RequestParam(required = false) UUID createdBy,

            @Parameter(
                    description = "Фильтр по статусу тикета. Возвращает тикеты с указанным статусом.",
                    example = "OPEN",
                    schema = @Schema(allowableValues = {"OPEN", "ASSIGNED", "IN_PROGRESS", "RESOLVED", "CLOSED"}),
                    required = false
            )
            @RequestParam(required = false) TicketStatus status,

            @Parameter(
                    description = "Фильтр по дате создания (после). Возвращает тикеты, созданные после указанной даты и времени.",
                    example = "2024-01-01T00:00:00Z",
                    required = false
            )
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAfter,

            @Parameter(
                    description = "Фильтр по дате создания (до). Возвращает тикеты, созданные до указанной даты и времени.",
                    example = "2024-12-31T23:59:59Z",
                    required = false
            )
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdBefore
    ) {
         return service.getAll(
                role,
                userId,
                categoryId,
                assignedTo,
                createdBy,
                status != null ? status.name() : null,
                createdAfter,
                createdBefore
        );
    }

    @Operation(
            summary = "Получить тикет по ID",
            description = """
                    Возвращает полную информацию о тикете по его уникальному идентификатору.
                    
                    Информация включает:
                    - Основные данные (тема, описание, статус)
                    - Связи (категория, автор, исполнитель)
                    - Временные метки (создание, последнее обновление)
                    
                    Права доступа:
                    - Администратор: доступ к любому тикету
                    - Сотрудник поддержки: доступ к тикетам, назначенным на него или из его категории
                    - Пользователь: доступ только к своим тикетам
                    
                    Эндпоинт доступен всем авторизованным пользователям с соответствующими правами.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Тикет успешно найден",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TicketResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "550e8400-e29b-41d4-a716-446655440001",
                                              "subject": "Не работает авторизация в личном кабинете",
                                              "description": "При попытке войти в личный кабинет после ввода логина и пароля выдается ошибка 500 Internal Server Error",
                                              "status": "IN_PROGRESS",
                                              "categoryId": "770e8400-e29b-41d4-a716-446655440001",
                                              "createdBy": "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
                                              "assigneeId": "a3e8e6e-3497-4690-bfc2-c7292e7438f3",
                                              "createdAt": "2024-01-15T10:30:00Z",
                                              "updatedAt": "2024-01-15T14:20:00Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тикет с указанным ID не найден",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Ticket not found with id: 550e8400-e29b-41d4-a716-446655440999",
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440999"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. У пользователя нет прав на просмотр этого тикета.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 403,
                                              "error": "Forbidden",
                                              "message": "Access denied. You don't have permission to view this ticket",
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440001"
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
    @GetMapping("/{id}")
    public TicketResponse getTicket(
            @Parameter(
                    description = "Уникальный идентификатор тикета",
                    example = "550e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return service.getById(id);
    }



    @Operation(
            summary = "Обновить тикет",
            description = """
                    Обновляет основные данные существующего тикета.
                    
                    Можно изменить:
                    - Тему (subject)
                    - Описание (description)
                    
                    Особенности:
                    - Оба поля обязательны для заполнения в запросе
                    - Нельзя изменить статус, исполнителя, категорию через этот эндпоинт
                    - После обновления автоматически обновляется временная метка updatedAt
                    - Все участники тикета получают уведомление об изменении
                    
                    Права доступа:
                    - Администратор: может обновлять любые тикеты
                    - Сотрудник поддержки: может обновлять тикеты, назначенные на него
                    - Пользователь: может обновлять только свои тикеты
                    
                    Эндпоинт доступен участникам тикета с соответствующими правами.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Тикет успешно обновлен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TicketResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "550e8400-e29b-41d4-a716-446655440001",
                                              "subject": "Не работает авторизация в личном кабинете (уточнено)",
                                              "description": "При попытке войти в личный кабинет после ввода логина и пароля выдается ошибка 500 Internal Server Error. Ошибка возникает только в Chrome, в Firefox работает",
                                              "status": "OPEN",
                                              "categoryId": "770e8400-e29b-41d4-a716-446655440001",
                                              "createdBy": "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
                                              "assigneeId": null,
                                              "createdAt": "2024-01-15T10:30:00Z",
                                              "updatedAt": "2024-01-15T11:45:00Z"
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
                                            name = "Пустая тема",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Ticket subject cannot be empty",
                                                      "path": "/tickets/550e8400-e29b-41d4-a716-446655440001"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Пустое описание",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Ticket description cannot be empty",
                                                      "path": "/tickets/550e8400-e29b-41d4-a716-446655440001"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тикет с указанным ID не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. У пользователя нет прав на обновление этого тикета.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
            )
    })
    @PutMapping("/{id}")
    public TicketResponse updateTicket(
            @RequestHeader("X-User-Role") UserRole role,
            @RequestHeader("X-User-Id") UUID userId,

            @Parameter(
                    description = "Уникальный идентификатор тикета для обновления",
                    example = "550e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления тикета. Оба поля обязательны для заполнения.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateTicketRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "subject": "Не работает авторизация в личном кабинете (уточнено)",
                                              "description": "При попытке войти в личный кабинет после ввода логина и пароля выдается ошибка 500 Internal Server Error. Ошибка возникает только в Chrome, в Firefox работает"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody UpdateTicketRequest request
    ) {
        return service.update(role, userId, id, request);
    }

    @Operation(
            summary = "Удалить тикет",
            description = """
                    Полностью удаляет тикет из системы.
                    
                    Важно:
                    - Удаление необратимо! Все данные тикета и связанные комментарии будут удалены
                    - Рекомендуется использовать этот эндпоинт только для ошибочных или тестовых тикетов
                    - Для закрытия решенных тикетов используйте изменение статуса на CLOSED
                    
                    Права доступа:
                    - Администратор: может удалять любые тикеты
                    - Сотрудник поддержки: может удалять только тикеты своей категории в статусе OPEN
                    - Пользователь: может удалять только свои тикеты в статусе OPEN
                    
                    Эндпоинт доступен с соответствующими правами доступа.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Тикет успешно удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тикет с указанным ID не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. У пользователя нет прав на удаление этого тикета.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Невозможно удалить тикет (например, тикет уже обрабатывается)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 409,
                                              "error": "Conflict",
                                              "message": "Cannot delete ticket with status IN_PROGRESS",
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440001"
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(
            @RequestHeader("X-User-Role") UserRole role,
            @RequestHeader("X-User-Id") UUID userId,

            @Parameter(
                    description = "Уникальный идентификатор тикета для удаления",
                    example = "550e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id
    ) {
        service.delete(role, userId, id);
    }

    @Operation(
            summary = "Обновить статус тикета",
            description = """
                    Изменяет статус тикета в процессе его жизненного цикла.
                    
                    Доступные статусы и переходы:
                    - OPEN -> ASSIGNED (начало обработки)
                    - ASSIGNED -> IN_PROGRESS (начало обработки)
                    - IN_PROGRESS -> RESOLVED (решение найдено)
                    - RESOLVED -> CLOSED (подтверждение решения)
                    - OPEN -> CLOSED (закрытие без обработки по решению администратора)
                    - ASSIGNED -> CLOSED (прекращение работы по решению администратора)
                    - IN_PROGRESS -> ASSIGNED (возврат в работу, если решение не найдено)
                    - RESOLVED -> ASSIGNED (решение не подошло, требуется доработка)
                    
                    При изменении статуса:
                    - Автоматически обновляется временная метка updatedAt
                    - Все участники тикета получают уведомление об изменении
                    - При переходе в статус CLOSED тикет становится доступен только для чтения
                    
                    Права доступа:
                    - Администратор: может менять статус любого тикета
                    - Сотрудник поддержки: может менять статус назначенных тикетов
                    - Пользователь: не может менять статус
                    
                    Эндпоинт доступен участникам тикета с соответствующими правами.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статус тикета успешно обновлен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TicketResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "550e8400-e29b-41d4-a716-446655440001",
                                              "subject": "Не работает авторизация",
                                              "description": "Ошибка при входе в систему",
                                              "status": "IN_PROGRESS",
                                              "categoryId": "770e8400-e29b-41d4-a716-446655440001",
                                              "createdBy": "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
                                              "assigneeId": "a3e8e6e-3497-4690-bfc2-c7292e7438f3",
                                              "createdAt": "2024-01-15T10:30:00Z",
                                              "updatedAt": "2024-01-15T14:20:00Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный статус или недопустимый переход",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Недопустимый переход статуса",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Invalid status transition from OPEN to RESOLVED",
                                                      "path": "/tickets/550e8400-e29b-41d4-a716-446655440001/status"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Некорректный статус",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Invalid status value. Allowed values: OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED",
                                                      "path": "/tickets/550e8400-e29b-41d4-a716-446655440001/status"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тикет с указанным ID не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. У пользователя нет прав на изменение статуса этого тикета.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
            )
    })
    @PutMapping("/{id}/status")
    public TicketResponse updateStatus(
            @Parameter(
                    description = "Уникальный идентификатор тикета",
                    example = "550e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новый статус тикета. Должен соответствовать допустимым переходам из текущего статуса.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateStatusRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "IN_PROGRESS"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody UpdateStatusRequest request
    ) {
        return service.updateStatus(id, request);
    }

    @Operation(
            summary = "Назначить тикет сотруднику",
            description = """
                    Назначает ответственного сотрудника на тикет.
                    
                    Назначение определяет, кто из сотрудников поддержки будет заниматься решением тикета.
                    
                    Правила назначения:
                    - На тикет может быть назначен только один сотрудник
                    - Повторное назначение заменяет предыдущего исполнителя
                    
                    После назначения:
                    - Назначенный сотрудник получает уведомление
                    - Статус тикета рекомендуется изменить на IN_PROGRESS
                    
                    Права доступа:
                    - Администратор: может назначать любых сотрудников на любые доступные им тикеты
                    - Сотрудник поддержки: может назначать только себя на свободные тикеты категории
                    - Пользователь: не может назначать исполнителей
                    
                    Эндпоинт доступен администраторам и сотрудникам поддержки.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Тикет успешно назначен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TicketResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "550e8400-e29b-41d4-a716-446655440001",
                                              "subject": "Не работает авторизация",
                                              "description": "Ошибка при входе в систему",
                                              "status": "OPEN",
                                              "categoryId": "770e8400-e29b-41d4-a716-446655440001",
                                              "createdBy": "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
                                              "assigneeId": "a3e8e6e-3497-4690-bfc2-c7292e7438f3",
                                              "createdAt": "2024-01-15T10:30:00Z",
                                              "updatedAt": "2024-01-15T14:20:00Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный ID сотрудника",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "User not found with id: a3e8e6e-3497-4690-bfc2-c7292e7438f9",
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440001/assignee"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тикет с указанным ID не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. У пользователя нет прав на назначение исполнителя.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
            )
    })
    @PutMapping("/{id}/assignee")
    public TicketResponse assignTicket(
            @Parameter(
                    description = "Уникальный идентификатор тикета",
                    example = "550e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для назначения тикета. Поле assigneeId обязательно.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssignTicketRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Назначить сотрудника",
                                            value = """
                                                    {
                                                      "assigneeId": "a3e8e6e-3497-4690-bfc2-c7292e7438f3"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @RequestBody AssignTicketRequest request
    ) {
        return service.assign(id, request);
    }

}