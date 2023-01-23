package com.shbt.backendgallery.service;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private String secretKey = "ateisjfkdsflk423!@%$4fd23fdsdtdfgdf5sdffdghd55erss";

    @Override
    public String getToken(String key, Object value) {

        Date expTime = new Date();
        expTime.setTime(expTime.getTime() + 1000 * 60 * 10);    // 시간(10분)
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);  // 비밀키 byte 처리
        Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());  // HS256 알고리즘으로 암호화

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expTime)
                .signWith(signKey, SignatureAlgorithm.HS256);

        return builder.compact();
    }

    @Override
    public Claims getClaims(String token) {

        if(token != null && !"".equals(token)) {
            try {
                byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);  // 비밀키 byte 처리
                Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());  // HS256 알고리즘으로 암호화

                return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();

            } catch(ExpiredJwtException e) {
                // 만료됨
            } catch (JwtException e) {
                // 유효하지 않음
            }
        }

        return null;
    }

    @Override
    public boolean isValid(String token) {
        return this.getClaims(token) != null;
    }

    @Override
    public int getId(String token) {

        Claims claims = this.getClaims(token);

        if(claims != null) {
            return Integer.parseInt(claims.get("id").toString());
        }

        return 0;
    }
}
