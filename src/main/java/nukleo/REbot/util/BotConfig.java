package nukleo.REbot.util;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


@Getter
@Configuration
public class BotConfig {

    private final String botToken;
    private final Long botId;
    private final Long logId;
    private final String tgUrl;

    public BotConfig(){
        this.botToken = System.getenv("TELEGRAM_BOT_TOKEN");
        if(this.botToken == null||this.botToken.isEmpty()||!this.botToken.contains(":")){
            throw new IllegalStateException("Invalid bot token");
        }


        String logIdStr = System.getenv("TELEGRAM_LOG_ID");
        this.logId = (logIdStr != null) ? Long.parseLong(logIdStr) : null;
        this.botId = Long.parseLong(botToken.split(":")[0]);
        this.tgUrl = "https://api.telegram.org/bot" + botToken;
    }

    @Bean
    public DataSource dataSource() {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true); //enable all foreign keys
        SQLiteDataSource ds = new SQLiteDataSource(config);
        ds.setUrl("jdbc:sqlite:database.db");
        return ds;
    }
}
