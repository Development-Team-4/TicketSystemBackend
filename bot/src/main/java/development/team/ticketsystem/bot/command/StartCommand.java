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
public class StartCommand implements Command {

    private static final String WELCOME_MESSAGE = "Ваш chatId:\n";

    private final TelegramBot telegramBot;

    @Override
    public String name() {
        return "/start";
    }

    @Override
    public void handle(Update update) {

        Long chatId = update.message().chat().id();

        telegramBot.execute(new SendMessage(chatId, WELCOME_MESSAGE + "```\n" + chatId + "\n```").parseMode(ParseMode.Markdown));

        log.info("Chat successfully activated. chatId={}", chatId);
    }
}
