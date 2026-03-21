package development.team.ticketsystem.ticketservice.Controllers;

import development.team.ticketsystem.ticketservice.DTO.topics.CreateTopicRequest;
import development.team.ticketsystem.ticketservice.DTO.topics.TopicResponse;
import development.team.ticketsystem.ticketservice.Service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/topics")
@Tag(name = "Topics", description = "Управление темами")
public class TopicController {

    private final TopicService service;

    @Operation(summary = "Получить все темы")
    @GetMapping
    public List<TopicResponse> getTopics() {
        return service.getAll();
    }

    @Operation(summary = "Создать тему")
    @PostMapping
    public TopicResponse createTopic(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания темы")
            @RequestBody CreateTopicRequest request
    ) {
        return service.create(request);
    }

    @Operation(summary = "Обновить тему")
    @PatchMapping("/{id}")
    public TopicResponse updateTopic(
            @Parameter(description = "ID темы", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для обновления темы")
            @RequestBody CreateTopicRequest request
    ) {
        return service.update(id, request);
    }

}