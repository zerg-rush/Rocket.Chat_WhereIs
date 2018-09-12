package pl.aszul.hot.rwb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.aszul.hot.rwb.config.AppConfig;

@Data
@AllArgsConstructor
public class RocketChatUser {

    private String nick;
    private String name;
    private String description;
    private String imgUrl;

    public Attachment toAttachment() {
        return new Attachment(
                AppConfig.ROCKET_CHAT_URL + "avatars/" + nick,
                "username:  " + name + "\n" +
                        "full name: " + name + "\n" +
                        "IP address: " + "123.234.45.56" + "\n" +
                        "MAC address: " + "12-34-56-78-90-12-34-56" + "\n",
                AppConfig.USER_AVATARS_URL + nick + ".jpg",
                "#FFFFFF"
        );
    }

}
