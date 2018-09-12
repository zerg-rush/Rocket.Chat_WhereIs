package pl.aszul.hot.rwb.whereis;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WhereisWebController {

    @RequestMapping({"/index", "/index.html", "/index.htm", "/"})
    public String index(Model model) {
        model.addAttribute("module","home");
        return "index.html";
    }

}
