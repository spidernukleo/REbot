package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.ChatMemberUpdate;
import nukleo.REbot.repository.DatabaseRepository;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ChatMemberService {

    private final DatabaseRepository databaseRepository;
    private final TelegramService telegramService;
    private final LogService logService;

    public void handleChat(ChatMemberUpdate member) {
        String status = member.getNew_chat_member().getStatus();
        if(status.equals("left") || status.equals("kicked")) return; //continue only if added

        if(!member.getChat().getType().equals("supergroup")){
            telegramService.leaveChat(member.getChat().getId()); //leave if channel or group(non supergroup)
            return;
        }

        databaseRepository.addTable(member.getChat().getId());
        logService.logAddChat(member);
        telegramService.sendMessage(member.getChat().getId(), "Salve, scrivi daniele per diventare il re daniele del giorno");
    }
}
