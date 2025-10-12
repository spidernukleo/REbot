package nukleo.REbot.repository;


import nukleo.REbot.model.GroupLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageRepository {
    @Autowired
    private JdbcTemplate jdbc;

    public void addChat(Long chatId){
        try{
            String sql = "INSERT INTO languages (chatId, activeLangCode) VALUES (?, ?) " +
                    "ON CONFLICT (chatId) DO NOTHING";
            jdbc.update(sql, chatId, "it");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void createLanguageTable(){
        try{
            jdbc.execute(" CREATE TABLE IF NOT EXISTS languages ( chatId BIGINT NOT NULL PRIMARY KEY, activeLangCode VARCHAR(5) NOT NULL) ");
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public List<GroupLanguage> getAllLanguages(){
        try{
            String sql = "SELECT chatId, activeLangCode FROM languages";
            return jdbc.query(sql, (rs, rowNum) -> new GroupLanguage(
                    rs.getLong("chatId"),
                    rs.getString("activeLangCode")
            ));
        }
        catch(Exception e){
            e.printStackTrace();
            return List.of();
        }
    }

    public void updateLanguage(Long chatId, String languageCode){
        try{
            String sql = "UPDATE languages SET activeLangCode = ? WHERE chatId = ? ";
            jdbc.update(sql, languageCode, chatId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
