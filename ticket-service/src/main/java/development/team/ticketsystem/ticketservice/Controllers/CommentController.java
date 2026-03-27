package development.team.ticketsystem.ticketservice.Controllers;

import development.team.ticketsystem.ticketservice.DTO.comments.CommentResponse;
import development.team.ticketsystem.ticketservice.DTO.comments.CreateCommentRequest;
import development.team.ticketsystem.ticketservice.Service.CommentService;
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
@Tag(name = "Comments", description = """
Управление комментариями к тикетам в системе поддержки.
 Комментарии позволяют вести обсуждение тикета, добавлять уточнения, прикладывать дополнительную информацию
 и отслеживать ход решения проблемы.
 """)
public class CommentController {

    private final CommentService service;


    @Operation(
            summary = "Получить все комментарии тикета",
            description = """
                    Возвращает список всех комментариев, оставленных к указанному тикету.
                    
                    Комментарии отображаются в хронологическом порядке (от старых к новым) и содержат:
                    - текст комментария
                    - информацию об авторе (ID пользователя)
                    - дату и время создания
                    
                    Комментарии используются для:
                    - Уточнения деталей проблемы
                    - Предоставления дополнительной информации
                    - Обсуждения хода решения между сотрудниками поддержки и пользователем
                    - Фиксации промежуточных этапов решения
                    
                    Эндпоинт доступен всем участникам тикета (автору, назначенному сотруднику, администраторам).
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список комментариев успешно получен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CommentResponse.class)),
                            examples = @ExampleObject(
                                    name = "Успешный ответ с комментариями",
                                    value = """
                                            [
                                              {
                                                "id": "770e8400-e29b-41d4-a716-446655440001",
                                                "ticketId": "550e8400-e29b-41d4-a716-446655440000",
                                                "authorId": "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
                                                "content": "Не могу войти в систему, пишет 'Неверный пароль', хотя пароль точно правильный",
                                                "createdAt": "2024-01-15T09:15:00Z"
                                              },
                                              {
                                                "id": "770e8400-e29b-41d4-a716-446655440002",
                                                "ticketId": "550e8400-e29b-41d4-a716-446655440000",
                                                "authorId": "9f2e8e6e-3497-4690-bfc2-c7292e7438f2",
                                                "content": "Попробуйте сбросить пароль через форму восстановления",
                                                "createdAt": "2024-01-15T10:30:00Z"
                                              },
                                              {
                                                "id": "770e8400-e29b-41d4-a716-446655440003",
                                                "ticketId": "550e8400-e29b-41d4-a716-446655440000",
                                                "authorId": "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
                                                "content": "Спасибо, помогло! Пароль сбросил, теперь всё работает",
                                                "createdAt": "2024-01-15T11:45:00Z"
                                              }
                                            ]
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
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440999/comments"
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
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440000/comments"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. Пользователь не является участником тикета (не автор, не назначенный сотрудник, не администратор).",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 403,
                                              "error": "Forbidden",
                                              "message": "Access denied. You don't have permission to view comments for this ticket",
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440000/comments"
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
    @GetMapping("/tickets/{ticketId}/comments")
    public List<CommentResponse> getComments(
            @Parameter(
                    description = "Уникальный идентификатор тикета, комментарии которого запрашиваются",
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    required = true
            )
            @PathVariable UUID ticketId
    ) {
        return service.getByTicket(ticketId);
    }

    @Operation(
            summary = "Добавить комментарий к тикету",
            description = """
                    Добавляет новый комментарий к указанному тикету.
                    
                    Комментарий может содержать:
                    - Текстовое описание (обязательно)
                    - Дополнительную информацию о ходе решения
                    - Уточняющие вопросы от сотрудников поддержки
                    - Ответы пользователя на вопросы
                    - Подтверждение решения проблемы
                    
                    Особенности:
                    - Комментарий автоматически привязывается к текущему авторизованному пользователю (автор берется из JWT токена)
                    - Временная метка создания устанавливается автоматически на момент добавления
                    - После добавления комментария, все участники тикета получают уведомление (если настроено)
                    - Комментарии нельзя редактировать после создания
                    
                    Эндпоинт доступен всем участникам тикета (автору, назначенному сотруднику, администраторам).
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Комментарий успешно добавлен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentResponse.class),
                            examples = @ExampleObject(
                                    name = "Успешный ответ",
                                    value = """
                                            {
                                              "id": "770e8400-e29b-41d4-a716-446655440004",
                                              "ticketId": "550e8400-e29b-41d4-a716-446655440000",
                                              "authorId": "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
                                              "content": "Проблема решена, тикет можно закрывать",
                                              "createdAt": "2024-01-15T12:00:00Z"
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
                                            name = "Пустой комментарий",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Comment content cannot be empty",
                                                      "path": "/tickets/550e8400-e29b-41d4-a716-446655440000/comments"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Превышение максимальной длины",
                                            value = """
                                                    {
                                                      "timestamp": "2024-01-15T10:30:00Z",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Comment content exceeds maximum length of 2000 characters",
                                                      "path": "/tickets/550e8400-e29b-41d4-a716-446655440000/comments"
                                                    }
                                                    """
                                    )
                            }
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
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440999/comments"
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
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440000/comments"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. Пользователь не является участником тикета (не автор, не назначенный сотрудник, не администратор).",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 403,
                                              "error": "Forbidden",
                                              "message": "Access denied. Only ticket participants can add comments",
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440000/comments"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Конфликт при добавлении комментария (например, тикет уже закрыт)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 409,
                                              "error": "Conflict",
                                              "message": "Cannot add comment to closed ticket",
                                              "path": "/tickets/550e8400-e29b-41d4-a716-446655440000/comments"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/tickets/{ticketId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(
            @RequestHeader("X-User-Id") UUID authorId,

            @Parameter(
                    description = "Уникальный идентификатор тикета, к которому добавляется комментарий",
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    required = true
            )
            @PathVariable UUID ticketId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания комментария. Поле 'content' обязательно для заполнения.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateCommentRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Простой комментарий",
                                            value = """
                                                    {
                                                      "content": "Проблема воспроизводится, начинаю разбираться"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Комментарий с уточнением",
                                            value = """
                                                    {
                                                      "content": "Уточните, пожалуйста, в каком браузере возникает ошибка? Пробовали очистить кэш?"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Комментарий с подтверждением решения",
                                            value = """
                                                    {
                                                      "content": "Проблема решена, тикет можно закрывать. Спасибо за помощь!"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @RequestBody CreateCommentRequest request
    ) {
        return service.create(
                ticketId,
                authorId,
                request
        );
    }

}