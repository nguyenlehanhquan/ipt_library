package advanced.ipt_library.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "Access denied");
        map.put("status", HttpServletResponse.SC_FORBIDDEN);
        map.put("error", accessDeniedException.getMessage());
        map.put("path", request.getRequestURI());
        map.put("abc", "xyz");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), map); // ghi giá trị này vào response trả về cho client
    }
}
