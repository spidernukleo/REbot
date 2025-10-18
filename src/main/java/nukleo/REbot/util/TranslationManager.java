package nukleo.REbot.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import nukleo.REbot.model.GroupLanguage;
import nukleo.REbot.repository.LanguageRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Data
public class TranslationManager {

    private final LanguageRepository languageRepository;

    private final Map<Long, String> groupLanguages = new ConcurrentHashMap<>();

    private final Map<String, Map<String, String>> translations = new HashMap<>();

    public void createLanguageTable(){
        languageRepository.createLanguageTable();
    }

    public void loadTranslations() {
        ObjectMapper mapper = new ObjectMapper();
        for (String lang : new String[]{"en", "it"}) { //add lang here
            try (InputStream is = getClass().getResourceAsStream("/langs/" + lang + ".json")) {
                if (is != null) {
                    Map<String, String> phrases = mapper.readValue(is, new TypeReference<>() {});
                    translations.put(lang, phrases);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getMessage(long chatId, String key){
        String langCode = this.getLanguage(chatId);
        return translations.get(langCode).getOrDefault(key, key);
    }

    public String getLanguage(long chatId){
        return this.groupLanguages.getOrDefault(chatId,"it");
    }

    public void loadAllGroupsLanguages(){
        List<GroupLanguage> langs = languageRepository.getAllLanguages();
        for(GroupLanguage lang : langs){
            groupLanguages.put(lang.getChatId(), lang.getActiveLangCode());
        }
    }

    public void addChatLanguage(Long chatId){
        languageRepository.addChat(chatId);
        if(!groupLanguages.containsKey(chatId)){
            groupLanguages.put(chatId, "it");
        }
    }

    public void setLanguage(Long chatId, String languageCode){
        if(!languageCode.equals(groupLanguages.get(chatId))){
            languageRepository.updateLanguage(chatId, languageCode);
            groupLanguages.put(chatId, languageCode);
        }
    }

}
