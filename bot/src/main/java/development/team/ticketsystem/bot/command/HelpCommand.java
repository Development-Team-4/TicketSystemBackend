package development.team.ticketsystem.bot.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private static final String MESSAGE_AVAILABLE_COMMANDS = "Бот предназначен только для уведомлений и получения chatId, который нужно добавить в личном кабинете." +
            "\nПоддерживаемые комманды:\n" +
            "/start\n" +
            "/help" + "\n"+ "Ваш chatId:\n";

    private final TelegramBot telegramBot;

    @Override
    public String name() {
        return "/help";
    }

    @Override
    public void handle(Update update) {
        var chatId = update.message().chat().id();

        try {
            telegramBot.execute(new SendMessage(
                    chatId,
                    MESSAGE_AVAILABLE_COMMANDS + "```" + chatId + "```").parseMode(ParseMode.Markdown));
        } catch (Exception exception) {
            log.error("Failed to send message. Command /help.", exception);
        }
    }
}

