package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.util.BotConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@Service
public class TelegramService {

    public final RestTemplate restTemplate = new RestTemplate();
    public BotConfig botConfig;

    public void sendMessage(Long chatId, String text){
        Map<String, Object> req = Map.of(
                "chat_id", chatId,
                "text", text
        );
        new RestTemplate().postForObject(botConfig.getTgUrl()+"/sendMessage", req, String.class);
    }
}
