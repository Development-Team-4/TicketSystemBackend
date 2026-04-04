package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.dto.topics.CreateTopicRequest;
import development.team.ticketsystem.ticketservice.dto.topics.TopicResponse;
import development.team.ticketsystem.ticketservice.entity.TopicEntity;
import development.team.ticketsystem.ticketservice.exceptions.AccessDeniedException;
import development.team.ticketsystem.ticketservice.mappers.TopicMapper;
import development.team.ticketsystem.ticketservice.repository.TopicRepository;
import development.team.ticketsystem.ticketservice.UserRole;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TopicService {

    private final TopicRepository repository;
    private final TopicMapper mapper;

    public List<TopicResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public TopicResponse create(UserRole role, CreateTopicRequest request) throws AccessDeniedException {
        if (!role.equals(UserRole.ADMIN)) {
            throw new AccessDeniedException("Only ADMIN can create topic");
        }

        TopicEntity entity = TopicEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        TopicEntity saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public TopicResponse update(UserRole role, UUID id, CreateTopicRequest request) throws AccessDeniedException {
        if (!role.equals(UserRole.ADMIN)) {
            throw new AccessDeniedException("Only ADMIN can update topic");
        }

        TopicEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        existing.setName(request.getName())
                .setDescription(request.getDescription());

        TopicEntity saved = repository.save(existing);

        return mapper.toResponse(saved);
    }

}