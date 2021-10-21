package az.bank.msauth.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Table(name = "active_refresh_token")
@AllArgsConstructor
public class ActiveRefreshTokenEntity {
    @Id
    private String uuid;

    public ActiveRefreshTokenEntity() {
        this.uuid = UUID.randomUUID().toString();
    }
}
