package nukleo.REbot;

import lombok.AllArgsConstructor;
import nukleo.REbot.service.LogService;
import nukleo.REbot.util.CommandsManager;
import nukleo.REbot.util.TranslationManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StartupInitializer implements CommandLineRunner {

    private final TranslationManager translationManager;
    private final LogService logService;
    private final CommandsManager commandsManager;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\uD83D\uDE80 Bot startup logic running...");

        translationManager.createLanguageTable();
        translationManager.loadAllGroupsLanguages();
        translationManager.loadTranslations();

        commandsManager.createCommandsTable();
        commandsManager.loadAllGroupCommands();

        //logService.pingOnline();

        System.out.println("✅ Startup complete!");
    }
}
