package org.family.hihishop.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.family.hihishop.model.User;
import org.family.hihishop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final UserRepository userRepository;

    @Value(("${jwt.expiration}"))
    private  int expiration;
    @Value("${jwt.secret}")
    private String secret;
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user){
        Map<String , Object> clams = new HashMap<String , Object>();
        clams.put("phoneNumber", user.getPhoneNumber());
        try {
            return Jwts.builder()
                    .setClaims(clams)
                    .setSubject(user.getPhoneNumber())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + expiration))
                    .signWith(getSecretKey())
                    .compact();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token đã hết hạn: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Token không được hỗ trợ: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Token không hợp lệ: " + e.getMessage());
        } catch (SecurityException  e) {
            System.out.println("Chữ ký của token không hợp lệ: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Token không đúng định dạng hoặc trống: " + e.getMessage());
        } catch (JwtException e) {
            System.out.println("Lỗi khi xác minh JWT: " + e.getMessage());
        }
        return false;
    }

    private Claims getAllClaim(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = this.getAllClaim(token);
        return claimsTFunction.apply(claims);
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

}
