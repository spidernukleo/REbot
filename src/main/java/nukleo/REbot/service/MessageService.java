package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.Message;
import nukleo.REbot.model.TopRecord;
import nukleo.REbot.repository.DatabaseRepository;
import nukleo.REbot.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MessageService {
    @Autowired
    private JdbcTemplate jdbc;
    private final TelegramService telegramService;
    private final RedisRepository redisRepository;
    private DatabaseRepository databaseRepository;

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

    private String generateTop(List<? extends TopRecord> records){
        StringBuilder text= new StringBuilder();
        for(int i=1;i<records.size()+1;i++) {
            TopRecord record = records.get(i-1);
            text.append("\n").append(i).append("° • ").append(record.getFirstName()).append(" | ").append(record.getPoints());
        }
        return text.toString();
    }

    public void handleTop(Message message) {
        if(message.getChat().getType().equals("private")) return;
        //if(redisRepository.canExecute(message.getChat().getId(), 20000)) {
            List<TopRecord> records = databaseRepository.getTopRecords(message.getChat().getId());
            String text="\uD83D\uDD1D || <b>CLASSIFICA DANIELI</b>:"+this.generateTop(records)+"\n\n<i>Per qualsiasi problema @nukleolimitatibot</i>";
            telegramService.sendMessage(message.getChat().getId(), text);
        //}
    }

    public void handleDaniele(Message message){
        if(message.getChat().getType().equals("private")) return;
        Long chatid = message.getChat().getId();
        Long userId= message.getFrom().getId();
        String userFirstName = message.getFrom().getFirst_name();
        String king= redisRepository.getKing(chatid);
        if(king!=null){
            telegramService.sendMessage(message.getChat().getId(), "\uD83D\uDE2D || HEY\n\n\uD83D\uDC51 — "+king+" ha già preso il posto di Re Daniele di oggi");
            return;
        }
        redisRepository.setKing(chatid, userFirstName);
        databaseRepository.incrementPoints(chatid, userId, userFirstName);
        telegramService.sendMessage(message.getChat().getId(), "\uD83C\uDF89 || Complimenti!\n\n\uD83D\uDC51 — "+userFirstName+" sei il Re Daniele di oggi!");
    }

    public void handleTopWrap(Message message) {
        handleTop(message); //used cuz if they go with the @command yk alr
    }
}
