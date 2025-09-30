package advanced.ipt_library.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class BookResponse {

    private Integer id;
    private String isbn;
    private String description;
    private String author;

}
