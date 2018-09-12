package pl.aszul.hot.rwb.rc;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;

import static pl.aszul.hot.rwb.config.AppConfig.ROCKET_CHAT_URL;

@Log4j2
public class User {

    private String userId;

    public static boolean checkLogin(String user, String password) {

        final HttpHeaders requestHeaders = new HttpHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("username", user);
        requestBody.add("password", password);

        var requestEntity = new HttpEntity<Object>(requestBody, requestHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RocketChatLoginResponse> responseEntity = null;
        var result = true;
        try {
            responseEntity = restTemplate.exchange(ROCKET_CHAT_URL + "api/v1/login", HttpMethod.POST, requestEntity, RocketChatLoginResponse.class);
        } catch (HttpClientErrorException e) {
            result = false;
        }

        result = result && responseEntity.getStatusCode() == HttpStatus.OK;

        if (result) {
            //roles
        }

        return result;
    }

}
