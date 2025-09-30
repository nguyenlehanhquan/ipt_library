package advanced.ipt_library.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String username;
    private String tokenType = "Bearer ";


}
