package pl.aszul.hot.rwb.rc;

import lombok.Data;

@Data
public class RocketChatToken {

    public String authToken;
    public String userId;
    public RocketChatUserDetails me;

}
