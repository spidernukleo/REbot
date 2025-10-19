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

    public void createPointsTable(){
        try{
            String sql = """
                CREATE TABLE IF NOT EXISTS chat_points (
                    chatId BIGINT NOT NULL,
                    userId BIGINT NOT NULL,
                    command VARCHAR(10) NOT NULL,
                    points BIGINT DEFAULT 0,
                    userFirstName TEXT,
                    PRIMARY KEY (chatId, userId, command),
                    FOREIGN KEY (chatId, command)
                        REFERENCES commands(chatId, command)
                        ON DELETE CASCADE)""";
            jdbc.execute(sql);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    public void incrementPoints(long chatId, long userId, String command, String userFirstName) {
        String sql = """
                INSERT INTO chat_points (chatId, userId, command, points, userFirstName)
                VALUES (?, ?, ?, 1, ?)
                ON CONFLICT (chatId, userId, command)
                DO UPDATE SET
                    points = chat_points.points + 1,
                    userFirstName = EXCLUDED.userFirstName
            """;
        try {
            jdbc.update(sql, chatId, userId, command, userFirstName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<TopRecord> getTopRecords(Long chatId, String command) {
        String sql = """
        SELECT userId, points, command, userFirstName
        FROM chat_points
        WHERE chatId = ? AND command = ?
        ORDER BY points DESC
        LIMIT 10
    """;

        try {
            return jdbc.query(sql,
                    new Object[]{chatId, command},
                    (rs, rowNum) -> {
                        TopRecord player = new TopRecord();
                        player.setUserId(rs.getLong("userId"));
                        player.setPoints(rs.getLong("points"));
                        player.setFirstName(rs.getString("userFirstName"));
                        player.setCommand(rs.getString("command"));
                        return player;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

}
