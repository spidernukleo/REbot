package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.ChatMemberUpdate;
import nukleo.REbot.repository.CoreRepository;
import nukleo.REbot.repository.LanguageRepository;
import nukleo.REbot.util.TranslationManager;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ChatMemberService {

    private final CoreRepository coreRepository;
    private final TelegramService telegramService;
    private final LogService logService;
    private final TranslationManager translationManager;

    public void handleChat(ChatMemberUpdate member) {
        String status = member.getNew_chat_member().getStatus();
        Long chatId = member.getChat().getId();
        if(status.equals("left") || status.equals("kicked")) return; //continue only if added

        if(!member.getChat().getType().equals("supergroup")){
            telegramService.leaveChat(chatId); //leave if channel or group(non supergroup)
            return;
        }

        coreRepository.addTable(chatId);
        translationManager.addChatLanguage(chatId);
        logService.logAddChat(member);
        telegramService.sendMessage(chatId, "Salve, scrivi daniele per diventare il re daniele del giorno");
    }
}
