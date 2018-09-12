package pl.aszul.hot.rwb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggedUser {

    private String username;
    private String fullname;
    private String description;
    private String[] roles;

}
