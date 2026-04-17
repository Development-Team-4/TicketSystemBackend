package development.team.ticketsystem.bot.command;


import com.pengrad.telegrambot.model.Update;

public interface Command {

    String name();
    
    void handle(Update update);
}
