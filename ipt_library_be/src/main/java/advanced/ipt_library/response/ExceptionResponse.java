package advanced.ipt_library.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {
    private String message;
    private String errorCode;

    public ExceptionResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

}
