package development.team.ticketsystem.gptservice.controller;

import development.team.ticketsystem.gptservice.dto.error.ErrorResponse;
import development.team.ticketsystem.gptservice.dto.UpgradeRequestDto;
import development.team.ticketsystem.gptservice.dto.UpgradeResponseDto;
import development.team.ticketsystem.gptservice.service.YaGptChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
@Tag(name = "Интеграция с YandexGPT", description = """
        Интеграция с Yandex GPT для автоматического улучшения описаний тикетов.
        
        Сервис использует нейросеть Yandex GPT для:
        - Структурирования неформальных описаний проблем
        - Добавления чёткости и конкретики в описание тикета
        - Выделения ключевых требований и ожидаемых результатов
        
        Нейросеть анализирует исходное описание и возвращает его улучшенную версию,
        сохраняя исходный смысл, но делая его более профессиональным и структурированным.
        """)
public class GptController {
    private final YaGptChatService yaGptChatService;

    @Operation(
            summary = "Улучшить описание тикета",
            description = """
                    Принимает текущее описание тикета и возвращает его улучшенную версию.
                    
                    Что делает нейросеть:
                    - Делает описание более подробным и понятным
                    - Добавляет структуру
                    - Описывает ожидаемый результат
                    - Использует профессиональный, но понятный язык
                    
                    Важно:
                    - Нейросеть только переформулирует описание
                    - Исходный смысл сохраняется
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Описание успешно улучшено",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpgradeResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера или ошибка при вызове Yandex GPT API",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/upgrade")
    public ResponseEntity<UpgradeResponseDto> upgradeDescription(
            @Parameter(
                    description = "Запрос на улучшение описания тикета",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpgradeRequestDto.class)
                    )
            )
            @RequestBody UpgradeRequestDto request
    ) {
        String upgradedDescription = yaGptChatService.upgradeDescription(
                request.getTicketName(),
                request.getCurrentDescription()
        );

        return ResponseEntity.ok(
                UpgradeResponseDto.builder()
                        .originalDescription(request.getCurrentDescription())
                        .upgradedDescription(upgradedDescription)
                        .build()
        );
    }
}