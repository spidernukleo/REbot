package nukleo.REbot.service;


import lombok.AllArgsConstructor;
import nukleo.REbot.util.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@Service
public class TelegramService {

    @Autowired
    private BotConfig botConfig;

    private final RestTemplate restTemplate = new RestTemplate();


    public void sendMessage(Long chatId, String text){
        Map<String, Object> req = Map.of(
                "chat_id", chatId,
                "text", text,
                "parse_mode", "HTML"
        );
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

}
