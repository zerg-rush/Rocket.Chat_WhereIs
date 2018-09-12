package pl.aszul.hot.rwb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

import static pl.aszul.hot.rwb.common.Util.durationFormat;
import static pl.aszul.hot.rwb.config.AppConfig.DATE_TIME_FORMATTER;
import static pl.aszul.hot.rwb.config.AppConfig.USER_AVATARS_URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {

    private String thumbnailUrl;
    private String text;
    private String imageUrl;
    private String color;

    public static Attachment statusAttachement(WhereIsMode mode, String id, String fullname, StatusType status, LocalDateTime lastActivity) {
        String thumbnailUrl;
        if (mode == WhereIsMode.USER) {
            thumbnailUrl = USER_AVATARS_URL + id + ".jpg";
        } else {
            thumbnailUrl = mode.getThumbnailUrl();
        }
        var label = mode.getLabel();

        return new Attachment(
                thumbnailUrl,
                label + id + "\n" +
                        (mode == WhereIsMode.USER ? "full name: " + fullname + "\n" : "") +
                        "status: " + status + "\n" +
                        "last activity: " + lastActivity.format(DATE_TIME_FORMATTER) + " (" +
                        durationFormat(Duration.between(lastActivity, LocalDateTime.now())) + " ago)\n",
                "",
                "#FFFFFF");
    }

}
