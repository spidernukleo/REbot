package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.ChatMemberUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ChatMemberService {
    @Autowired
    private JdbcTemplate jdbc;
    private final TelegramService telegramService;

    public void handleChat(ChatMemberUpdate member) {
        if(member.getChat().getType().equals("channel")){
            telegramService.leaveChat(member.getChat().getId());
            return;
        }
        if (member.getNew_chat_member().getStatus().equals("member")) {
            telegramService.sendMessage(member.getChat().getId(), "entrato con successo");
        }
    }
}
