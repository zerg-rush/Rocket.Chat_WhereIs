package pl.aszul.hot.rwb.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.aszul.hot.rwb.location.LocationRepository;
import pl.aszul.hot.rwb.model.LocationType;
import pl.aszul.hot.rwb.model.db.DbLocation;
import pl.aszul.hot.rwb.model.db.DbUser;
import pl.aszul.hot.rwb.model.rest.UserContext;
import pl.aszul.hot.rwb.rc.User;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static pl.aszul.hot.rwb.common.Network.getClientDetails;

@Controller
@Log4j2
public class UserWebController {

/*    @Autowired
    private StudentRepository repository;*/

/*    @Autowired
    private CourseRepository crepository;*/

/*    @Autowired
    private LocationRepository locationRepository;*/

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("getUsers")
    public @ResponseBody
    List<DbUser> getUsers() {
        return userRepository.findAll();
    }

    @RequestMapping("/users.html")
    public String indexUsers(Model model) {
        var users = userRepository.findAllByOrderByUsernameAsc();
        model.addAttribute("users", users);
        model.addAttribute("module","users");
        return "users";
    }

    @RequestMapping(value = "addUser.html")
    public String addUser(Model model) {
        model.addAttribute("user", new DbUser());
        return "addUser";
    }

    @RequestMapping(value = "/editUser/{username}")
    public String editUser(@PathVariable("username") String username, Model model) {
        model.addAttribute("user", userRepository.findById(username));
        model.addAttribute("pageTitle", "Edit data for user " + username);
        model.addAttribute("avatarUrl", userRepository.findById(username).get().getAvatarUrl());
        model.addAttribute("hasAvatar", userRepository.findById(username).get().hasAvatar());
        return "editUser.html";
    }

	@PostMapping("saveUser")
    public String saveUser(DbUser user) {
        userRepository.save(user);
        return "redirect:/users.html";
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable("username") String username, Model model) {
        userRepository.deleteById(username);
        return "redirect:/users.html";
    }

    @GetMapping("/deleteAvatar/{username}")
    public String deleteAvatar(@PathVariable("username") String username, Model model,
                               @ApiIgnore @AuthenticationPrincipal UserContext userContext,
                               HttpServletRequest request) {
        //repository.deleteById(username);
        if (!UserService.avatarAvailable(username)) {
            /*return new ResponseEntity<>(Map.of("error", "User " + username + " does not exist!"),
                    HttpStatus.NOT_FOUND);*/
            return "User " + username + " does not exist!";
        }

        var user = userRepository.findById(username);
        user.get().setAvatarUrl(null);
        userRepository.save(user.get());

        try {
            Files.deleteIfExists(Paths.get("./avatars/" + username + ".jpg"));
        } catch (IOException e) {
/*            return new ResponseEntity<>(Map.of("error", "this user does not have avatar!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);*/
            return "this user does not have avatar!";
        }

        log.info("Avatar file for user " + username +
                " successfully deleted by user " +
                getClientDetails(request, (userContext != null ? userContext.getUsername() : "")));

        return "redirect:/users.html";
    }

}
