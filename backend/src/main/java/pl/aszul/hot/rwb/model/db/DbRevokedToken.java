package pl.aszul.hot.rwb.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "REVOKED_TOKEN")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DbRevokedToken {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;

    private String username;
    private Date expirationDate;

    public DbRevokedToken(String username, Date expirationDate) {
        this.username = username;
        this.expirationDate = expirationDate;
    }

}
