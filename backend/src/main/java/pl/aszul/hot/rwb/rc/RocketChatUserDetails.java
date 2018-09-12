package pl.aszul.hot.rwb.rc;

import java.util.List;

import lombok.Data;

@Data
public class RocketChatUserDetails {

    private String _id;
    private String name;
    private List<RocketChatEmail> emails = null;
    private String status;
    private String statusConnection;
    private String username;
    private Integer utcOffset;
    private Boolean active;
    private List<String> roles = null;
    private RocketChatSettings settings;

}
