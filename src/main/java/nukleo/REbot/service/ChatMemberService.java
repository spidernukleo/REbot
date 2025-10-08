package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.ChatMemberUpdate;
import nukleo.REbot.repository.DatabaseRepository;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ChatMemberService {

    private DatabaseRepository databaseRepository;
    private final TelegramService telegramService;

    public void handleChat(ChatMemberUpdate member) {
        String status = member.getNew_chat_member().getStatus();
        if(status.equals("left") || status.equals("kicked")) return; //continue only if added

        if(member.getChat().getType().equals("channel")){
            telegramService.leaveChat(member.getChat().getId()); //leave if channel
            return;
        }

        databaseRepository.addTable(member.getChat().getId());
        telegramService.sendMessage(1780793442L, "✅ Nuova aggiunta gruppo: "+member.getChat().getTitle()+"\nAggiunto da: <a href='tg://user?id="+member.getFrom().getId()+"'>"+member.getFrom().getFirst_name()+"</a>");
        telegramService.sendMessage(member.getChat().getId(), "Salve, scrivi daniele per diventare il re daniele del giorno");
    }
}
