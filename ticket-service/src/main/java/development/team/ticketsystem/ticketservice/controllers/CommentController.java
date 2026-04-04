package development.team.ticketsystem.ticketservice.controllers;

import development.team.ticketsystem.ticketservice.dto.comments.CommentResponse;
import development.team.ticketsystem.ticketservice.dto.comments.CreateCommentRequest;
import development.team.ticketsystem.ticketservice.dto.error.ErrorResponse;
import development.team.ticketsystem.ticketservice.service.CommentService;
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
@SecurityRequirement(name = "bearerAuth")
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
                                    name = "Успешный ответ с комментариями"
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
                                    name = "Нет указанного тикета"
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
                    description = "Доступ запрещен. Пользователь не является участником тикета (не автор, не назначенный сотрудник, не администратор).",
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
                                            name = "Пустой комментарий"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тикет с указанным ID не найден",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Нет указанного тикета"
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
                    description = "Доступ запрещен. Пользователь не является участником тикета (не автор, не назначенный сотрудник, не администратор).",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Доступ запрещен"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Конфликт при добавлении комментария (например, тикет уже закрыт)",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Конфликт с бизнес-правилами"
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
    @PostMapping("/tickets/{ticketId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(
            @Parameter(hidden = true)
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
                                            name = "Простой комментарий"
                                    ),
                                    @ExampleObject(
                                            name = "Комментарий с уточнением"
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