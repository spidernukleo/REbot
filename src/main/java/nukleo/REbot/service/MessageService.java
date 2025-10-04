package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class MessageService {

    private final TelegramService telegramService;

    public void handleMessage(Map<String, Object> message) {
        @SuppressWarnings("unchecked")
        Map<String, Object> chat = (Map<String, Object>) message.get("chat");
        Long chatId = ((Number) chat.get("id")).longValue();


        if(message.get("text").equals("/start")){
            telegramService.sendMessage(chatId, "BENVENUTO");
        }
        else{
            telegramService.sendMessage(chatId, "Comando sconosciuto");
        }
    }
}
