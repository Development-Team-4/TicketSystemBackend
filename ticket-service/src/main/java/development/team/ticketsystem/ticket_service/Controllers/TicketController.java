package development.team.ticketsystem.ticket_service.Controllers;

import development.team.ticketsystem.ticket_service.DTO.TicketStatus;
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

    // Доступно пользователю, userId придет от gateway
    @GetMapping("/my")
    public List<TicketResponse> getMyTickets(
            @RequestHeader("X-User-Id") UUID userId
    ) {
        return List.of();
    }


    // Доступно сотруднику, userId придет от gateway
    @GetMapping("/assigned")
    public List<TicketResponse> getAssignedTickets(
            @RequestHeader("X-User-Id") UUID userId
    ) {
        return List.of();
    }

    // Это будет доступно только админу
    @GetMapping
    public List<TicketResponse> getAllTickets(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) Instant createdAfter,
            @RequestParam(required = false) Instant createdBefore
    ) {
        return List.of();
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable UUID id) {
        return new TicketResponse();
    }

    @PatchMapping("/{id}")
    public TicketResponse updateTicket(
            @PathVariable UUID id,
            @RequestBody UpdateTicketRequest request
    ) {
        return new TicketResponse();
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable UUID id) {

    }

    @PatchMapping("/{id}/status")
    public TicketResponse updateStatus(
            @PathVariable UUID id,
            @RequestBody UpdateStatusRequest request
    ) {
        return new TicketResponse();
    }

    @PatchMapping("/{id}/assignee")
    public TicketResponse assignTicket(
            @PathVariable UUID id,
            @RequestBody AssignTicketRequest request
    ) {
        return new TicketResponse();
    }

}