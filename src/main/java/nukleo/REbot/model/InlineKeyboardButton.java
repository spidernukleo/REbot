package nukleo.REbot.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InlineKeyboardButton {
    private String text;
    private String url;
    private String callback_data;

    public static InlineKeyboardButton cb(String text, String callback) {
        return new InlineKeyboardButton(text, null, callback);
    }

    public static InlineKeyboardButton link(String text, String url) {
        return new InlineKeyboardButton(text, url, null);
    }
}
