package development.team.ticketsystem.bot.dispatcher;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;

import development.team.ticketsystem.bot.command.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final List<Command> commands;
    private final TelegramBot telegramBot;

    public void dispatch(Update update) {
        if (!isValidUpdate(update)) {
            return;
        }

        var text = update.message().text();
        var chatId = update.message().chat().id();

        if (!isCommand(text)) {
            handleNonCommandMessage(chatId);
            return;
        }

        handleCommand(update, chatId, text);
    }

    private boolean isValidUpdate(Update update) {
        if (update == null || update.message() == null) {
            log.debug("Update or message is null");
            return false;
        }
        if (update.message().chat() == null) {
            log.debug("Chat is null in message");
            return false;
        }
        if (update.message().text() == null) {
            log.debug("Update without text message received: update={}", update);
            return false;
        }
        return true;
    }

    private boolean isCommand(String text) {
        return text.startsWith("/");
    }

    private void handleCommand(Update update, Long chatId, String text) {
        String commandName = text.split(" ")[0];
        Command command = commands.stream()
                .filter(cmd -> cmd.name().equals(commandName))
                .findFirst()
                .orElse(null);

        if (command != null) {
            log.debug("Executing command: {} for chatId: {}", commandName, chatId);
            command.handle(update);
        } else {
            handleUnknownCommand(chatId, commandName);
        }
    }

    private void handleUnknownCommand(Long chatId, String commandName) {
        log.warn("Unknown command received. chatId={}, command={}", chatId, commandName);
        telegramBot.execute(new SendMessage(
                chatId, "Неизвестная команда. Используйте /help, чтобы узнать список доступных команд."));
    }

    private void handleNonCommandMessage(Long chatId) {
        log.debug("Non-command message received from chatId={}", chatId);
        telegramBot.execute(new SendMessage(
                chatId, "Бот принимает только команды /start и /help. Остальные сообщения игнорируются."));
    }
}