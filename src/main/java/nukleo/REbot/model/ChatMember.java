package nukleo.REbot.model;

import lombok.Data;

@Data
public class ChatMember {
        private ChatMemberUpdate.User user;
        private String status;
}
