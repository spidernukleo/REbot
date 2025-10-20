package nukleo.REbot.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import nukleo.REbot.model.ChatMember;
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


    public void sendPhoto(Long chatid, String caption, String fileId){
        if(fileId==null || fileId.isEmpty()) sendMessage(chatid, caption);
        else{
            Map<String, Object> req = new HashMap<>();
            req.put("chat_id", chatid);
            req.put("photo", fileId);
            req.put("parse_mode", "HTML");
            if (caption != null) req.put("caption", caption);
            try{
                new RestTemplate().postForObject(botConfig.getTgUrl()+"/sendPhoto", req, String.class);
            }catch(Exception e){
                throw new RuntimeException("Error sending photo "+chatid +": "+e.getMessage());
            }
        }
    }


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

    public void editMessage(Long chatId, Integer messageId, String newText){
        this.editMessage(chatId, messageId, newText, null);
    }

    public void editMessage(Long chatId, Integer messageId, String newText, InlineKeyboardMarkup newMenu) {
        Map<String, Object> req = new HashMap<>();
        req.put("chat_id", chatId);
        req.put("message_id", messageId);
        req.put("text", newText);
        req.put("parse_mode", "HTML");
        if (newMenu != null) req.put("reply_markup", newMenu);

        try{
            new RestTemplate().postForObject(botConfig.getTgUrl()+"/editMessageText", req, String.class);
        }catch(Exception e){
            throw new RuntimeException("Error editing message "+chatId +": "+e.getMessage());
        }
    }

    private ChatMember getChatMember(Long chatId, Long userId){
        Map<String, Object> req = Map.of(
                "chat_id", chatId,
                "user_id", userId
        );
        try{
            Map<String, Object> response = new RestTemplate().postForObject(botConfig.getTgUrl() + "/getChatMember", req, Map.class);
            if (response == null || !(Boolean) response.get("ok")) return null;
            return new ObjectMapper().convertValue(response.get("result"), ChatMember.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isUserAdmin(Long chatId, Long userId) {
        ChatMember member = getChatMember(chatId, userId);
        if (member == null) return false;
        String status = member.getStatus();
        return "administrator".equals(status) || "creator".equals(status);
    }

}
