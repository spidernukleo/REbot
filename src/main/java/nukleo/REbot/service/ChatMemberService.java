package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.ChatMemberUpdate;
import nukleo.REbot.repository.CoreRepository;
import nukleo.REbot.util.CommandsManager;
import nukleo.REbot.util.TranslationManager;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ChatMemberService {

    private final TelegramService telegramService;
    private final LogService logService;
    private final TranslationManager translationManager;
    private final CommandsManager commandsManager;

    public void handleChat(ChatMemberUpdate member) {
        String status = member.getNew_chat_member().getStatus();
        Long chatId = member.getChat().getId();
        if(status.equals("left") || status.equals("kicked")) return; //continue only if added

        if(member.getChat().getType().equals("channel")){
            telegramService.leaveChat(chatId); //leave if channel
            return;
        }

        translationManager.addChatLanguage(chatId);
        commandsManager.firstTimeAdd(chatId);
        telegramService.sendMessage(chatId, translationManager.getMessage(chatId, "welcome"));
        logService.logAddChat(member);
    }
}
