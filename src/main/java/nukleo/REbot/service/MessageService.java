package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.*;
import nukleo.REbot.repository.CoreRepository;
import nukleo.REbot.repository.RedisRepository;
import nukleo.REbot.util.CommandsManager;
import nukleo.REbot.util.TranslationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nukleo.REbot.model.InlineKeyboardButton.cb;
import static nukleo.REbot.model.InlineKeyboardButton.link;
import static nukleo.REbot.model.InlineKeyboardMarkup.genMenu;

@Service
@AllArgsConstructor
public class MessageService {
    private final TelegramService telegramService;
    private final RedisRepository redisRepository;
    private final CommandsManager commandsManager;
    private CoreRepository coreRepository;
    private TranslationManager translationManager;


    public void handleMessage(Message message) {
        if(message.getChat().getType().equals("private")) return;
        String text = message.getText();
        long chatId = message.getChat().getId();
        long senderId = message.getFrom().getId();
        if(text==null) return;

        else if(text.startsWith("/lang")) {
            if(!telegramService.isUserAdmin(chatId, senderId)) return;
            InlineKeyboardMarkup menu = genMenu(
                    new InlineKeyboardButton[]{
                            cb("\uD83C\uDDEE\uD83C\uDDF9", "/langit"),
                            cb("\uD83C\uDDFA\uD83C\uDDF8", "/langen")
                    } //add flags here
            );
            telegramService.sendMessage(chatId, translationManager.getMessage(chatId, "changelang"), menu);
        }

        else if(text.startsWith("/commands")) {
            List<String> commands = commandsManager.getGroupCommands(chatId);
            if(!commands.isEmpty()) {
                InlineKeyboardButton[][] rows = commands.stream()
                        .map(cmd -> new InlineKeyboardButton[]{
                                cb(cmd, "/info_"+cmd),                     // command button
                                cb("❌", "/del_"+cmd)            // delete button with unique callback
                        })
                        .toArray(InlineKeyboardButton[][]::new);
                telegramService.sendMessage(chatId, translationManager.getMessage(chatId, "kings"), genMenu(rows));
            }
            else telegramService.sendMessage(chatId, translationManager.getMessage(chatId, "nocmds"));
        }

        else if(text.equals("/setking")) {
            if(!telegramService.isUserAdmin(chatId, senderId)) return;
            Message repliedMessage = message.getReply_to_message();
            if(repliedMessage == null) telegramService.sendMessage(chatId, translationManager.getMessage(chatId, "notreply"));
            else{
                String command = repliedMessage.getText();
                if(command!=null && command.length()<10 && !command.startsWith("/")) {
                    if(command.contains(" ")) telegramService.sendMessage(chatId, translationManager.getMessage(chatId, "nospaces"));
                    else if(commandsManager.addChatCommand(chatId, command)) telegramService.sendMessage(chatId, translationManager.getMessage(chatId, "setking"));
                    else telegramService.sendMessage(chatId, translationManager.getMessage(chatId, "toomanycmds"));
                }
                else
                    telegramService.sendMessage(chatId, translationManager.getMessage(chatId, "toolong"));
            }
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
