package storytime.websites;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import storytime.story.Story;
import storytime.story.StoryService;

import javax.validation.Valid;

@Controller
@EnableAutoConfiguration
public class AdminSiteController {
  private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

  @Autowired
  StoryService storyService;

  @GetMapping("/admin/stories")
  public String admin__stories(Model model) {
    log.info("GET /admin/stories");

    // get all stories here to populate in the view

    return "admin/story-all-show";
  }

  @GetMapping("/admin/story/new-story")
  public String admin__story__new_story(ModelMap model) {
    log.info("GET admin/story/new-story");
    model.addAttribute("story", new Story());
    return "admin/story-create";
  }

  @PostMapping("/admin/story/new-story")
  public String admin__story__new_story(@Valid @ModelAttribute("story") Story story,
      BindingResult result) {

    if (result.hasErrors()) {
      return "admin/story-create";
    }

    log.info("POST /admin/story/new-story -- story: {}", story);
    storyService.create(story);

    return "admin/story-all-show";
  }

  @GetMapping("/admin/story/{id}")
  public String admin__story__id(Model model, @PathVariable("id") long id) {
    log.info("GET /admin/story/{}", id);
    storyService.read(id).ifPresent(model::addAttribute);

    return "admin/story-show";
  }



  @GetMapping("/admin/story/{id}/edit")
  public String admin__story__id__edit(ModelMap model, @PathVariable("id") long id) {
    log.info("GET /admin/story/{}/edit", id);
    storyService.read(id).ifPresent(model::addAttribute);

    return "admin/story-edit";
  }

  @PostMapping("/admin/story/{id}/edit")
  public String admin__story__id__edit(Model model, @PathVariable("id") long id,
      @Valid @ModelAttribute("story") Story story, BindingResult result) {

    if (result.hasErrors()) {
      log.info("result: {}", result);

      return "admin/story-edit";
    }

    log.info("POST /admin/story/{}/edit -- story {}", id, story);

    storyService.update(story);


    storyService.create(story);
    return "admin/story-all-show";
  }

}
