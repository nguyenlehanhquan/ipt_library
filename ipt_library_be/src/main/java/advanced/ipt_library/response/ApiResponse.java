package advanced.ipt_library.response;

import lombok.Data;

@Data
public class ApiResponse {
    private String message;
    private Object data;
    private String errorCode;

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String message, Object data, String errorCode) {
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
    }
}
