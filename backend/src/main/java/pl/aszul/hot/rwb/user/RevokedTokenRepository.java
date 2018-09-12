package pl.aszul.hot.rwb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.aszul.hot.rwb.model.db.DbRevokedToken;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
interface RevokedTokenRepository extends JpaRepository<DbRevokedToken, UUID> {

//    DbRevokedToken findOneByUserIdAndExpirationDate(UUID userId, Date expirationDate);

    boolean existsById(UUID tokenId);

    List<DbRevokedToken> findAll();

    void delete(DbRevokedToken dbRevokedToken);

    @Transactional
    @Modifying
    @Query(value = "delete from REVOKED_TOKEN where EXPIRATION_DATE < CURRENT_TIMESTAMP", nativeQuery = true)
    void removeExpiredTokens();

}
