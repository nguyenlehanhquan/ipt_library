package advanced.ipt_library.security;

import advanced.ipt_library.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {

    // load thông tin khóa và thời gian hết hạn
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;


    // tạo token từ username
    public String getJWTToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact(); // chuyển thành string để trả về , kiểu giống toString
    }

    // lấy username từ token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // check xem token hết hạn chưa?
    public boolean isExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date()); // nếu thời gian hết hạn trước thời gian hiện tại thì trả về true (hết hạn)
    }

    // check token hợp lệ không?
    public boolean validateToken(String token, UserDetails userLogin) {
        String username = getUsernameFromToken(token);
        return username.equals(userLogin.getUsername()) && !isExpired(token);
    }
}
