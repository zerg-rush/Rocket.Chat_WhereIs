package pl.aszul.hot.rwb.model.db;

import lombok.*;
import pl.aszul.hot.rwb.model.Attachment;

import javax.persistence.*;

import static pl.aszul.hot.rwb.config.AppConfig.USER_AVATARS_URL;

@Entity
@Table(name = "USERS") // Note: USER is SQL reserved keyword
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DbUser {

    @Id
    private String username;
    private String fullname;
    private String description;
    private String avatarUrl;
    //private String[] roles;

    public boolean hasAvatar() {
        return !this.avatarUrl.isEmpty();
    }

    public Attachment toAttachment() {
        return new Attachment(
                "",
                "username:  " + username + "\n" +
                        "full name: " + fullname + "\n",
                USER_AVATARS_URL + username + ".jpg",
                "#FFFFFF"
        );
    }

}
