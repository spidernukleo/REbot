package nukleo.REbot.model;

import lombok.Data;

@Data
public class Message {
    private User from;
    private Chat chat;
    private Long date;
    private String text;
}
