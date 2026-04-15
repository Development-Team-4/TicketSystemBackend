package development.team.ticketsystem.bot.controller;

import development.team.ticketsystem.bot.dto.CreateMessageRequest;
import development.team.ticketsystem.bot.service.TelegramMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/telegram")
@RequiredArgsConstructor
public class InternalTelegramController {

    private final TelegramMessageService telegramMessageService;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestBody CreateMessageRequest request) {
        telegramMessageService.sendMessage(request.getChatId(), request.getText());
        return ResponseEntity.ok().build();
    }
}
