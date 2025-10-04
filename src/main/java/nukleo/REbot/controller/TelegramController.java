package nukleo.REbot.controller;

import lombok.AllArgsConstructor;
import nukleo.REbot.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class TelegramController {

    private final MessageService messageService;


    //TEST ENDPOINT FOR BROWSER TESTING
    @GetMapping("/test")
    public String test() {
        return "Bot is running!";
    }

    @PostMapping("/bot-webhook")
    public void onTelegramUpdate(@RequestBody Map<String, Object> update) {
        System.out.println("Arrivato update da tg");
        if(update.containsKey("message")){
            System.out.println("Update contiene message");
            //messageService.cazzoPalle();
        }
    }

}
