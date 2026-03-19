package development.team.ticketsystem.ticketservice.Service;


import development.team.ticketsystem.ticketservice.Entity.CategoryEntity;
import development.team.ticketsystem.ticketservice.Repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryEntity> getByTopic(UUID topicId) {
        return repository.findByTopicId(topicId);
    }

    public CategoryEntity getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    public CategoryEntity create(CategoryEntity category) {
        return repository.save(category);
    }

    public CategoryEntity update(UUID id, CategoryEntity updated) {
        CategoryEntity existing = getById(id);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());

        return repository.save(existing);
    }
}