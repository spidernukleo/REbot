package nukleo.REbot.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramService {

    private final String botToken = System.getenv("TELEGRAM_REBOT_TOKEN");
    private final String tgUrl = "https://api.telegram.org/bot"+botToken;
    public final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(Long chatId, String text){
        Map<String, Object> req = Map.of(
                "chat_id", chatId,
                "text", text
        );
        new RestTemplate().postForObject(tgUrl+"/sendMessage", req, String.class);
    }
}
