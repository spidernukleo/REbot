package nukleo.REbot.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import nukleo.REbot.model.GroupLanguage;
import nukleo.REbot.repository.LanguageRepository;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Data
public class TranslationManager {

    private final LanguageRepository languageRepository;

    private final Map<Long, String> groupLanguages = new ConcurrentHashMap<>();

    private final Map<String, Map<String, String>> translations = new HashMap<>();

    public String getLanguage(long chatId){
        return this.groupLanguages.getOrDefault(chatId,"it");
    }

    public void loadTranslations(String langCode){
        try{
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = this.getClass().getResourceAsStream("/"+langCode+".json");
            Map<String, String> phrases = mapper.readValue(is, new TypeReference<>() {});
            translations.put(langCode, phrases);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public String getMessage(long chatId, String key){
        String langCode = this.getLanguage(chatId);
        return translations.get(langCode).get(key);
    }


    public void createLanguageTable(){
        languageRepository.createLanguageTable();
    }

    public void loadAll(){
        List<GroupLanguage> langs = languageRepository.getAllLanguages();
        for(GroupLanguage lang : langs){
            groupLanguages.put(lang.getChatId(), lang.getActiveLangCode());
        }
    }

    public void addChatLanguage(Long chatId){
        languageRepository.addChat(chatId);
        groupLanguages.put(chatId, "it");
    }

    public void setLanguage(Long chatId, String languageCode){
        languageRepository.updateLanguage(chatId, languageCode);
        groupLanguages.put(chatId, languageCode);
    }

}
