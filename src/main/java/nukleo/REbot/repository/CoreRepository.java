package nukleo.REbot.repository;


import nukleo.REbot.model.TopRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoreRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void addTable(Long chatId){
        String tableName = "chat_"+chatId;
        try{
            jdbc.execute("CREATE TABLE IF NOT EXISTS \""+tableName+"\" (userId BIGINT PRIMARY KEY, points BIGINT DEFAULT 0, userFirstName TEXT)");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void incrementPoints(Long chatId, Long userId, String userFirstName){
        String tableName = "chat_"+chatId;
        try{
            String sql = "INSERT INTO \"" + tableName + "\" (userId, points, userFirstName) " +
                    "VALUES (?, 1, ?) " +
                    "ON CONFLICT (userId) DO UPDATE SET points = \"" + tableName + "\".points + 1, " +
                    "userFirstName = EXCLUDED.userFirstName";
            jdbc.update(sql, userId, userFirstName);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<TopRecord> getTopRecords(Long chatId){
        String tableName = "chat_"+chatId;
        try{
            String sql = "SELECT userId, points, userFirstName FROM \""+tableName+"\" ORDER BY points DESC LIMIT 10";
            return jdbc.query(sql, (rs, rowNum) ->{
                TopRecord player = new TopRecord();
               player.setUserId(rs.getLong("userId"));
               player.setPoints(rs.getLong("points"));
               player.setFirstName(rs.getString("userFirstName"));
               return player;
            });
        }
        catch(Exception e){
            e.printStackTrace();
            return List.of();
        }
    }

}
