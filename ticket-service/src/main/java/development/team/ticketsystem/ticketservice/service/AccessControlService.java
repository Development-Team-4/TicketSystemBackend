package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.UserRole;
import development.team.ticketsystem.ticketservice.entity.CategoryStaffEntity;
import development.team.ticketsystem.ticketservice.entity.TicketEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final CategoryStaffService categoryStaffService;

    public boolean canViewTicket(UserRole role, UUID userId, TicketEntity ticket) {

        if (role == UserRole.ADMIN) {
            return true;
        }

        if (role == UserRole.USER) {
            return ticket.getCreatedBy().equals(userId);
        }

        if (role == UserRole.SUPPORT) {
            boolean isAssignee = userId.equals(ticket.getAssigneeId());

            boolean inCategory = categoryStaffService.getByUser(userId)
                    .stream()
                    .map(CategoryStaffEntity::getCategoryId)
                    .anyMatch(catId -> catId.equals(ticket.getCategoryId()));

            return isAssignee || inCategory;
        }

        return false; // на будущее для новых ролей
    }

    public boolean canEditTicket(UserRole role, UUID userId, TicketEntity ticket) {

        if (role == UserRole.ADMIN) {
            return true;
        }

        if (role == UserRole.USER) {
            return ticket.getCreatedBy().equals(userId);
        }

        if (role == UserRole.SUPPORT) {
            return userId.equals(ticket.getAssigneeId());
        }

        return false;
    }

    public boolean canDeleteTicket(UserRole role, UUID userId, TicketEntity ticket) {

        if (role == UserRole.ADMIN) {
            return true;
        }

        if (role == UserRole.USER) {
            return ticket.getCreatedBy().equals(userId);
        }

        if (role == UserRole.SUPPORT) {
            boolean isAssignee = userId.equals(ticket.getAssigneeId());

            boolean inCategory = categoryStaffService.getByUser(userId)
                    .stream()
                    .map(CategoryStaffEntity::getCategoryId)
                    .anyMatch(catId -> catId.equals(ticket.getCategoryId()));

            return isAssignee || inCategory;
        }

        return false;
    }

    public boolean canCommentOnTicket(UserRole role, UUID userId, TicketEntity ticket) {

        if (role == UserRole.ADMIN) {
            return true;
        }

        if (role == UserRole.USER) {
            return ticket.getCreatedBy().equals(userId);
        }

        if (role == UserRole.SUPPORT) {
            boolean isAssignee = userId.equals(ticket.getAssigneeId());

            boolean inCategory = categoryStaffService.getByUser(userId)
                    .stream()
                    .map(CategoryStaffEntity::getCategoryId)
                    .anyMatch(catId -> catId.equals(ticket.getCategoryId()));

            return isAssignee || inCategory;
        }

        return false;
    }

    public boolean canViewTicketHistory(UserRole role, UUID userId, TicketEntity ticket) {

        if (role == UserRole.ADMIN) {
            return true;
        }

        if (role == UserRole.USER) {
            return ticket.getCreatedBy().equals(userId);
        }

        if (role == UserRole.SUPPORT) {
            boolean isAssignee = userId.equals(ticket.getAssigneeId());

            boolean inCategory = categoryStaffService.getByUser(userId)
                    .stream()
                    .map(CategoryStaffEntity::getCategoryId)
                    .anyMatch(catId -> catId.equals(ticket.getCategoryId()));

            return isAssignee || inCategory;
        }

        return false;
    }
}
