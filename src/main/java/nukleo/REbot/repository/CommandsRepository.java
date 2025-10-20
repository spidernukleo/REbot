package nukleo.REbot.repository;

import nukleo.REbot.model.Command;
import nukleo.REbot.model.GroupCommand;
import nukleo.REbot.model.GroupLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommandsRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void createCommandsTable(){
        try{
            jdbc.execute(" CREATE TABLE IF NOT EXISTS commands ( chatId BIGINT NOT NULL, command VARCHAR(10) NOT NULL, file_id VARCHAR(255), UNIQUE (chatId, command)) ");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<GroupCommand> getAllGroupCommands(){
        try{
            String sql = "SELECT chatId, command, file_id FROM commands";
            return jdbc.query(sql, (rs, rowNum) -> new GroupCommand(
                    rs.getLong("chatId"),
                    new Command(rs.getString("command"), rs.getString("file_id"))
            ));
        }
        catch(Exception e){
            e.printStackTrace();
            return List.of();
        }
    }

    public void addCommand(Long chatId, Command command){
        try{
            String sql = "INSERT OR IGNORE INTO commands (chatId, command, file_id) VALUES(?, ?, ?)";
            jdbc.update(sql, chatId, command.getCmd(), command.getFile_id());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void removeCommand(Long chatId, String command){
        try {
            String sql = "DELETE FROM commands WHERE chatId = ? AND command = ?";
            jdbc.update(sql, chatId, command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
