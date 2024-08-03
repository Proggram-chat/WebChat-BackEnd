package sanity.nil.webchat.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenHelper {

    @Value("${application.security.jwt.key}")
    private transient String privateKey;

    @Value("${application.security.jwt.ttl-min}")
    private Integer ttlMinutes;

    private SecretKey getSecretKey() {
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        return Keys.hmacShaKeyFor(privateKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Map<String, Object> additionalClaims) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("exp", Date.from(ZonedDateTime.now().plusMinutes(ttlMinutes).toInstant()));
        if (!CollectionUtils.isEmpty(additionalClaims)) {
            claims.putAll(additionalClaims);
        }
        return Jwts.builder()
                .claims(claims)
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }
}
