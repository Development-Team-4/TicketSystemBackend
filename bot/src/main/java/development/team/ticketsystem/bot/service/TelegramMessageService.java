package development.team.ticketsystem.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramMessageService {

    private final TelegramBot telegramBot;

    public void sendMessage(Long chatId, String text, String ticketUrl) {
        telegramBot.execute(
                new SendMessage(chatId, text)
                        .parseMode(ParseMode.HTML).replyMarkup(new InlineKeyboardMarkup(
                                new InlineKeyboardButton("Открыть тикет").url(ticketUrl)
                        ))
        );

        log.info("Telegram message sent. chatId={}", chatId);
    }
}
