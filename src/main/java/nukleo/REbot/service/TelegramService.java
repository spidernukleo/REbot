package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.util.BotConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@Service
@AllArgsConstructor
public class TelegramService {

    private final BotConfig botConfig;
    public final RestTemplate restTemplate = new RestTemplate();

    private final String tgUrl = "https://api.telegram.org/bot"+botConfig.getBotToken();


    public void sendMessage(Long chatId, String text){
        Map<String, Object> req = Map.of(
                "chat_id", chatId,
                "text", text
        );
        new RestTemplate().postForObject(tgUrl+"/sendMessage", req, String.class);
    }
}
