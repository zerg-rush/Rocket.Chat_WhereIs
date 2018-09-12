package pl.aszul.hot.rwb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.aszul.hot.rwb.model.db.DbUser;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<DbUser, String> {

    List<DbUser> findAllByOrderByUsernameAsc();
    void deleteByUsername(String username);

}
