package advanced.ipt_library.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException { // xử lý thông báo khi gặp lỗi 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String, Object> map = new HashMap<>();
        map.put("message", "Authentication failed: " + authException.getMessage());
        map.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        map.put("error", authException.getMessage());
        map.put("path", request.getRequestURI());
        map.put("abc", "xyz");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), map); // ghi giá trị này vào response trả về cho client
    }
}
