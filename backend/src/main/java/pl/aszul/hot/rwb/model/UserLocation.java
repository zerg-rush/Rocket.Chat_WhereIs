package pl.aszul.hot.rwb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocation {

    private String index;
    private LocationType type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActivity;

    @Enumerated(EnumType.STRING)
    @Column(length = 7)
    private StatusType status;

    @Range(min = 1, max = 10, message = "Signal strength must be in range 1-10")
    private String signalStrength;
    private String ip;
    private String mac;

}
