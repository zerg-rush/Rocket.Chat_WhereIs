package pl.aszul.hot.rwb.model.rest;

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
@ApiModel(value = "DbUser info and JWT token")
@Builder
public class Token {

    @ApiModelProperty(value = "Represents user's id", required = true, position = 1,
            example = "c5296892-347f-4b2e-b1c6-6faff971f767")
    private final UUID userId;

    @ApiModelProperty(value = "Represents user's email", required = true, position = 2, example = "unknown@gmail.com")
    private final String email;

    @ApiModelProperty(value = "Represents user's name", required = true, position = 3, example = "Jan")
    private final String name;

    @ApiModelProperty(value = "Represents user's surname", required = true, position = 4, example = "Kowalski")
    private final String surname;

    @ApiModelProperty(value = "Represents user's roles", required = true, position = 5, example = "USER")
    private final Set<Role> roles;

    @ApiModelProperty(value = "Represents token expiration date (time in seconds)", required = true,
            position = 6, example = "1520031600")
    private final Date expirationDateSeconds;

    @ApiModelProperty(value = "Represents JWT token value", required = true, position = 7,
            example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkMDJlZjVkNS04MTQzLTRjZGMtODk3Yi1kOWJhYmViMzE5Y2UiLCJlbWFpbCI6I" +
                    "nVua25vd25AZ21haWwuY29tIiwic2NvcGVzIjpbIlVTRVIiXSwiaWF0IjoxNTI3ODAwODcxLCJleHAiOjE1Mjc4MDgwNzF9." +
                    "-TN-a8QRyPnmXdIBgNBE68pSw5UgDW7dARI8-A8xLXDNh0RAFflpY9OJ0N2Y9afksXvG_zgSdfCPEvV5rAlSrQ")
    private final String jwt;

}

