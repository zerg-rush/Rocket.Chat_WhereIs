package pl.aszul.hot.rwb.location;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.aszul.hot.rwb.model.db.DbLocation;

import java.io.File;
import java.util.List;

@Service
@Transactional
public class LocationService {

    private static String locationMapsFolder;
    private final LocationRepository repository;

    public LocationService(@Value("${file.location.maps.folder}") String locationMapsFolder,
                           LocationRepository repository) {
        LocationService.locationMapsFolder = locationMapsFolder;
        this.repository = repository;
    }

    public static boolean mapAvailable(String index) {
        var mapFile = new File(locationMapsFolder + "/" + index + ".jpg");
        return mapFile.exists();
    }

    public DbLocation create(DbLocation dbLocation) {
        return repository.save(dbLocation);
    }

    DbLocation createOrUpdate(DbLocation dbLocation) {
        return repository.save(dbLocation);
    }

    public DbLocation get(String index) {
        return repository.getOne(index);
    }

    public List<DbLocation> getAll() {
        return repository.findAll();
    }

    public void delete(String index) {
        repository.deleteByIndex(index);
    }

    public boolean exists(String index) {
        return repository.existsById(index);
    }

}
