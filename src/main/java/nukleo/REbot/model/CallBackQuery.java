package nukleo.REbot.model;

import lombok.Data;

@Data
public class CallBackQuery {
    private String id;
    private User from;
    private Message message;
    private String data;
}
