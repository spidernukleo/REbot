package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.CallBackQuery;
import nukleo.REbot.util.TranslationManager;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class CallbackService {

    private TranslationManager translationManager;
    private TelegramService telegramService;

    public void handleCallBack(CallBackQuery query) {
        String data = query.getData();
        Long chatId = query.getMessage().getChat().getId();
        String queryId = query.getId();
        Integer messageId = query.getMessage().getMessage_id();
        if (data == null) return;
        else if(data.equals("/langit")) {
            translationManager.setLanguage(chatId, "it");
            telegramService.answerCallback(queryId, translationManager.getMessage(chatId, "changedLang"));
            telegramService.deleteMessage(chatId, messageId);
        }
        else if(data.equals("/langen")) {
            translationManager.setLanguage(chatId, "en");
            telegramService.answerCallback(queryId, translationManager.getMessage(chatId, "changedLang"));
            telegramService.deleteMessage(chatId, messageId);
        }
    }

}
