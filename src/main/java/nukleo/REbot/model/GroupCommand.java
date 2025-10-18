package nukleo.REbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupCommand {
    private Long chatId;
    private String command;
}
