package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.*;
import nukleo.REbot.repository.CoreRepository;
import nukleo.REbot.repository.RedisRepository;
import nukleo.REbot.util.CommandsManager;
import nukleo.REbot.util.TranslationManager;
import org.springframework.stereotype.Service;

import java.util.List;

import static nukleo.REbot.model.InlineKeyboardButton.cb;
import nukleo.REbot.util.CoreManager;
import static nukleo.REbot.model.InlineKeyboardMarkup.genMenu;

@Service
@AllArgsConstructor
public class MessageService {
    private final TelegramService telegramService;
    private final RedisRepository redisRepository;
    private final CommandsManager commandsManager;
    private CoreRepository coreRepository;
    private TranslationManager translationManager;
    private CoreManager helper;


    public void handleMessage(Message message) {
        if(message.getChat().getType().equals("private")) return;
        String text = message.getText();
        long chatId = message.getChat().getId();
        if(text==null) return;

        else if(commandsManager.getGroupCommands(chatId).contains(text.toLowerCase())){
            String cmd=text.toLowerCase();
            String userFirstName=message.getFrom().getFirst_name();
            if (!redisRepository.setKingIfAbsent(chatId, userFirstName, cmd)) {
                String king = redisRepository.getKing(chatId, cmd);
                telegramService.sendMessage(chatId,
                        translationManager.getMessage(chatId, "hey") + king +
                                translationManager.getMessage(chatId, "already") + cmd +
                                translationManager.getMessage(chatId, "oftoday"));
                return;
            }
            coreRepository.incrementPoints(chatId, message.getFrom().getId(), cmd, userFirstName);
            telegramService.sendMessage(chatId,
                    translationManager.getMessage(chatId, "congrats") + userFirstName +
                            translationManager.getMessage(chatId, "youare") + cmd +
                            translationManager.getMessage(chatId, "oftoday"));
        }

        else if(text.startsWith("/lang")) {
            if(!telegramService.isUserAdmin(chatId, message.getFrom().getId())) return;
            InlineKeyboardMarkup menu = genMenu(
                    new InlineKeyboardButton[]{
                            cb("\uD83C\uDDEE\uD83C\uDDF9", "/langit"),
                            cb("\uD83C\uDDFA\uD83C\uDDF8", "/langen")
                    } //add flags here
            );
            telegramService.sendMessage(chatId, translationManager.getMessage(chatId, "changelang"), menu);
        }

        else if(text.startsWith("/kings")) {
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

        else if(text.startsWith("/setking")) {
            if(!telegramService.isUserAdmin(chatId, message.getFrom().getId())) return;
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

        else if(text.startsWith("/top")) {
            if(redisRepository.canExecute(message.getChat().getId(), 20000)) {
                helper.genTop(chatId, 0, "/");
            }
        }
    }
}
