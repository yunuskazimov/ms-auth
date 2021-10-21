package az.bank.msauth.repository;

import az.bank.msauth.dao.ActiveRefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ActiveRefreshTokenRepository extends CrudRepository<ActiveRefreshTokenEntity,String> {
    Optional<ActiveRefreshTokenEntity> findByUuid(String uuid);
}
