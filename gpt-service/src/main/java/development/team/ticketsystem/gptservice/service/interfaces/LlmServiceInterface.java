package development.team.ticketsystem.gptservice.service.interfaces;

/**
 * Интерфейс взаимодействия с удалёнными LLM для формирования
 * обновлённого описания тикета
 */
public interface LlmServiceInterface {
    String upgradeDescription(String originalDescription);
}
