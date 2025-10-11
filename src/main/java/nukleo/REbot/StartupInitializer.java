package nukleo.REbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StartupInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\uD83D\uDE80 Bot startup logic running...");

        // ensure table exists
        jdbc.execute("""
            CREATE TABLE IF NOT EXISTS languages (
                chatId BIGINT NOT NULL PRIMARY KEY,
                activeLangCode VARCHAR(5) NOT NULL
            )
        """);


        System.out.println("✅ Startup complete!");
    }
}
