package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.Entity.TopicEntity;
import development.team.ticketsystem.ticketservice.Repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TopicService {

    private final TopicRepository repository;

    public TopicService(TopicRepository repository) {
        this.repository = repository;
    }

    public List<TopicEntity> getAll() {
        return repository.findAll();
    }

    public TopicEntity create(TopicEntity topic) {
        return repository.save(topic);
    }

    public TopicEntity update(UUID id, TopicEntity updated) {
        TopicEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());

        return repository.save(existing);
    }
}