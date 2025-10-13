package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.model.InlineKeyboardButton;
import nukleo.REbot.model.InlineKeyboardMarkup;
import nukleo.REbot.util.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramService {

    @Autowired
    private BotConfig botConfig;

    private final RestTemplate restTemplate = new RestTemplate();


    public void sendMessage(Long chatId, String text){
        this.sendMessage(chatId, text, null);
    }

    public void sendMessage(Long chatId, String text, InlineKeyboardMarkup menu) {
        Map<String, Object> req = new HashMap<>();
        req.put("chat_id", chatId);
        req.put("text", text);
        req.put("parse_mode", "HTML");
        if (menu != null) req.put("reply_markup", menu);

        try{
            new RestTemplate().postForObject(botConfig.getTgUrl()+"/sendMessage", req, String.class);
        }catch(Exception e){
            throw new RuntimeException("Error sending message "+chatId +": "+e.getMessage());
        }
    }

    public void leaveChat(Long chatId){
        Map<String, Object> req = Map.of("chat_id", chatId);
        try{
            new RestTemplate().postForObject(botConfig.getTgUrl()+"/leaveChat", req, String.class);
        }
        catch(Exception ignored){}
    }

    public void answerCallback(String callbackId, String text) {
        Map<String, Object> req = Map.of(
                "callback_query_id", callbackId,
                "text", text,
                "show_alert", false
        );
        try {
            new RestTemplate().postForObject(botConfig.getTgUrl() + "/answerCallbackQuery", req, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(Long chatId, Integer messageId){
        Map<String, Object> req = Map.of(
                "chat_id", chatId,
                "message_id", messageId
        );

        try {
            new RestTemplate().postForObject(botConfig.getTgUrl() + "/deleteMessage", req, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
