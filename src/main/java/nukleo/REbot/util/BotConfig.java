package nukleo.REbot.util;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BotConfig {

    private final String botToken;
    private final long botId;
    private final String tgUrl;

    public BotConfig(){
        this.botToken = System.getenv("TELEGRAM_REBOT_TOKEN");
        if(this.botToken == null||this.botToken.isEmpty()||!this.botToken.contains(":")){
            throw new IllegalStateException("Invalid bot token");
        }
        this.botId = Long.parseLong(botToken.split(":")[0]);
        this.tgUrl = "https://api.telegram.org/bot" + botToken;
    }
}
