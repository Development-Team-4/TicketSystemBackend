package development.team.ticketsystem.ticketservice.service;

import development.team.ticketsystem.ticketservice.UserRole;
import development.team.ticketsystem.ticketservice.dto.topics.CreateTopicRequest;
import development.team.ticketsystem.ticketservice.dto.topics.TopicResponse;
import development.team.ticketsystem.ticketservice.entity.TopicEntity;
import development.team.ticketsystem.ticketservice.exceptions.AccessDeniedException;
import development.team.ticketsystem.ticketservice.mappers.TopicMapper;
import development.team.ticketsystem.ticketservice.repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public List<TopicResponse> getAll() {
        return topicRepository.findAll()
                .stream()
                .map(topicMapper::toResponse)
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

        TopicEntity saved = topicRepository.save(entity);
        return topicMapper.toResponse(saved);
    }

    @Transactional
    public TopicResponse update(UserRole role, UUID id, CreateTopicRequest request) throws AccessDeniedException {
        if (!role.equals(UserRole.ADMIN)) {
            throw new AccessDeniedException("Only ADMIN can update topic");
        }

        TopicEntity existing = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        existing.setName(request.getName())
                .setDescription(request.getDescription());

        TopicEntity saved = topicRepository.save(existing);

        return topicMapper.toResponse(saved);
    }

}