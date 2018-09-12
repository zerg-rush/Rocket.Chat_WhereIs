package pl.aszul.hot.rwb.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.aszul.hot.rwb.model.db.DbLocation;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<DbLocation, String> {

    List<DbLocation> findAllByOrderByIndexAsc();
    void deleteByIndex(String index);

}
