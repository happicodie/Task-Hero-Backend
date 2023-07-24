package com.fivesigma.backend.util;

import com.fivesigma.backend.dto.UserDetailDTO;
import com.fivesigma.backend.service.ITokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Andy
 * @date 2022/10/8
 */

@Component
public class JWTUtil {

    private static final int EXPIRE = 60 * 60 * 6 * 1000;  //token expire after 6 hour
    private static final String SECRET_KEY = "secret";

    @Autowired
    private ITokenService tokenService;

    //todo: generate token based on Authentication --> UserDetail
    public String generateToken(UserDetailDTO userDetailDTO){
        //todo: set token payload for user validation
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("user_id", userDetailDTO.getUser_id());
        //todo: set token expire time
        Date now = new Date(System.currentTimeMillis());
        Date expire_time = new Date(System.currentTimeMillis() + EXPIRE);

        String token =
                Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetailDTO.getUsername())
                    .setIssuedAt(now)
                    .setExpiration(expire_time)
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    .compact();
        return "Bearer " + token;
    }

    public Claims parseToken(String token){
        Claims claims = null;
        claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    public boolean isExpired(String token){
        Date expiredDate = parseToken(token).getExpiration();
        return expiredDate.before(new Date());
    }

    public String userInToken(String token){
        Claims claims = parseToken(token);
        return claims == null ? null : claims.getSubject();
    }
    public String userIdInToken(String token){
        Claims claims = parseToken(token);
        return claims == null ? null : (String) claims.get("user_id");
    }

    public boolean isLogout(String token){
        return tokenService.inBlackList(token);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String username = userInToken(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    public String tryGetId(String raw_token){
        String token_id = null;
        try {
            //try to convert token, if not a token, it must be a user_id
            raw_token = raw_token.replace("Bearer ", "");
            token_id = userIdInToken(raw_token);
        } catch (Exception e) {
            token_id = raw_token;
        }
        return token_id;
    }
}
