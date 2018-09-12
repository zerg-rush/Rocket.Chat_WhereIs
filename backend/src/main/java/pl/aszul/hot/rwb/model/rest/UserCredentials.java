package pl.aszul.hot.rwb.model.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "DbUser Credentials")
public class UserCredentials {

    @NotEmpty
    @ApiModelProperty(value = "Represents user's username", example = "jan", required = true, position = 1)
    private final String username;

    @NotEmpty
    @ApiModelProperty(value = "Represents user's password", example = "123jan", required = true, position = 2)
    private final String password;

    @JsonCreator
    public UserCredentials(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

}
