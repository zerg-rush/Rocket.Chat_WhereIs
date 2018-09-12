package pl.aszul.hot.rwb.model.rest;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserWrite {

    @NotEmpty
    @Size(max = 20, message = "DbUser's firstName contains too much characters, maximum is " +
            20 + ".")
    @ApiModelProperty(value = "Represents username", required = true, position = 1, example = "Jan")
    private String username;

    @NotEmpty
    @ApiModelProperty(value = "Represents username", required = true, position = 2, example = "Jan")
    private String fullname;

    @NotEmpty
    @ApiModelProperty(value = "Represents username", required = true, position = 3, example = "Jan")
    private String description;

}
