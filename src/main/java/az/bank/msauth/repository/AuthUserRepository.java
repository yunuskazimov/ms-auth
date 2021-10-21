package az.bank.msauth.repository;

import az.bank.msauth.dao.AuthUserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthUserRepository extends CrudRepository<AuthUserEntity,Long> {
    Optional<AuthUserEntity> findByUsername(String username);
}
