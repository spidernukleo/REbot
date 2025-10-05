package nukleo.REbot.model;

import lombok.Data;

@Data
public class ChatMemberUpdate {
    private Chat chat;
    private User from;
    private ChatMember oldChatMember;
    private ChatMember newChatMember;
}
