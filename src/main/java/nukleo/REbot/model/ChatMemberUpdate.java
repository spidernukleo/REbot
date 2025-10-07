package nukleo.REbot.model;

import lombok.Data;

@Data
public class ChatMemberUpdate {
    private Chat chat;
    private User from;
    private Long date;
    private ChatMember old_chat_member;
    private ChatMember new_chat_member;
}
