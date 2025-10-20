package nukleo.REbot.util;


import lombok.Data;
import nukleo.REbot.model.Command;
import nukleo.REbot.model.GroupCommand;
import nukleo.REbot.model.Photo;
import nukleo.REbot.repository.CommandsRepository;
import nukleo.REbot.repository.RedisRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class CommandsManager {

    private final CommandsRepository commandsRepository;

    private final Map<Long, List<Command>> groupCommands = new HashMap<>();

    private static final String FIRST_TIME_ADD_COMMAND = "primo";

    private static final String FIRST_TIME_ADD_PHOTO = "AgACAgQAAyEFAAS7GRUkAAIFLWj2iZ9dFqK8112NaJR5Ng_X52zsAAIeyjEbNFu5U9cHDDt6EIN2AQADAgADbQADNgQ";

    private final RedisRepository redisRepository;

    public void createCommandsTable(){
        commandsRepository.createCommandsTable();
    }

    public void loadAllGroupCommands(){
        List<GroupCommand> commands = commandsRepository.getAllGroupCommands();
        for(GroupCommand gc : commands){
            groupCommands.computeIfAbsent(gc.getChatId(), k -> new ArrayList<>()).add(gc.getCommand());
        }
    }

    public void firstTimeAdd(Long chatid){
        if(!groupCommands.containsKey(chatid)){
            this.addChatCommand(chatid, FIRST_TIME_ADD_COMMAND, new Photo(FIRST_TIME_ADD_PHOTO));
        }
    }

    public boolean addChatCommand(Long chatId, String text, Photo photo){
        List<Command> commands = groupCommands.computeIfAbsent(chatId, k -> new ArrayList<>());
        if(commands.size()>=5) return false;
        String cmdText = text.toLowerCase();
        if(commands.stream().anyMatch(c->c.getCmd().equals(cmdText)))return true; //if dosent already exist

        String fileId = photo!=null ? photo.getFile_id() : null;
        Command newCommand = new Command();
        newCommand.setCmd(cmdText);
        newCommand.setFile_id(fileId); //create new command obj and add it both map and db
        commandsRepository.addCommand(chatId, newCommand);
        commands.add(newCommand);
        return true;
    }

    public List<Command> getGroupCommands(Long chatid){
        return groupCommands.getOrDefault(chatid, List.of());
    }

    public void removeChatCommand(Long chatid, String command){
        List<Command> cmds = groupCommands.get(chatid);
        if (cmds != null) {
            cmds.removeIf(c -> c.getCmd().equalsIgnoreCase(command));
            if (cmds.isEmpty()) groupCommands.remove(chatid);
        }
        redisRepository.removeKing(chatid, command);
        commandsRepository.removeCommand(chatid, command);
    }


}
