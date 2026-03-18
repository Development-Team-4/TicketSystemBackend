package development.team.ticketsystem.ticket_service.Controllers;

import development.team.ticketsystem.ticket_service.TicketStatus;
import development.team.ticketsystem.ticket_service.DTO.tickets.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @PostMapping
    public TicketResponse createTicket(@RequestBody CreateTicketRequest request) {
        return new TicketResponse();
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

        return List.of();
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable UUID id) {
        return new TicketResponse();
    }

    @PutMapping("/{id}")
    public TicketResponse updateTicket(
            @PathVariable UUID id,
            @RequestBody UpdateTicketRequest request
    ) {
        return new TicketResponse();
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable UUID id) {

    }

    @PutMapping("/{id}/status")
    public TicketResponse updateStatus(
            @PathVariable UUID id,
            @RequestBody UpdateStatusRequest request
    ) {
        return new TicketResponse();
    }

    @PutMapping("/{id}/assignee")
    public TicketResponse assignTicket(
            @PathVariable UUID id,
            @RequestBody AssignTicketRequest request
    ) {
        return new TicketResponse();
    }

}