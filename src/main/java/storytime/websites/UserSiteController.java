package storytime.websites;

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
        log.info("getting / [website root]");
        return "user/_index";
    }

    @GetMapping("/sign-up")
    public String signup(ModelMap model) {
        log.info("getting /sign-up");
        return "user/parent-create";
    }

    @GetMapping("/parent/edit/{id}")
    public String parent__edit(ModelMap model,
                               @PathVariable("id") long id) {
        log.info("getting /parent/edit/{}", id);
        model.addAttribute("id", id);
        return "user/parent-edit";
    }

    @GetMapping("/parent/{id}/new-child")
    public String admin__story__new(ModelMap model,
                                    @PathVariable("id") long id) {
        log.info("getting /parent/{}/new-child", id);
        model.addAttribute("id", id);
        return "user/child-create";
    }

    @GetMapping("/parent/{id}/{child_id}")
    public String admin__story__new(ModelMap model,
                                    @PathVariable("id") long id,
                                    @PathVariable("child_id") long childId) {
        log.info("getting /parent/{}/{}", id, childId);
        model.addAttribute("id", id);
        model.addAttribute("child_id", childId);
        return "user/child-edit";
    }

}
