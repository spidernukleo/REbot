package nukleo.REbot.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public boolean addTable(Long chatId){
        String tableName = "chat_"+chatId;
        try{
            jdbc.execute("CREATE TABLE IF NOT EXISTS \""+tableName+"\" (user_id BIGINT PRIMARY KEY, points BIGINT DEFAULT 0);");
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
