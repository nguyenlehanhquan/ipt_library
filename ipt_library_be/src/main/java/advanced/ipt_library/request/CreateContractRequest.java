package advanced.ipt_library.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateContractRequest {

    @NotBlank(message = "code is required")
    private String code;
}
