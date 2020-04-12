package storytime.websites;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import storytime.child.Child;
import storytime.child.ChildService;
import storytime.parent.Parent;
import storytime.parent.ParentService;

import javax.validation.Valid;

@Controller
@EnableAutoConfiguration
public class UserSiteController {
    Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    ParentService parentService;

    @Autowired
    ChildService childService;

    @GetMapping("/")
    public String index() {
        log.info("GET / [website root]");
        return "user/_index";
    }

    /*--- Parent Account ----------------------------------------------------*/
    @GetMapping("/sign-up")
    public String signup(Model model) {
        log.info("GET /sign-up");
        model.addAttribute("parent", new Parent());
        return "user/parent-create";
    }

    @PostMapping("/sign-up")
    public String signup(@Valid @ModelAttribute("parent") Parent parent,
                         BindingResult result) {

        if (result.hasErrors()) {
            return "user/parent-create";
        }

        log.info("POST /sign-up -- parent: {}", parent);
        parentService.persist(parent);
        String url = "/parent/" + parent.getId();
        log.info("redirecting to: {}", url);

        return "redirect:" + url;
    }

    @GetMapping("/parent/{id}")
    public String parent__id(Model model,
                             @PathVariable("id") long id) {
        log.info("GET /parent/{}", id);

        // todo should 404 if parent not found
        parentService.getParentById(id).ifPresent(model::addAttribute);

        return "user/parent-home";
    }

    @GetMapping("/parent/{id}/edit")
    public String parent__id__edit(Model model,
                               @PathVariable("id") long id) {
        log.info("GET /parent/edit/{}", id);
        model.addAttribute("id", id);
        return "user/parent-edit";
    }

    /*--- Child & Child Prefs -----------------------------------------------*/
    @GetMapping("/parent/{id}/new-child")
    public String parent__id__new_child(Model model,
                                        @PathVariable("id") long id) {
        log.info("GET /parent/{}/new-child", id);

        Child child = new Child();

        // todo should 404 if parent not found
        parentService.getParentById(id).ifPresent(model::addAttribute);
        model.addAttribute("child", child);

        return "user/child-create";
    }

    @PostMapping("/parent/{id}/new-child")
    public String parent__id__new_child(Model model,
                                        @PathVariable("id") long id,
                                        @Valid @ModelAttribute("child") Child child,
                                        BindingResult result) {

        parentService.getParentById(id).ifPresent(child::setParent);

        if (result.hasErrors()) {
            log.info("result: {}", result);
            return "user/child-create";
        }

        log.info("POST /parent/{}/new-child -- child {}", id, child);

        childService.persist(child);
//        String url = "/parent/" + id + "/welcome";
//        log.info("redirecting to: {}", url);


//        return "redirect:" + url;
        return parent__id(model, id);
    }

    @GetMapping("/parent/{id}/{child_id}")
    public String admin__story__new(Model model,
                                    @PathVariable("id") long id,
                                    @PathVariable("child_id") long childId) {
        log.info("GET /parent/{}/{}", id, childId);
        model.addAttribute("id", id);
        model.addAttribute("child_id", childId);
        return "user/child-edit";
    }
}
