package pl.aszul.hot.rwb.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.aszul.hot.rwb.model.WhereIsMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static pl.aszul.hot.rwb.config.AppConfig.USER_AVATARS_URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RocketChatRequest {

    @NotEmpty
    @ApiModelProperty(value = "Represents command text as entered by Rocket.Chat user", required = true, position = 1,
            example = "jan")
    private String command;

    @JsonProperty("mode")
    @NotEmpty
    @ApiModelProperty(value = "Represents requested work mode (USER, IP, MAC or NOT_FOUND)", required = true, position = 2,
            example = "USER")
    private WhereIsMode mode;

    @NotEmpty
    @ApiModelProperty(value = "Represents id value (username, IP or MAC address)", required = true, position = 3,
            example = "jan")
    private String id;

    @NotNull
    @ApiModelProperty(value = "Represents existence of user with requested username", required = true, position = 4,
            example = "true")
    private Boolean exists;

    @NotNull
    @ApiModelProperty(value = "Represents full name of user", required = true, position = 5,
            example = "Jan Kowalski")
    private String fullname;

    @ApiModelProperty(value = "Represents url for user avatar", required = true, position = 6,
            example = USER_AVATARS_URL + "jan.jpg")
    private String avatarUrl;

    @ApiModelProperty(value = "Represents user roles array (i.e. [\"admin\", \"whereis-admin\"])", required = true,
            position = 7, dataType = "List", example = "admin,whereis-admin,whereis-details")
    private String[] senderRoles;

}
