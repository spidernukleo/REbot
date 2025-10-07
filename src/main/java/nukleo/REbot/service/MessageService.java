package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class MessageService {
    @Autowired
    private JdbcTemplate jdbc;
    private final TelegramService telegramService;

    public void handleMessage(Message message) {
        if (message.getText() != null) {
            Map<String, Runnable> commands = new HashMap<>();
            commands.put("/topdaniele".toLowerCase(), () -> handleTop(message));
            commands.put("daniele".toLowerCase(), () -> handleDaniele(message)); //mappa con tutti i comandi e che funzione eseguire

            commands.getOrDefault(message.getText().toLowerCase(), () -> {}).run(); //se non trovato non lanciare nulla
        }
    }

    //handle all commands
    public void handleTop(Message message) {
        telegramService.sendMessage(message.getChat().getId(), "ecco la top");
    }

    public void handleDaniele(Message message){
        telegramService.sendMessage(message.getChat().getId(), "sei diventato");
    }
}
