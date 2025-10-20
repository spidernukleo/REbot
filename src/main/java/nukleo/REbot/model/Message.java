package nukleo.REbot.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class Message {
    private Integer message_id;
    private User from;
    private Chat chat;
    private Long date;
    private String text;
    private Message reply_to_message;
    private List<Photo> photo = new ArrayList<>();
    private String caption;


    public Photo getLargestPhoto() {
        return photo.stream()
                .max(Comparator.comparingInt(Photo::getFile_size))
                .orElse(null);
    }
}
