package development.team.ticketsystem.ticket_service.Controllers;

import development.team.ticketsystem.ticket_service.DTO.topics.CreateTopicRequest;
import development.team.ticketsystem.ticket_service.DTO.topics.TopicResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/topics")
public class TopicController {

    @GetMapping
    public List<TopicResponse> getTopics() {
        return List.of();
    }

    @PostMapping
    public TopicResponse createTopic(@RequestBody CreateTopicRequest request) {
        return new TopicResponse();
    }

    @PatchMapping("/{id}")
    public TopicResponse updateTopic(
            @PathVariable UUID id,
            @RequestBody CreateTopicRequest request
    ) {
        return new TopicResponse();
    }

}