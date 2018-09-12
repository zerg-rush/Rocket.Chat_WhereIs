package pl.aszul.hot.rwb.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.aszul.hot.rwb.model.Attachment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RocketChatResponse {

    private String icon_emoji;
    private String text;
    private Attachment[] attachments;

}
