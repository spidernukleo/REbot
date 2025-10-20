package nukleo.REbot.model;

import lombok.Data;

@Data
public class Photo {
    private String file_id;
    private String file_unique_id;
    private int width;
    private int height;
    private int file_size;

    public Photo(String file_id){
        this.file_id = file_id;
    }
}
