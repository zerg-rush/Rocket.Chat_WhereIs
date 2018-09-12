package pl.aszul.hot.rwb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetworkServiceResponse {

    private String id;
    private String fullname;
    private List<UserLocation> locations;

}
