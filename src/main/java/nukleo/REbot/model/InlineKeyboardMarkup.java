package nukleo.REbot.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InlineKeyboardMarkup {
    private List<List<InlineKeyboardButton>> inline_keyboard;

    public static InlineKeyboardMarkup genMenu(InlineKeyboardButton[]... rows) {
        List<List<InlineKeyboardButton>> layout = Arrays.stream(rows)
                .map(Arrays::asList)
                .toList();
        return new InlineKeyboardMarkup(layout);
    }


}
