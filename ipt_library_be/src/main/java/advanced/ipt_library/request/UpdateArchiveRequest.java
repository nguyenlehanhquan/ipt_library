package advanced.ipt_library.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateArchiveRequest {

    private String shelf;
    private String location;
}
