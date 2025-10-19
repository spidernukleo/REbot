package nukleo.REbot.util;

import lombok.AllArgsConstructor;
import nukleo.REbot.model.InlineKeyboardButton;
import nukleo.REbot.model.TopRecord;
import nukleo.REbot.repository.CoreRepository;
import nukleo.REbot.service.TelegramService;
import org.springframework.stereotype.Component;

import java.util.List;

import static nukleo.REbot.model.InlineKeyboardButton.cb;
import static nukleo.REbot.model.InlineKeyboardMarkup.genMenu;

@AllArgsConstructor
@Component
public class CoreManager {
    private TranslationManager translationManager;
    private CommandsManager commandsManager;
    private TelegramService telegramService;
    private CoreRepository coreRepository;


    public void genTop(long chatid, Integer messageid, String cmd){
        List<String> commands = commandsManager.getGroupCommands(chatid);
        if(!commands.isEmpty()){
            if(cmd.equals("/")) cmd=commands.getFirst();
            if(commands.contains(cmd)){
                if(messageid!=0)
                    telegramService.editMessage(chatid, messageid, genTopText(cmd, chatid), genMenu(genTopMenu(commands, cmd)));
                else
                    telegramService.sendMessage(chatid, genTopText(commands.getFirst(), chatid), genMenu(genTopMenu(commands, cmd)));
            }
            else
                telegramService.deleteMessage(chatid, messageid);
        }
        else if(messageid==0) telegramService.sendMessage(chatid, translationManager.getMessage(chatid, "nocmds"));
        else telegramService.deleteMessage(chatid, messageid);
    }

    private String genTopText(String cmd, long chatid){
        List<TopRecord> records = this.getTopRecords(chatid, cmd);
        StringBuilder top = new StringBuilder();
        top.append(translationManager.getMessage(chatid, "topText"));
        top.append(" ").append(cmd);
        for(int i=1;i<records.size()+1;i++) {
            TopRecord record = records.get(i-1);
            top.append("\n").append(i).append("° • ").append(record.getFirstName()).append(" | ").append(record.getPoints());
        }
        top.append(translationManager.getMessage(chatid, "bottomText"));
        return top.toString();
    }

    private InlineKeyboardButton[][] genTopMenu(List<String> commands, String currCmd){
        return new InlineKeyboardButton[][] {
                commands.stream()
                        .filter(cmd -> !cmd.equals(currCmd))
                        .map(cmd -> cb(cmd, "/top_" + cmd))
                        .toArray(InlineKeyboardButton[]::new)
        };
    }

    public void createPointsTable(){
        coreRepository.createPointsTable();
    }

    private List<TopRecord> getTopRecords(Long chatid, String command){
        return coreRepository.getTopRecords(chatid, command);
    }
}
