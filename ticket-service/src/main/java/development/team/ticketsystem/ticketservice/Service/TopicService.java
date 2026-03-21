package development.team.ticketsystem.ticketservice.Service;

import development.team.ticketsystem.ticketservice.DTO.topics.CreateTopicRequest;
import development.team.ticketsystem.ticketservice.DTO.topics.TopicResponse;
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

    public List<TopicResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TopicResponse create(CreateTopicRequest request) {
        TopicEntity entity = new TopicEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());

        TopicEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    public TopicResponse update(UUID id, CreateTopicRequest request) {
        TopicEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());

        TopicEntity saved = repository.save(existing);

        return toResponse(saved);
    }

    // mapper - ИСПОЛЬЗОВАТЬ БИБЛИОТЕКУ mapstruct
    private TopicResponse toResponse(TopicEntity entity) {
        TopicResponse response = new TopicResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }
}