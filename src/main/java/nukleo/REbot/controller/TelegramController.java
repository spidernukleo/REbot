package nukleo.REbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import nukleo.REbot.model.ChatMemberUpdate;
import nukleo.REbot.service.ChatMemberService;
import nukleo.REbot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class TelegramController {
    @Autowired
    private ObjectMapper objectMapper;
    private final MessageService messageService;
    private final ChatMemberService chatMemberService;


    //TEST ENDPOINT FOR BROWSER TESTING
    @GetMapping("/test")
    public String test() {
        return "Bot is running!";
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/bot-webhook")
    public void onTelegramUpdate(@RequestBody Map<String, Object> update) {
        if(update.containsKey("message")){
            Map<String, Object> message = (Map<String, Object>) update.get("message");
            messageService.handleMessage(message);
        }
        else if(update.containsKey("my_chat_member")){
            ChatMemberUpdate member = objectMapper.convertValue(
                    update.get("my_chat_member"),
                    ChatMemberUpdate.class
            );
            chatMemberService.handleChat(member);
        }
    }

}
