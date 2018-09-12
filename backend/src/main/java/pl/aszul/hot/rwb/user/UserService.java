package pl.aszul.hot.rwb.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.aszul.hot.rwb.model.db.DbUser;
import pl.aszul.hot.rwb.user.UserRepository;

import java.io.File;
import java.util.List;

@Service
@Transactional
public class UserService {

    private static String userAvatarsFolder;
    private final UserRepository repository;

    public UserService(@Value("${file.user.avatars.folder}") String userAvatarsFolder,
                       pl.aszul.hot.rwb.user.UserRepository repository) {
        UserService.userAvatarsFolder = userAvatarsFolder;
        this.repository = repository;
    }

    public static boolean avatarAvailable(String username) {
        var avatarFile = new File(userAvatarsFolder + "/" + username + ".jpg");
        return avatarFile.exists();
    }

    public DbUser create(DbUser dbUser) {
        return repository.save(dbUser);
    }

    DbUser createOrUpdate(DbUser dbUser) {
        return repository.save(dbUser);
    }

    public DbUser get(String username) {
        return repository.getOne(username);
    }

    public List<DbUser> getAll() {
        return repository.findAll();
    }

    public void delete(String username) {
        repository.deleteByUsername(username);
    }

    public void aquire(String username, String fullname) {
        if (!repository.existsById(username)) {
            var newUser = new DbUser(username, fullname, null, null);
            repository.save(newUser);
        }
    }

    public boolean exists(String username) {
        return repository.existsById(username);
    }

}
