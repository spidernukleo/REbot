package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.Message;
import nukleo.REbot.repository.RedisRepository;
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
    private final RedisRepository redisRepository;

    public void handleMessage(Message message) {
        if (message.getText() != null) {
            Map<String, Runnable> commands = new HashMap<>();
            commands.put("/topdaniele".toLowerCase(), () -> handleTop(message));
            commands.put("/topdaniele@reclonebot".toLowerCase(), () -> handleTopWrap(message));
            commands.put("daniele".toLowerCase(), () -> handleDaniele(message)); //mappa con tutti i comandi e che funzione eseguire

            commands.getOrDefault(message.getText().toLowerCase(), () -> {}).run(); //se non trovato non lanciare nulla
        }
    }

    //handle all commands



    public void handleTop(Message message) {
        if(message.getChat().getType().equals("private")) return;
        //remove the or true when deploy
        if((redisRepository.canExecute(message.getChat().getId(), 20000)) || true) {
            String text="cacca culo top";
            telegramService.sendMessage(message.getChat().getId(), text);
        }else{
            telegramService.sendMessage(message.getChat().getId(), "ancora non passati");
        }
    }

    public void handleDaniele(Message message){
        telegramService.sendMessage(message.getChat().getId(), "sei diventato");
    }

    public void handleTopWrap(Message message) {
        handleTop(message); //used cuz if they go with the @command yk alr
    }
}
