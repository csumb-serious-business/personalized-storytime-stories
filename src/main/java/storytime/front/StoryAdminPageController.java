package storytime.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@EnableAutoConfiguration
public class StoryAdminPageController {
    Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @GetMapping("/admin/stories")
    public String admin__stories(ModelMap model) {
        log.info("getting admin/stories");
        return "admin/story-all-show";
    }

    @GetMapping("/admin/story/{name}")
    public String admin__story__name(ModelMap model,
                                     @PathVariable("name") String name) {
        log.info("getting admin/story/{}", name);
        model.addAttribute("name", name);
        return "admin/story-one-edit";
    }

    @GetMapping("/admin/story/new")
    public String admin__story__new(ModelMap model) {
        log.info("getting admin/story/new");
        return "admin/story-one-edit";
    }


}
