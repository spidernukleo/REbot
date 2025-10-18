package nukleo.REbot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
        private long id;
        private String first_name;
        private Boolean is_bot;
}
