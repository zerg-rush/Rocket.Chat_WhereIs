package pl.aszul.hot.rwb.model.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class UserContext {

    @ApiModelProperty(value = "Represents username", required = true, position = 1, example = "jan")
    private final String username;

    @ApiModelProperty(value = "Represents user's full name", required = true, position = 2, example = "Jan Kowalski")
    private final String fullname;

    @ApiModelProperty(value = "Represents user's roles (possible values: USER, ADMIN, WHEREIS, WHEREIS-DETAILS, WHEREIS-ADMIN)", required = true, position = 3, example = "USER")
    private final Set<GrantedAuthority> authorities;

    @ApiModelProperty(value = "Represents user's token expiration date", required = true, position = 4)
    private final Date expirationDate;

}
