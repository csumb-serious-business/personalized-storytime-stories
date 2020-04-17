package storytime.websites;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import storytime.child.Child;
import storytime.child.ChildService;
import storytime.parent.Parent;
import storytime.parent.ParentService;

import javax.validation.Valid;
import java.util.Optional;

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
    public String sign_up(Model model) {
        log.info("GET /sign-up");
        model.addAttribute("parent", new Parent());
        return "user/parent-create";
    }

    @PostMapping("/sign-up")
    public String sign_up(@Valid @ModelAttribute("parent") Parent parent,
                          BindingResult result) {

        if (result.hasErrors()) {
            return "user/parent-create";
        }

        log.info("POST /sign-up -- parent");

        // persist will only fail on dupe username?
        if (!parentService.persist(parent)) {
            result.rejectValue("username", "error.user",
                    parent.getUsername() +
                            " is already taken.");
            return "user/parent-create";
        }

        String url = "/parent/" + parent.getId();
        log.info("redirecting to: {}", url);

        return "redirect:" + url;
    }

    @GetMapping("/sign-in")
    public String sign_in(Model model) {
        log.info("GET /sign-in");
        model.addAttribute("parent", new Parent());
        return "user/parent-sign-in";
    }

    @PostMapping("/sign-in")
    public String sign_in(@Valid @ModelAttribute("parent") Parent parent,
                          BindingResult result) {

        // check for basic validation fail
        if (result.hasErrors()) {
            return "user/parent-sign-in";
        }

        // lookup the actual username/passphrase to auth
        Optional<Parent> actual = parentService.getParentByUsername(parent.getUsername());

        // username doesn't exist or passphrase doesn't match
        if (!actual.isPresent()) {
            log.info("{} not found", parent);
            result.reject("sign_in",
                    "Either the username doesn't exist or passphrase is incorrect");
            return "user/parent-sign-in";
        } else if (!actual.get().getPassphrase().equals(parent.getPassphrase())) {
            log.info("{} != {}", parent.getPassphrase(), actual.get().getPassphrase());
            result.reject("sign_in",
                    "Either the username doesn't exist or passphrase is incorrect");
            return "user/parent-sign-in";
        }

        log.info("POST /sign-in -- parent: {}", parent);
        String url = "/parent/" + actual.get().getId();
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
