package pl.aszul.hot.rwb.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import pl.aszul.hot.rwb.model.Role;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "User info and JWT token")
@Builder
public class Token {

    @ApiModelProperty(value = "Represents username", required = true, position = 1, example = "jan")
    private final String username;

    @ApiModelProperty(value = "Represents user's full name", required = true, position = 2, example = "Jan Kowalski")
    private final String fullname;

    @ApiModelProperty(value = "Represents user's roles", required = true, position = 3, example = "WHEREIS")
    private final Set<Role> roles;

    @ApiModelProperty(value = "Represents token expiration date (time in seconds)", required = true,
            position = 4, example = "1520031600")
    private final Date expirationDateSeconds;

    @ApiModelProperty(value = "Represents JWT token value", required = true, position = 5,
            example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkMDJlZjVkNS04MTQzLTRjZGMtODk3Yi1kOWJhYmViMzE5Y2UiLCJlbWFpbCI6I" +
                    "nVua25vd25AZ21haWwuY29tIiwic2NvcGVzIjpbIlVTRVIiXSwiaWF0IjoxNTI3ODAwODcxLCJleHAiOjE1Mjc4MDgwNzF9." +
                    "-TN-a8QRyPnmXdIBgNBE68pSw5UgDW7dARI8-A8xLXDNh0RAFflpY9OJ0N2Y9afksXvG_zgSdfCPEvV5rAlSrQ")
    private final String jwt;

}
