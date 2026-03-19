package development.team.ticketsystem.ticketservice.Controllers;

import development.team.ticketsystem.ticketservice.DTO.tickets.AssignTicketRequest;
import development.team.ticketsystem.ticketservice.DTO.tickets.CreateTicketRequest;
import development.team.ticketsystem.ticketservice.DTO.tickets.TicketResponse;
import development.team.ticketsystem.ticketservice.DTO.tickets.UpdateStatusRequest;
import development.team.ticketsystem.ticketservice.DTO.tickets.UpdateTicketRequest;
import development.team.ticketsystem.ticketservice.Entity.TicketEntity;
import development.team.ticketsystem.ticketservice.Service.TicketService;
import development.team.ticketsystem.ticketservice.TicketStatus;
import development.team.ticketsystem.ticketservice.DTO.tickets.*;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public TicketResponse createTicket(@RequestBody CreateTicketRequest request) {
        TicketEntity entity = new TicketEntity();
        entity.setSubject(request.getSubject());
        entity.setDescription(request.getDescription());
        entity.setCategoryId(request.getCategoryId());
        entity.setStatus(TicketStatus.valueOf(TicketStatus.OPEN.name()));

        TicketEntity saved = service.create(entity);

        return toResponse(saved);
    }

    // Этот метод доступен всем, но с разными фильтрами
    @GetMapping
    public List<TicketResponse> getTickets(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID assignedTo,
            @RequestParam(required = false) UUID createdBy,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) Instant createdAfter,
            @RequestParam(required = false) Instant createdBefore
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

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable UUID id) {
        return toResponse(service.getById(id));
    }

    @PutMapping("/{id}")
    public TicketResponse updateTicket(
            @PathVariable UUID id,
            @RequestBody UpdateTicketRequest request
    ) {
        TicketEntity updated = new TicketEntity();
        updated.setSubject(request.getSubject());
        updated.setDescription(request.getDescription());

        return toResponse(service.update(id, updated));
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable UUID id) {
        service.delete(id);
    }

    @PutMapping("/{id}/status")
    public TicketResponse updateStatus(
            @PathVariable UUID id,
            @RequestBody UpdateStatusRequest request
    ) {
        return toResponse(service.updateStatus(id, request.getStatus().name()));
    }

    @PutMapping("/{id}/assignee")
    public TicketResponse assignTicket(
            @PathVariable UUID id,
            @RequestBody AssignTicketRequest request
    ) {
        return toResponse(service.assign(id, request.getAssigneeId()));
    }


    // mapper
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