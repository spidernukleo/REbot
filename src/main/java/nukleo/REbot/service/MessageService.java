package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@AllArgsConstructor
public class MessageService {

    private final TelegramService telegramService;

    private Long getChatId(Map<String, Object> message){
        @SuppressWarnings("unchecked")
        Map<String, Object> chat = (Map<String, Object>) message.get("chat");
        return ((Number) chat.get("id")).longValue();
    }

    public void handleMessage(Map<String, Object> message) {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("/topdaniele".toLowerCase(), () -> handleTop(message));
        commands.put("daniele".toLowerCase(), () -> handleDaniele(message)); //mappa con tutti i comandi e che funzione eseguire

        commands.getOrDefault(((String) message.get("text")).toLowerCase(), ()->{}).run(); //se non trovato non lanciare nulla
    }

    public void handleTop(Map<String, Object> message){
        telegramService.sendMessage(getChatId(message), "ecco la top");
    }

    public void handleDaniele(Map<String, Object> message){
        telegramService.sendMessage(getChatId(message), "sei diventato");
    }
}
