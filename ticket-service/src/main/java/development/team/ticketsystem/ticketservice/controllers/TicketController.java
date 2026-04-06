package development.team.ticketsystem.ticketservice.controllers;

import development.team.ticketsystem.ticketservice.TicketStatus;
import development.team.ticketsystem.ticketservice.UserRole;
import development.team.ticketsystem.ticketservice.dto.error.ErrorResponse;
import development.team.ticketsystem.ticketservice.dto.filter.TicketFilterRequest;
import development.team.ticketsystem.ticketservice.dto.tickets.*;
import development.team.ticketsystem.ticketservice.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearerAuth")
public class TicketController {

    private final TicketService ticketService;


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
                                            name = "Пустая тема"
                                    ),
                                    @ExampleObject(
                                            name = "Пустое описание"
                                    ),
                                    @ExampleObject(
                                            name = "Категория не найдена"
                                    )
                            }
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") UUID userId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания тикета. Все поля обязательны для заполнения.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateTicketRequest.class),
                            examples = @ExampleObject(
                                    name = "Пример тела запроса"
                            )
                    )
            )
            @RequestBody CreateTicketRequest request
    ) {
        return ticketService.create(userId, request);
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
                                    name = "Успешный ответ со списком тикетов"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры фильтрации",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Некорректный формат даты"
                                    )
                            }
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
                    description = "Доступ запрещен. Недостаточно прав для использования выбранных фильтров.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Недостаточно прав для использования указанных фильтров"
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
    @GetMapping
    public List<TicketResponse> getTickets(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Role") UserRole role,
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") UUID userId,

            @ModelAttribute TicketFilterRequest filterRequest
    ) {
         return ticketService.getAll(
                role,
                userId,
                filterRequest
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
                                    name = "Успешный ответ"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тикет с указанным ID не найден",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Тикет не найден"
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
                    description = "Доступ запрещен. У пользователя нет прав на просмотр этого тикета.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Недостаточно прав для просмотра"                            )
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
    @GetMapping("/{id}")
    public TicketResponse getTicket(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") UUID userId,

            @Parameter(hidden = true)
            @RequestHeader("X-User-Role") UserRole userRole,


            @Parameter(
                    description = "Уникальный идентификатор тикета",
                    example = "550e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return ticketService.getById(userRole, userId, id);
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
                                            name = "Пустая тема"
                                    ),
                                    @ExampleObject(
                                            name = "Пустое описание"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тикет с указанным ID не найден",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
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
                    description = "Доступ запрещен. У пользователя нет прав на обновление этого тикета.",
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
    @PutMapping("/{id}")
    public TicketResponse updateTicket(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Role") UserRole role,
            @Parameter(hidden = true)
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
                                    name = "Пример тела запроса"
                            )
                    )
            )
            @RequestBody UpdateTicketRequest request
    ) {
        return ticketService.update(role, userId, id, request);
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
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
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
                    description = "Доступ запрещен. У пользователя нет прав на удаление этого тикета.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Невозможно удалить тикет (например, тикет уже обрабатывается)",
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Role") UserRole role,
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") UUID userId,

            @Parameter(
                    description = "Уникальный идентификатор тикета для удаления",
                    example = "550e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id
    ) {
        ticketService.delete(role, userId, id);
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
                                    name = "Успешный ответ"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный статус или недопустимый переход",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Недопустимый переход статуса"
                                    ),
                                    @ExampleObject(
                                            name = "Некорректный статус"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тикет с указанным ID не найден",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
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
                    description = "Доступ запрещен. У пользователя нет прав на изменение статуса этого тикета.",
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
                                    name = "Пример тела запроса"
                            )
                    )
            )
            @RequestBody UpdateStatusRequest request
    ) {
        return ticketService.updateStatus(id, request);
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
                                    name = "Успешный ответ"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный ID сотрудника",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Сотрудник не найден"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тикет с указанным ID не найден",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
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
                    description = "Доступ запрещен. У пользователя нет прав на назначение исполнителя.",
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
                                            name = "Назначить сотрудника"
                                    )
                            }
                    )
            )
            @RequestBody AssignTicketRequest request
    ) {
        return ticketService.assign(id, request);
    }

}