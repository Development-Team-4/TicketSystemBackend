package development.team.ticketsystem.gptservice.exception;

import lombok.Getter;

@Getter
public class YaGptApiException extends RuntimeException {
    private final int statusCode;

    public YaGptApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public YaGptApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }

}
