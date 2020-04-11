package storytime.webpages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@EnableAutoConfiguration
public class UserSiteController {
    Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @GetMapping("/")
    public String index(ModelMap model) {
        log.info("GET / [index page]");
//        return "user/_index";
        return "user/_index";
    }

    @GetMapping("/sign-up")
    public String signup(ModelMap model) {
        log.info("GET /sign-up");
        return "user/parent-create";
    }

    @GetMapping("/parent/edit/{id}")
    public String parent__edit(ModelMap model,
                               @PathVariable("id") long id) {
        log.info("GET /parent/edit/{}", id);
        model.addAttribute("id", id);
        return "user/parent-edit";
    }

    @GetMapping("/parent/{id}/new-child")
    public String admin__story__new(ModelMap model,
                                    @PathVariable("id") long id) {
        log.info("GET /parent/{}/new-child", id);
        model.addAttribute("id", id);
        return "user/child-create";
    }

    @GetMapping("/parent/{id}/{child_id}")
    public String admin__story__new(ModelMap model,
                                    @PathVariable("id") long id,
                                    @PathVariable("child_id") long childId) {
        log.info("GET /parent/{}/{}", id, childId);
        model.addAttribute("id", id);
        model.addAttribute("child_id", childId);
        return "user/child-edit";
    }

}
