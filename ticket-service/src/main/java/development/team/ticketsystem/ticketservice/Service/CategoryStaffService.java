package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.Entity.CategoryStaffEntity;
import development.team.ticketsystem.ticketservice.Repository.CategoryStaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryStaffService {

    private final CategoryStaffRepository repository;

    public CategoryStaffService(CategoryStaffRepository repository) {
        this.repository = repository;
    }

    public List<CategoryStaffEntity> getByUser(UUID userId) {
        return repository.findByUserId(userId);
    }

    public boolean isUserInCategory(UUID userId, UUID categoryId) {
        return repository.existsByCategoryIdAndUserId(categoryId, userId);
    }
}