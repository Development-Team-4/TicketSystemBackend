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
        TopicEntity entity = TopicEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        TopicEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    public TopicResponse update(UUID id, CreateTopicRequest request) {
        TopicEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        existing.setName(request.getName())
                .setDescription(request.getDescription());

        TopicEntity saved = repository.save(existing);

        return toResponse(saved);
    }

    // mapper - ИСПОЛЬЗОВАТЬ БИБЛИОТЕКУ mapstruct
    private TopicResponse toResponse(TopicEntity entity) {
        return TopicResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}