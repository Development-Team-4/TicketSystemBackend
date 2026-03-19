package development.team.ticketsystem.ticketservice.Controllers;

import development.team.ticketsystem.ticketservice.DTO.topics.CreateTopicRequest;
import development.team.ticketsystem.ticketservice.DTO.topics.TopicResponse;
import development.team.ticketsystem.ticketservice.Entity.TopicEntity;
import development.team.ticketsystem.ticketservice.Service.TopicService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicService service;

    public TopicController(TopicService service) {
        this.service = service;
    }

    @GetMapping
    public List<TopicResponse> getTopics() {
        return service.getAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    public TopicResponse createTopic(@RequestBody CreateTopicRequest request) {
        TopicEntity entity = new TopicEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());

        return toResponse(service.create(entity));
    }

    @PatchMapping("/{id}")
    public TopicResponse updateTopic(
            @PathVariable UUID id,
            @RequestBody CreateTopicRequest request
    ) {
        TopicEntity updated = new TopicEntity();
        updated.setName(request.getName());
        updated.setDescription(request.getDescription());

        return toResponse(service.update(id, updated));
    }

    // mapper
    private TopicResponse toResponse(TopicEntity entity) {
        TopicResponse response = new TopicResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }

}