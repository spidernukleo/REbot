package nukleo.REbot.util;


import lombok.Data;
import nukleo.REbot.model.GroupCommand;
import nukleo.REbot.model.GroupLanguage;
import nukleo.REbot.repository.CommandsRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class CommandsManager {

    private final CommandsRepository commandsRepository;

    private final Map<Long, List<String>> groupCommands = new HashMap<>();

    public void createCommandsTable(){
        commandsRepository.createCommandsTable();
    }

    public void loadAllGroupCommands(){
        List<GroupCommand> commands = commandsRepository.getAllGroupCommands();
        for(GroupCommand cmd : commands){
            groupCommands.computeIfAbsent(cmd.getChatId(), k -> new ArrayList<>()).add(cmd.getCommand());
        }
    }

    public boolean addChatCommand(Long chatId, String command){
        List<String> commands = groupCommands.computeIfAbsent(chatId, k -> new ArrayList<>());
        if(commands.size()>=5) return false;
        if(commands.contains(command)) return true;
        commandsRepository.addCommand(chatId, command);
        commands.add(command);
        return true;
    }

    private Integer countCommands(Long chatid){
        return groupCommands.getOrDefault(chatid, List.of()).size();
    }

    public List<String> getGroupCommands(Long chatid){
        return groupCommands.getOrDefault(chatid, List.of());
    }

    public void removeChatCommand(Long chatid, String command){
        List<String> cmds = groupCommands.get(chatid);
        if (cmds != null) {
            cmds.remove(command);
            if (cmds.isEmpty()) groupCommands.remove(chatid);
        }
        commandsRepository.removeCommand(chatid, command);
    }


}
