package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.Chat;
import nukleo.REbot.model.ChatMember;
import nukleo.REbot.model.ChatMemberUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@AllArgsConstructor
public class ChatMemberService {
    @Autowired
    private JdbcTemplate jdbc;
    private final TelegramService telegramService;

    @SuppressWarnings("unchecked")
    public void handleChat(ChatMemberUpdate member) {
        ChatMember old = member.getOldChatMember();
        ChatMember newM =  member.getNewChatMember();
    }
}
