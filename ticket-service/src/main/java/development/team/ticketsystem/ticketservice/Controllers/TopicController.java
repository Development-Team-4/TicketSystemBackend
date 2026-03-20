package development.team.ticketsystem.ticketservice.Controllers;

import development.team.ticketsystem.ticketservice.DTO.topics.CreateTopicRequest;
import development.team.ticketsystem.ticketservice.DTO.topics.TopicResponse;
import development.team.ticketsystem.ticketservice.Entity.TopicEntity;
import development.team.ticketsystem.ticketservice.Service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/topics")
@Tag(name = "Topics", description = "Управление темами")
public class TopicController {

    private final TopicService service;

    public TopicController(TopicService service) {
        this.service = service;
    }

    @Operation(summary = "Получить все темы")
    @GetMapping
    public List<TopicResponse> getTopics() {
        return service.getAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Operation(summary = "Создать тему")
    @PostMapping
    public TopicResponse createTopic(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания темы")
            @RequestBody CreateTopicRequest request
    ) {
        TopicEntity entity = new TopicEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());

        return toResponse(service.create(entity));
    }

    @Operation(summary = "Обновить тему")
    @PatchMapping("/{id}")
    public TopicResponse updateTopic(
            @Parameter(description = "ID темы", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для обновления темы")
            @RequestBody CreateTopicRequest request
    ) {
        TopicEntity updated = new TopicEntity();
        updated.setName(request.getName());
        updated.setDescription(request.getDescription());

        return toResponse(service.update(id, updated));
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