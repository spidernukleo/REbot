package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.CallBackQuery;
import nukleo.REbot.model.InlineKeyboardButton;
import nukleo.REbot.model.InlineKeyboardMarkup;
import nukleo.REbot.util.CommandsManager;
import nukleo.REbot.util.TranslationManager;
import org.springframework.stereotype.Service;

import static nukleo.REbot.model.InlineKeyboardButton.cb;
import static nukleo.REbot.model.InlineKeyboardMarkup.genMenu;


@Service
@AllArgsConstructor
public class CallbackService {

    private final CommandsManager commandsManager;
    private TranslationManager translationManager;
    private TelegramService telegramService;

    public void handleCallBack(CallBackQuery query) {
        String data = query.getData();
        Long chatId = query.getMessage().getChat().getId();
        String queryId = query.getId();
        Integer messageId = query.getMessage().getMessage_id();
        long senderId = query.getFrom().getId();
        if(!telegramService.isUserAdmin(chatId, senderId)) return;
        if (data == null) return;
        else if(data.equals("/langit")) {
            translationManager.setLanguage(chatId, "it");
            telegramService.answerCallback(queryId, translationManager.getMessage(chatId, "done"));
            telegramService.deleteMessage(chatId, messageId);
        }
        else if(data.equals("/langen")) {
            translationManager.setLanguage(chatId, "en");
            telegramService.answerCallback(queryId, translationManager.getMessage(chatId, "done"));
            telegramService.deleteMessage(chatId, messageId);
        }
        else if(data.startsWith("/info_")) {
            telegramService.answerCallback(queryId, translationManager.getMessage(chatId, "infocmd"));
        }
        else if(data.startsWith("/del_")) {
            String cmdToDelete = data.substring(5);
            InlineKeyboardMarkup menu = genMenu(
                    new InlineKeyboardButton[]{
                            cb(translationManager.getMessage(chatId, "dismiss"), "/dismissdel"),
                            cb(translationManager.getMessage(chatId, "confirm"), "/confirmdel_"+cmdToDelete)
                    }
            );
            telegramService.editMessage(chatId, messageId, translationManager.getMessage(chatId, "deletecmdinfo") + "<i>"+cmdToDelete+"</i>", menu);
        }
        else if (data.startsWith("/confirmdel_")) {
            String cmdToDelete = data.substring(12);
            commandsManager.removeChatCommand(chatId, cmdToDelete);
            telegramService.answerCallback(queryId, translationManager.getMessage(chatId, "done"));
            telegramService.deleteMessage(chatId, messageId);
        }
        else if(data.startsWith("/dismissdel")) {
            telegramService.deleteMessage(chatId, messageId);
        }

    }

}
