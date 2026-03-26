package development.team.ticketsystem.ticketservice.Controllers;


import development.team.ticketsystem.ticketservice.DTO.categories.AssignStaffRequest;
import development.team.ticketsystem.ticketservice.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.DTO.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.Service.CategoryService;
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
@Tag(name = "Categories",
        description = """
Управление категориями в системе поддержки.
 Категории группируют тикеты по тематикам внутри определенной темы.
 """)
public class CategoryController {

    private final CategoryService service;

    @Operation(
            summary = "Получить все категории темы",
            description = """
                    Возвращает список всех категорий, принадлежащих указанной теме.
                    
                    Категории используются для классификации тикетов внутри темы. Например, для темы "Техническая поддержка"
                    категориями могут быть "Проблемы с авторизацией", "Сбои в работе сервиса", "Вопросы по API".
                    
                    Эндпоинт доступен всем авторизованным пользователям.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список категорий успешно получен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)),
                            examples = @ExampleObject(
                                    name = "Успешный ответ",
                                    value = """
                                            [
                                              {
                                                "id": "550e8400-e29b-41d4-a716-446655440000",
                                                "topicId": "660e8400-e29b-41d4-a716-446655440001",
                                                "name": "Проблемы с авторизацией",
                                                "description": "Категория для тикетов, связанных с входом в систему"
                                              },
                                              {
                                                "id": "550e8400-e29b-41d4-a716-446655440001",
                                                "topicId": "660e8400-e29b-41d4-a716-446655440001",
                                                "name": "Сбои в работе сервиса",
                                                "description": "Категория для тикетов о недоступности или сбоях сервиса"
                                              }
                                            ]
                                            """
                            )
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
                                              "message": "Topic not found with id: 550e8400-e29b-41d4-a716-446655440000",
                                              "path": "/topics/550e8400-e29b-41d4-a716-446655440000/categories"
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
                                              "path": "/topics/550e8400-e29b-41d4-a716-446655440000/categories"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. У пользователя недостаточно прав для просмотра категорий этой темы.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
            )
    })
    @GetMapping("/topics/{topicId}/categories")
    public List<CategoryResponse> getCategories(
            @Parameter(
                    description = "Уникальный идентификатор темы, для которой запрашиваются категории",
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    required = true
            )
            @PathVariable UUID topicId
    ) {
        return service.getByTopic(topicId);
    }


    @Operation(
            summary = "Получить категорию по ID",
            description = """
                    Возвращает детальную информацию о конкретной категории.
                    Категория возвращается без указания темы, так как идентификатор категории уникален в системе.
                    
                    Позволяет получить полные данные категории, включая идентификатор темы, к которой она относится,
                    название и описание.
                    
                    Эндпоинт доступен всем авторизованным пользователям.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Категория успешно найдена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class),
                            examples = @ExampleObject(
                                    name = "Успешный ответ",
                                    value = """
                                            {
                                              "id": "550e8400-e29b-41d4-a716-446655440000",
                                              "topicId": "660e8400-e29b-41d4-a716-446655440001",
                                              "name": "Проблемы с авторизацией",
                                              "description": "Категория для тикетов, связанных с входом в систему"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория с указанным ID не найдена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Category not found with id: 550e8400-e29b-41d4-a716-446655440000",
                                              "path": "/categories/550e8400-e29b-41d4-a716-446655440000"
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
                    description = "Доступ запрещен. У пользователя недостаточно прав для просмотра этой категории.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
            )
    })
    @GetMapping("/categories/{id}")
    public CategoryResponse getCategory(
            @Parameter(
                    description = "Уникальный идентификатор категории, которую необходимо получить",
                    example = "550e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return service.getById(id);
    }



    @Operation(
            summary = "Создать категорию",
            description = """
                    Создает новую категорию в указанной теме.
                    
                    Категория служит для группировки тикетов по определенным направлениям внутри темы.
                    После создания категории, пользователи смогут выбирать её при создании тикетов.
                    
                    Требования к данным:
                    - Название обязательно для заполнения
                    - Описание обязательно для заполнения
                    
                    Эндпоинт доступен только администраторам и менеджерам.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Категория успешно создана",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class),
                            examples = @ExampleObject(
                                    name = "Успешный ответ",
                                    value = """
                                            {
                                              "id": "550e8400-e29b-41d4-a716-446655440002",
                                              "topicId": "660e8400-e29b-41d4-a716-446655440001",
                                              "name": "Вопросы по API",
                                              "description": "Категория для вопросов, связанных с использованием API"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса (например, пустое название или превышение максимальной длины)",
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
                                                      "message": "Category name cannot be empty",
                                                      "path": "/topics/660e8400-e29b-41d4-a716-446655440001/categories"
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
                                                      "message": "Category with name 'Проблемы с авторизацией' already exists in this topic",
                                                      "path": "/topics/660e8400-e29b-41d4-a716-446655440001/categories"
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
                                              "message": "Topic not found with id: 550e8400-e29b-41d4-a716-446655440000",
                                              "path": "/topics/550e8400-e29b-41d4-a716-446655440000/categories"
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
                    description = "Доступ запрещен. Только администраторы могут создавать категории.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 403,
                                              "error": "Forbidden",
                                              "message": "Access denied. Only ADMIN role can create categories",
                                              "path": "/topics/660e8400-e29b-41d4-a716-446655440001/categories"
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
    @PostMapping("/topics/{topicId}/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(
            @RequestHeader("X-User-Role") UserRole role,

            @Parameter(
                    description = "Уникальный идентификатор темы, в которой будет создана категория",
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    required = true
            )
            @PathVariable UUID topicId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания категории. Поля 'name' и 'description' обязательны.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateCategoryRequest.class),
                            examples = @ExampleObject(
                                    name = "Пример запроса",
                                    value = """
                                            {
                                              "name": "Вопросы по API",
                                              "description": "Категория для вопросов, связанных с использованием API"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody CreateCategoryRequest request
    ) {
        return service.create(role, topicId, request);
    }

    @Operation(
            summary = "Обновить категорию",
            description = """
                    Обновляет данные существующей категории.
                    Категория обновляется без указания темы, так как идентификатор категории уникален в системе.
                    
                    Можно изменить название и описание категории.
                    
                    Примечания:
                    - Если передать пустое название, вернется ошибка 400
                    - Изменить принадлежность категории к теме нельзя
                    
                    Эндпоинт доступен только администраторам.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Категория успешно обновлена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class),
                            examples = @ExampleObject(
                                    name = "Успешный ответ",
                                    value = """
                                            {
                                              "id": "550e8400-e29b-41d4-a716-446655440000",
                                              "topicId": "660e8400-e29b-41d4-a716-446655440001",
                                              "name": "Проблемы с авторизацией (обновлено)",
                                              "description": "Категория для тикетов, связанных с входом в систему"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса (например, пустое название)",
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
                                                      "message": "Category name cannot be empty",
                                                      "path": "/topics/660e8400-e29b-41d4-a716-446655440001/categories/550e8400-e29b-41d4-a716-446655440000"
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
                                                      "message": "Category with name 'Вопросы по API' already exists in this topic",
                                                      "path": "/topics/660e8400-e29b-41d4-a716-446655440001/categories/550e8400-e29b-41d4-a716-446655440000"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория с указанным ID не найдена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Category not found with id: 550e8400-e29b-41d4-a716-446655440999",
                                              "path": "/categories/550e8400-e29b-41d4-a716-446655440999"
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
                    description = "Доступ запрещен. Только администраторы и менеджеры могут обновлять категории.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-01-15T10:30:00Z",
                                              "status": 403,
                                              "error": "Forbidden",
                                              "message": "Access denied. Only ADMIN role can update categories",
                                              "path": "/categories/550e8400-e29b-41d4-a716-446655440000"
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
    @PatchMapping("/categories/{id}")
    public CategoryResponse updateCategory(
            @RequestHeader("X-User-Role") UserRole role,

            @Parameter(
                    description = "Уникальный идентификатор категории, которую необходимо обновить",
                    example = "550e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления категории. Оба поля опциональны, но хотя бы одно должно быть заполнено.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateCategoryRequest.class),
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "name": "Проблемы с авторизацией (обновлено)",
                                                      "description": "Обновленное описание категории"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @RequestBody CreateCategoryRequest request
    ) {
        return service.update(role, id, request);
    }

    @Operation(
            summary = "Назначить сотрудника на категорию",
            description = """
                Назначает сотрудника поддержки на указанную категорию тикетов.
                
                Назначение определяет, какие сотрудники могут работать с тикетами данной категории.
                
                Правила:
                - Один сотрудник может быть назначен на несколько категорий
                - Одна категория может иметь несколько сотрудников
                
                После назначения:
                - Сотрудник получает доступ к тикетам данной категории
                - Может брать тикеты в работу
                
                Права доступа:
                - Администратор: может назначать сотрудников на любые категории
                - Сотрудник поддержки: не может управлять категориями
                - Пользователь: не имеет доступа
                
                Эндпоинт доступен только администраторам.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Сотрудник успешно назначен на категорию"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Категория не найдена",
                                            value = """
                                                {
                                                  "timestamp": "2024-01-15T10:30:00Z",
                                                  "status": 400,
                                                  "error": "Bad Request",
                                                  "message": "Category not found with id: 770e8400-e29b-41d4-a716-446655440999",
                                                  "path": "/categories/770e8400-e29b-41d4-a716-446655440001/assign-staff"
                                                }
                                                """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен. Только администратор может назначать сотрудников.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "timestamp": "2024-01-15T10:30:00Z",
                                          "status": 403,
                                          "error": "Forbidden",
                                          "message": "Only ADMIN can assign staff",
                                          "path": "/categories/770e8400-e29b-41d4-a716-446655440001/assign-staff"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("categories/{id}/staff")
    public void assignStaffToCategory(
            @RequestHeader("X-User-Role") UserRole role,

            @Parameter(
                    description = "Уникальный идентификатор категории",
                    example = "770e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID сотрудника, которого необходимо назначить на категорию",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssignStaffRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "staffId": "a3e8e6e-3497-4690-bfc2-c7292e7438f3"
                                        }
                                        """
                            )
                    )
            )
            @RequestBody AssignStaffRequest request
    ) {
        service.assignStaffToCategory(role, id, request);
    }


    @Operation(
            summary = "Удалить сотрудника из категории",
            description = """
                Удаляет сотрудника поддержки из категории.
                
                После удаления:
                - Сотрудник теряет доступ к тикетам данной категории
                - Не может брать новые тикеты этой категории
                
                Важно:
                - Уже назначенные тикеты остаются у сотрудника
                - Для полного отвязывания администратору необходимо переназначить тикеты вручную
                
                Права доступа:
                - Администратор: может удалять сотрудников из категорий
                - Остальные роли: не имеют доступа
                
                Эндпоинт доступен только администраторам.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Сотрудник успешно удален из категории"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Связь сотрудника с категорией не найдена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "timestamp": "2024-01-15T10:30:00Z",
                                          "status": 404,
                                          "error": "Not Found",
                                          "message": "Staff is not assigned to this category",
                                          "path": "/categories/770e8400-e29b-41d4-a716-446655440001/staff/a3e8e6e-3497-4690-bfc2-c7292e7438f3"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("categories/{categoryId}/staff/{staffId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeStaff(
            @RequestHeader("X-User-Role") UserRole role,

            @Parameter(
                    description = "ID категории",
                    example = "770e8400-e29b-41d4-a716-446655440001",
                    required = true
            )
            @PathVariable UUID categoryId,

            @Parameter(
                    description = "ID сотрудника",
                    example = "a3e8e6e-3497-4690-bfc2-c7292e7438f3",
                    required = true
            )
            @PathVariable UUID staffId
    ) {
        service.removeStaff(role, categoryId, staffId);
    }

}
