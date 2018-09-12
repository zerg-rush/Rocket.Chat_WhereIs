package pl.aszul.hot.rwb.model.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.aszul.hot.rwb.model.LocationType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationWrite {

    @NotEmpty
    @ApiModelProperty(value = "Represents location index number", required = true, position = 1, example = "WIFI_1234")
    private String index;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 4)
    @ApiModelProperty(value = "Represents network device type", required = true, position = 2, example = "WIFI")
    private LocationType type;

    @NotEmpty
    @ApiModelProperty(value = "Represents location short name", required = true, position = 3,
            example = "DaVinci conference room")
    private String name;

    @NotEmpty
    @ApiModelProperty(value = "Represents location description", required = true, position = 4,
            example = "basement, on the right wall near the printer area")
    private String description;

    @ApiModelProperty(value = "Represents model name of network device", required = true, position = 5,
            example = "AccessPoint Ubiquiti UAP-AC-LR UniFi AP, AC Long Range")
    private String device;

    @NotEmpty
    @ApiModelProperty(value = "Represents serial number of network device", required = true, position = 6,
            example = "SN12345W")
    private String sn;

}
