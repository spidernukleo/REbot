package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.ChatMemberUpdate;
import nukleo.REbot.repository.DatabaseRepository;
import nukleo.REbot.util.BotConfig;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogService {

    private final DatabaseRepository databaseRepository;
    private final TelegramService telegramService;
    private final BotConfig botConfig;



    public void logAddChat(ChatMemberUpdate member){
        telegramService.sendMessage(botConfig.getLogId(), "✅ New group added: "+member.getChat().getTitle()+"\nAdded by: <a href='tg://user?id="+member.getFrom().getId()+"'>"+member.getFrom().getFirst_name()+"</a>");
    }

}
