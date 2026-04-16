package development.team.ticketsystem.gptservice.controller;

import development.team.ticketsystem.gptservice.dto.UpgradeRequestDto;
import development.team.ticketsystem.gptservice.dto.UpgradeResponseDto;
import development.team.ticketsystem.gptservice.service.YaGptChatService;
import development.team.ticketsystem.gptservice.service.interfaces.LlmServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GptController {
    private final LlmServiceInterface yaGptChatService;

    @PostMapping("/upgrade")
    public ResponseEntity<UpgradeResponseDto> upgradeDescription(@RequestBody UpgradeRequestDto request) {
        String upgradedDescription = this.yaGptChatService.upgradeDescription(request.getDescription());

        return ResponseEntity.ok(UpgradeResponseDto.builder()
                .originalDescription(request.getDescription())
                .upgradedDescription(upgradedDescription)
                .build());
    }
}