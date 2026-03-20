package development.team.ticketsystem.ticketservice.Controllers;

import development.team.ticketsystem.ticketservice.DTO.tickets.*;
import development.team.ticketsystem.ticketservice.Entity.TicketEntity;
import development.team.ticketsystem.ticketservice.Service.TicketService;
import development.team.ticketsystem.ticketservice.TicketStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jspecify.annotations.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Tickets", description = "Управление тикетами")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @Operation(summary = "Создать тикет")
    @PostMapping
    public TicketResponse createTicket(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания тикета")
            @RequestBody CreateTicketRequest request
    ) {
        TicketEntity entity = new TicketEntity();
        entity.setSubject(request.getSubject());
        entity.setDescription(request.getDescription());
        entity.setCategoryId(request.getCategoryId());
        entity.setStatus(TicketStatus.valueOf(TicketStatus.OPEN.name()));

        TicketEntity saved = service.create(entity);

        return toResponse(saved);
    }

    // Этот метод доступен всем, но с разными фильтрами
    @Operation(summary = "Получить список тикетов с фильтрацией")
    @GetMapping
    public List<TicketResponse> getTickets(
            @Parameter(description = "ID категории для фильтрации", example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam(required = false) UUID categoryId,

            @Parameter(description = "ID назначенного сотрудника для фильтрации", example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam(required = false) UUID assignedTo,

            @Parameter(description = "ID создателя для фильтрации", example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam(required = false) UUID createdBy,

            @Parameter(description = "Статус тикета для фильтрации", example = "OPEN")
            @RequestParam(required = false) TicketStatus status,

            @Parameter(description = "Фильтр по дате создания (после)", example = "2024-01-01T00:00:00Z")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAfter,

            @Parameter(description = "Фильтр по дате создания (до)", example = "2024-12-31T23:59:59Z")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdBefore
    ) {
        // внутри будет проверка на роль того, от кого пришел запрос из JWT
        // все фильтры одновременно доступны только админу
        // createdBy - пользователю, assignedTo - сотруднику НО только свои
        // фильтры по времени, категории и статусу доступны всем, но с учетом роли

        return service.getAll(
                        categoryId,
                        assignedTo,
                        createdBy,
                        status != null ? status.name() : null,
                        createdAfter,
                        createdBefore
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Operation(summary = "Получить тикет по ID")
    @GetMapping("/{id}")
    public TicketResponse getTicket(
            @Parameter(description = "ID тикета", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id
    ) {
        return toResponse(service.getById(id));
    }

    @Operation(summary = "Обновить тикет")
    @PutMapping("/{id}")
    public TicketResponse updateTicket(
            @Parameter(description = "ID тикета", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для обновления тикета")
            @RequestBody UpdateTicketRequest request
    ) {
        TicketEntity updated = new TicketEntity();
        updated.setSubject(request.getSubject());
        updated.setDescription(request.getDescription());

        return toResponse(service.update(id, updated));
    }

    @Operation(summary = "Удалить тикет")
    @DeleteMapping("/{id}")
    public void deleteTicket(
            @Parameter(description = "ID тикета", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id
    ) {
        service.delete(id);
    }

    @Operation(summary = "Обновить статус тикета")
    @PutMapping("/{id}/status")
    public TicketResponse updateStatus(
            @Parameter(description = "ID тикета", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Новый статус тикета")
            @RequestBody UpdateStatusRequest request
    ) {
        return toResponse(service.updateStatus(id, request.getStatus().name()));
    }

    @Operation(summary = "Назначить тикет сотруднику")
    @PutMapping("/{id}/assignee")
    public TicketResponse assignTicket(
            @Parameter(description = "ID тикета", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для назначения тикета")
            @RequestBody AssignTicketRequest request
    ) {
        return toResponse(service.assign(id, request.getAssigneeId()));
    }


    // mapper - ИСПОЛЬЗОВАТЬ БИБЛИОТЕКУ mapstruct
    private TicketResponse toResponse(@NonNull TicketEntity entity) {
        TicketResponse response = new TicketResponse();

        response.setId(entity.getId());
        response.setSubject(entity.getSubject());
        response.setDescription(entity.getDescription());
        response.setStatus(TicketStatus.valueOf(String.valueOf(entity.getStatus())));

        response.setCategoryId(entity.getCategoryId());
        response.setCreatedBy(entity.getCreatedBy());
        response.setAssigneeId(entity.getAssigneeId());

        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        return response;
    }
}