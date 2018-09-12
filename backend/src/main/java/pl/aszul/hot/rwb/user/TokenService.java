package pl.aszul.hot.rwb.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.aszul.hot.rwb.model.LoggedUser;
import pl.aszul.hot.rwb.model.Role;
import pl.aszul.hot.rwb.model.db.DbUser;
import pl.aszul.hot.rwb.rc.RocketChatLoginResponse;

import java.util.Date;
import java.util.UUID;

import static pl.aszul.hot.rwb.config.AppConfig.ROCKET_CHAT_URL;
import static pl.aszul.hot.rwb.config.AppConfig.USERNAME_CLAIM_NAME;
import static pl.aszul.hot.rwb.config.AppConfig.SCOPES_CLAIM_NAME;

@Service
@Log4j2
class TokenService {

    private final int expirationTimeInMinutes;
    private final String secret;

    @Autowired
    TokenService(@Value("${jwt.expiration-time-in-minutes}") int expirationTimeInMinutes,
                 @Value("${jwt.secret}") String secret) {
        this.expirationTimeInMinutes = expirationTimeInMinutes;
        this.secret = secret;
    }

    String exchangePasswordForToken(final LoggedUser user, final String password) {
        if (user == null || !verifyCredentials(user, password)) {
            throw new BadCredentialsException("Incorrect email and/or password");
        }
        var iat = new Date();

        return Jwts.builder()
                .setIssuer("WhereIs")
                .setSubject(user.getUsername())
                .setId(UUID.randomUUID().toString())
                .claim(USERNAME_CLAIM_NAME, user.getUsername())
                .claim(SCOPES_CLAIM_NAME, user.getRoles())
                .setIssuedAt(iat)
                .setExpiration(DateUtils.addMinutes(iat, expirationTimeInMinutes))
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.decode(secret))
                .compact();
    }

    Date getExpirationDateSeconds() {
        //return new Date(Instant.now().plusMillis(expirationTimeInMinutes).toEpochMilli());
        return DateUtils.addMinutes(new Date(), expirationTimeInMinutes);
    }

    private boolean verifyCredentials(LoggedUser user, final String password) {
        final HttpHeaders requestHeaders = new HttpHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("username", user.getUsername());
        requestBody.add("password", password);

        var requestEntity = new HttpEntity<Object>(requestBody, requestHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RocketChatLoginResponse> responseEntity = null;
        var result = true;
        try {
            responseEntity = restTemplate.exchange(ROCKET_CHAT_URL + "api/v1/login",
                    HttpMethod.POST, requestEntity, RocketChatLoginResponse.class);
        } catch (HttpClientErrorException e) {
            result = false;
        }

        result = result && (responseEntity.getStatusCode() == HttpStatus.OK);

        if (result) {
            var responseRolesList = responseEntity.getBody().getData().getMe().getRoles();
            log.info("responseRolesList = " + responseRolesList.toString());
            var loggedUserRolesSet = responseRolesList.stream().toArray(String[]::new);
            log.info("loggedUserRolesSet = " + loggedUserRolesSet.toString());
            log.info(Role.isAuthorizedForManagement(loggedUserRolesSet));
            if (Role.isAuthorizedForManagement(loggedUserRolesSet)) {
                user.setRoles(loggedUserRolesSet);
            } else {
                log.warn(String.format("User %s provided correct credentials but is not authorized for WhereIs management!!!", user.getUsername()));
                result = false;
            }
        }

        if (result) {
            log.info(String.format("User %s successfully logged in", user.getUsername()));
        }
        return result;
    }

}
