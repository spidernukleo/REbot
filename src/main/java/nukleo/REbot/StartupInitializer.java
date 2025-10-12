package nukleo.REbot;

import lombok.AllArgsConstructor;
import nukleo.REbot.util.TranslationManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StartupInitializer implements CommandLineRunner {

    private final TranslationManager translationManager;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\uD83D\uDE80 Bot startup logic running...");

        translationManager.createLanguageTable();
        translationManager.loadAll();
        translationManager.loadTranslations("it");
        translationManager.loadTranslations("en");

        System.out.println("✅ Startup complete!");
    }
}
