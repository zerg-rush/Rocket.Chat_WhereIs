package pl.aszul.hot.rwb.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.aszul.hot.rwb.model.db.DbRevokedToken;

import java.util.UUID;

@Service
@Transactional
public class RevokedTokenService {

    private static long nextTokenRemoval/* = 0L*/;
    private final RevokedTokenRepository repository;
    @Value("${jwt.revoked-tokens-flush-interval-in-minutes}")
    private long revokedTokensFlushInterval;

    @Autowired
    public RevokedTokenService(RevokedTokenRepository repository) {
        this.repository = repository;
    }

    void logout(DbRevokedToken dbRevokedToken) {
        repository.save(dbRevokedToken);
        removeExpiredTokens();
    }

/*    public boolean isRevoked(UserContext userContext) {
        final var userId = userContext.getUserId();
        final var expirationDate = userContext.getExpirationDateSeconds();
        return repository.findOneByUserIdAndExpirationDate(userId, expirationDate) != null;
    }*/

    public boolean isRevoked(UUID tokenId) {
        return repository.existsById(tokenId);
    }

    private void removeExpiredTokens() {
        if (System.currentTimeMillis() > nextTokenRemoval) {
            nextTokenRemoval = System.currentTimeMillis() + (revokedTokensFlushInterval * 60 * 1000);
            repository.removeExpiredTokens();
        }
    }

}
