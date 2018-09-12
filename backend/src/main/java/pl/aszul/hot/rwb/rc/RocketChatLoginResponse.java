package pl.aszul.hot.rwb.rc;

import lombok.Data;

@Data
public class RocketChatLoginResponse {

    private String status;
    private RocketChatToken data;

}
