package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.InlineKeyboardButton;
import nukleo.REbot.model.InlineKeyboardMarkup;
import nukleo.REbot.model.Message;
import nukleo.REbot.model.TopRecord;
import nukleo.REbot.repository.CoreRepository;
import nukleo.REbot.repository.RedisRepository;
import nukleo.REbot.util.TranslationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nukleo.REbot.model.InlineKeyboardButton.cb;
import static nukleo.REbot.model.InlineKeyboardButton.link;
import static nukleo.REbot.model.InlineKeyboardMarkup.genMenu;

@Service
@AllArgsConstructor
public class MessageService {
    @Autowired
    private JdbcTemplate jdbc;
    private final TelegramService telegramService;
    private final RedisRepository redisRepository;
    private CoreRepository coreRepository;
    private TranslationManager translationManager;

    public void handleMessage(Message message) {
        if(message.getChat().getType().equals("private")) return;
        if (message.getText() != null) {
            Map<String, Runnable> commands = new HashMap<>();
            commands.put("/lang".toLowerCase(), () -> handleLang(message));
            commands.put("/lang@reclonebot".toLowerCase(), () -> handleLang(message));
            commands.put("/topdaniele".toLowerCase(), () -> handleTop(message));
            commands.put("/topdaniele@reclonebot".toLowerCase(), () -> handleTop(message));
            commands.put("/test".toLowerCase(), () -> comandoTestLingua(message));
            commands.put("daniele".toLowerCase(), () -> handleDaniele(message)); //mappa con tutti i comandi e che funzione eseguire

            commands.getOrDefault(message.getText().toLowerCase(), () -> {}).run(); //se non trovato non lanciare nulla
        }
    }

    //handle all commands

    private void comandoTestLingua(Message message) {
        Long chatId = message.getChat().getId();
        telegramService.sendMessage(
                chatId,
                translationManager.getMessage(chatId, "test")
        );
    }

    private void handleLang(Message message) {
        long chatId = message.getChat().getId();

        InlineKeyboardMarkup menu = genMenu(
                new InlineKeyboardButton[]{ cb("\uD83C\uDDEE\uD83C\uDDF9", "/langit"), cb("\uD83C\uDDFA\uD83C\uDDF8", "/langen") }
        );
        telegramService.sendMessage(
                chatId,
                translationManager.getMessage(chatId, "changelang"),
                menu
        );
    }

    private void handleTop(Message message) {
        //if(redisRepository.canExecute(message.getChat().getId(), 20000)) {
            List<TopRecord> records = coreRepository.getTopRecords(message.getChat().getId());
            String text="\uD83D\uDD1D || <b>CLASSIFICA DANIELI</b>:"+this.generateTop(records)+"\n\n<i>Per qualsiasi problema @nukleolimitatibot</i>";
            telegramService.sendMessage(message.getChat().getId(), text);
        //}
    }

    private void handleDaniele(Message message){
        Long chatid = message.getChat().getId();
        Long userId= message.getFrom().getId();
        String userFirstName = message.getFrom().getFirst_name();
        String king= redisRepository.getKing(chatid);
        if(king!=null){
            telegramService.sendMessage(message.getChat().getId(), "\uD83D\uDE2D || HEY\n\n\uD83D\uDC51 — "+king+" ha già preso il posto di Re Daniele di oggi");
            return;
        }
        redisRepository.setKing(chatid, userFirstName);
        coreRepository.incrementPoints(chatid, userId, userFirstName);
        telegramService.sendMessage(message.getChat().getId(), "\uD83C\uDF89 || Complimenti!\n\n\uD83D\uDC51 — "+userFirstName+" sei il Re Daniele di oggi!");
    }


    private String generateTop(List<? extends TopRecord> records){
        StringBuilder text= new StringBuilder();
        for(int i=1;i<records.size()+1;i++) {
            TopRecord record = records.get(i-1);
            text.append("\n").append(i).append("° • ").append(record.getFirstName()).append(" | ").append(record.getPoints());
        }
        return text.toString();
    }
}
