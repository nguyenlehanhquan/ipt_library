package advanced.ipt_library.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBookRequest {

    @NotBlank(message = "isbn is required")
    private String isbn;

    @NotBlank(message = "description is required")
    private String description;

    private String author;

}
