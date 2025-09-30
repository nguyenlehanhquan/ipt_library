package advanced.ipt_library.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateArchiveRequest {

    @NotBlank(message = "archive type is required")
    private String archiveType;
    // có 3 loại EnumType.String

    @NotBlank(message = "shelf is required")
    private String shelf;

    @NotBlank(message = "location is required")
    private String location;
}
