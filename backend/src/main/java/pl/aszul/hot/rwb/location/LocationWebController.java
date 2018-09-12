package pl.aszul.hot.rwb.location;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.aszul.hot.rwb.model.LocationType;
import pl.aszul.hot.rwb.model.db.DbLocation;
import pl.aszul.hot.rwb.model.rest.UserContext;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static pl.aszul.hot.rwb.common.Network.getClientDetails;

@Controller
@Log4j2
public class LocationWebController {

    private final LocationRepository locationRepository;
    private final MapStorageService mapStorageService;

    public LocationWebController(LocationRepository locationRepository, MapStorageService mapStorageService) {
        this.locationRepository = locationRepository;
        this.mapStorageService = mapStorageService;
    }


    @RequestMapping(value = "addLocation.html")
    public String addLocation(Model model){
        model.addAttribute("location", new DbLocation());
        return "addLocation";
    }

    @RequestMapping("/locations.html")
    public String indexLocations(Model model) {
        var locations = locationRepository.findAllByOrderByIndexAsc();
        model.addAttribute("locations", locations);
        model.addAttribute("module","locations");
        return "locations";
    }

    @RequestMapping(value = "/editLocation/{index}")
    public String editLocation(@PathVariable("index") String index, Model model) {
        model.addAttribute("location", locationRepository.findById(index));
        model.addAttribute("pageTitle", "Edit data for location " + index);
        model.addAttribute("locationType", LocationType.values());
        model.addAttribute("mapUrl", locationRepository.findById(index).get().getMapUrl());
        model.addAttribute("hasMap", locationRepository.findById(index).get().hasMap());
        return "editLocation";
    }

    @PostMapping(value = "saveLocation")
    public String save(DbLocation location) {
        locationRepository.save(location);
        return "redirect:/locations";
    }

    @PostMapping("/uploadMap/{index}")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        mapStorageService.storeFile(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/locations";
    }

    @GetMapping("/deleteMap/{index}")
    public String deleteMap(@PathVariable("index") String index, Model model,
                               @ApiIgnore @AuthenticationPrincipal UserContext userContext,
                               HttpServletRequest request) {
        if (!LocationService.mapAvailable(index)) {
            return "Location " + index + " does not exist!";
        }

        var location = locationRepository.findById(index);
        location.get().setMapUrl(null);
        locationRepository.save(location.get());

        try {
            Files.deleteIfExists(Paths.get("./maps/" + index + ".jpg"));
        } catch (IOException e) {
            return "this user does not have avatar!";
        }

        log.info("Map file for location " + index + " successfully deleted by user " +
                getClientDetails(request, (userContext != null ? userContext.getUsername() : "")));

        return "redirect:/locations.html";
    }

}
