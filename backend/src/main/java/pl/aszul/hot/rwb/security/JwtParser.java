package pl.aszul.hot.rwb.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
class JwtParser {

    private static final String INVALID_TOKEN = "Invalid token";
    private static final String INVALID_TOKEN_SIGNATURE = "Invalid token signature";
    private static final String TOKEN_EXPIRED = "Expired token";

    private final String secret;

    @Autowired
    public JwtParser(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    static UUID getTokenId(final Claims claims) {
        return UUID.fromString(claims.getId());
    }

    static String getUsername(final Claims claims) {
        return claims.getSubject();
    }

    static String getFullname(final Claims claims) {
        return claims.get("fullname", String.class);
    }

    static List getScopes(final Claims claims) {
        return claims.get("scopes", List.class);
    }

    static Date getExpirationDate(final Claims claims) {
        return claims.getExpiration();
    }

    Claims parse(final String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(TextCodec.BASE64.decode(secret))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException(INVALID_TOKEN);
        } catch (SignatureException e) {
            throw new JwtAuthenticationException(INVALID_TOKEN_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException(TOKEN_EXPIRED);
        }
    }

}
