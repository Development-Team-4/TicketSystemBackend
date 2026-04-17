package development.team.ticketsystem.gptservice.controller;

import development.team.ticketsystem.gptservice.dto.UpgradeRequestDto;
import development.team.ticketsystem.gptservice.dto.UpgradeResponseDto;
import development.team.ticketsystem.gptservice.service.YaGptChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GptController {
    private final YaGptChatService yaGptChatService;

    @PostMapping("/upgrade")
    public ResponseEntity<UpgradeResponseDto> upgradeDescription(@RequestBody UpgradeRequestDto request) {
        String upgradedDescription = yaGptChatService.upgradeDescription(
                request.getDescription(),
                request.getTicketName(),
                request.getCurrentDescription()
        );

        return ResponseEntity.ok(UpgradeResponseDto.builder()
                .originalDescription(request.getDescription())
                .upgradedDescription(upgradedDescription)
                .build());
    }
}